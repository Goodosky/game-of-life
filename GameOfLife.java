package simulation;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.*;
import static javax.swing.ScrollPaneConstants.*;

import simulation.animals.*;



public class GameOfLife extends JFrame  {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int CELL_SIZE = 20;
    private static final int FRAME_HEIGHT = 650;
    private static final int FRAME_WIDTH = 650;
    private static final int LOG_PANEL_HEIGHT = 10;
    private static final int LOG_PANEL_WIDTH = 35;
    private static final double INITIAL_WORLD_FILLING = 0.1;

    private static final String SAVE_FILE_PATH = "game_save.txt";
    private int turn = 1;
    private Organism[][] board;
    private ArrayList<Organism> organisms;
    private ArrayList<Organism> organismsToRemove;
    private ArrayList<Organism> organismsToAdd;
    private static final Map<String, String> availableOrganisms = new HashMap<>();
    private JTextArea logTextArea;
    private JPanel boardPanel;


    public GameOfLife() {
        super("Game of Life");
        initializeGUI();

        availableOrganisms.put("Sheep", "simulation.animals.Sheep");
        availableOrganisms.put("Wolf", "simulation.animals.Wolf");
        availableOrganisms.put("Fox", "simulation.animals.Fox");
        availableOrganisms.put("Antelope", "simulation.animals.Antelope");
        availableOrganisms.put("Turtle", "simulation.animals.Turtle");

        availableOrganisms.put("Dandelion", "simulation.plants.Dandelion");
        availableOrganisms.put("Grass", "simulation.plants.Grass");

    }

    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add Board to JFrame
        boardPanel = new JPanel();
        boardPanel.setBackground(Color.GREEN);
        add(boardPanel, BorderLayout.CENTER);

        // Add menu to JFrame
        JPanel menuPanel = new JPanel();

        logTextArea = new JTextArea(LOG_PANEL_HEIGHT,LOG_PANEL_WIDTH);
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        menuPanel.add(logScrollPane);

        JPanel btnsPanel = new JPanel();
        btnsPanel.setLayout(new BoxLayout(btnsPanel, BoxLayout.Y_AXIS));

        JButton saveGameBtn = new JButton("Save Game");
        saveGameBtn.addActionListener(e -> saveGame());
        btnsPanel.add(saveGameBtn);

        JButton loadGameBtn = new JButton("Load Game");
        loadGameBtn.addActionListener(e -> loadGame());
        btnsPanel.add(loadGameBtn);

        JButton nextTurnBtn = new JButton("Next Turn");
        nextTurnBtn.addActionListener(e -> nextTurn());
        btnsPanel.add(nextTurnBtn);

        menuPanel.add(btnsPanel);
        add(menuPanel, BorderLayout.SOUTH);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
    }

    private void saveGame() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(SAVE_FILE_PATH);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(board);
            objectOutputStream.writeObject(organisms);
            objectOutputStream.writeObject(turn);
            objectOutputStream.writeObject(logTextArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGame() {
        try (FileInputStream fileInputStream = new FileInputStream(SAVE_FILE_PATH);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            board = (Organism[][]) objectInputStream.readObject();
            organisms = (ArrayList<Organism>) objectInputStream.readObject();
            turn = (int) objectInputStream.readObject();
            String logText = (String) objectInputStream.readObject();

            logTextArea.setText(logText);

            for (Organism organism : organisms) {
                organism.setWorldRef(this);
            }

            drawBoard();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void log(String log) {
        logTextArea.append(log + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }

    private void initializeBoard() {
        board = new Organism[WIDTH][HEIGHT];
        organisms = new ArrayList<Organism>();
        organismsToAdd = new ArrayList<Organism>();
        organismsToRemove = new ArrayList<Organism>();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++){
                board[j][i] = null;
            }
        }

        // Add random organism to the board
        int number_of_organisms = (int) (WIDTH * HEIGHT * INITIAL_WORLD_FILLING);

        for (int i = 0; i < number_of_organisms; i++) {
            // Draw position
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);

            while (board[x][y] != null) {
                x = (int) (Math.random() * WIDTH);
                y = (int) (Math.random() * HEIGHT);
            }

            // Draw organism
            int random_index = (int) (Math.random() * availableOrganisms.size());
            String organismName = (String) availableOrganisms.keySet().toArray()[random_index];

            addOrganism(organismName, x, y);
        }

        updateOrganismsArray();
    }

    private void drawBoard() {
        boardPanel.removeAll();

        int startX = boardPanel.getWidth()/2 - WIDTH*CELL_SIZE/2;
        int startY = boardPanel.getHeight()/2 - HEIGHT*CELL_SIZE/2;

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                JLabel label = new JLabel();
                label.setLocation(i*CELL_SIZE + startX, j*CELL_SIZE + startY);
                label.setSize(CELL_SIZE, CELL_SIZE);

                if (board[j][i] != null) {
                    label.setText(board[j][i].draw());
                } else {
                    label.setText("🟢");
                }

                addMouseListenerToLabel(label, j, i);
                boardPanel.add(label);
            }
        }

        boardPanel.repaint();
    }

    public void startGame() {
        initializeBoard();
        drawBoard();
    }

    public void nextTurn() {
        this.setTitle("Game of Life - Turn " + turn);
        // this.setTitle("Oskar Radziewicz 193676");
        log("\n --- Next turn: " + turn + " ---");

        for (Organism organism : organisms) {
            organism.increaseAge();
            if(organism.isAlive()) organism.action();
        }

        updateOrganismsArray();

        drawBoard();
        turn++;
    }

    public void updateOrganismsArray() {
        for (Organism organism : organismsToRemove) {
            organisms.remove(organism);
        }

        for (Organism organism : organismsToAdd) {
            organisms.add(organism);
        }

        if (organismsToAdd.size() > 0 || organismsToRemove.size() > 0) {
            // Sort organisms by initiative and age
            organisms.sort((o1, o2) -> {
                if (o1.getInitiative() == o2.getInitiative()) {
                    return o2.getAge() - o1.getAge();
                } else {
                    return o2.getInitiative() - o1.getInitiative();
                }
            });
        }

        organismsToRemove.clear();
        organismsToAdd.clear();
    }

    public Organism getOrganism(int x, int y) {
        return board[x][y];
    }

    public void addOrganism(String name, int x, int y) {
        try {
            Class<?> organismClass = Class.forName(availableOrganisms.get(name));
            Constructor<?> constructor = organismClass.getConstructor(int.class, int.class, GameOfLife.class);
            Organism organism = (Organism) constructor.newInstance(x, y, this);

            board[x][y] = organism;
            organismsToAdd.add(organism);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Unknown type of organism: " + name, e);
        }
    }

    public void removeOrganism(Organism organism) {
        board[organism.getX()][organism.getY()] = null;
        organism.setIsAlive(false);
        organismsToRemove.add(organism);
    }

    public void moveOrganism(Organism organism, int new_x, int new_y) {
        board[organism.getX()][organism.getY()] = null;
        board[new_x][new_y] = organism;
        organism.setPosition(new_x, new_y);
    }

    public void addMouseListenerToLabel(JLabel label, int x, int y) {
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                log("Clicked on (" + x + "," + y + ")");

                // Skip if there is an organism on the clicked field
                if(board[x][y] != null) return;

                // Select organism type from a list
                String[] options = availableOrganisms.keySet().toArray(new String[0]);
                String selectedOrganism = (String) JOptionPane.showInputDialog(null, "Choose organism type", "Add organism", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                // Add selected organism to the board
                addOrganism(selectedOrganism, x, y);
                updateOrganismsArray();
                drawBoard();
            }
        });
    }

    public int[] getRandomNeighborPosition(Organism organism, int range, boolean can_be_occupied) {
        int direction = (int) (Math.random() * 4);

        for (int i = 0; i < 4; i++) {
            int new_x = organism.getX();
            int new_y = organism.getY();

            int step_range = (int) (Math.random() * range) + 1;

            switch(direction) {
                case 0:
                    new_x += step_range;
                    break;
                case 1:
                    new_x -= step_range;
                    break;
                case 2:
                    new_y += step_range;
                    break;
                case 3:
                    new_y -= step_range;
                    break;
            }

            //  return a new position if it's in the world
            if(new_x >= 0 && new_x < WIDTH && new_y >= 0 && new_y < HEIGHT) {
                if (can_be_occupied || board[new_x][new_y] == null) {
                    return new int[]{new_x, new_y};
                }
            }

            // ...if not, try a next direction
            direction = (direction + 1) % 4;
        }

        // If (can_be_occupied == false) and there is no free space, the coordinates will not change
        return new int[]{organism.getX(), organism.getY()};
    }

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        game.startGame();
    }
}
