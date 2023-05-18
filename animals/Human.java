package simulation.animals;

import java.awt.event.*;
import java.io.*;

import simulation.*;



public class Human extends Animal  {
    private static int direction;
    protected static int specialAbilityCooldown;

    public Human(int x, int y, GameOfLife worldRef) {
        super(x, y, 5, 4, "Human", worldRef);

        this.specialAbilityCooldown = 0;
        this.direction = 0;

        worldRef.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isAlive) return;

                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_UP) {
                    direction = 1;
                    worldRef.log("🎮 Human will move up next turn");
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    direction = 2;
                    worldRef.log("🎮 Human will move right next turn");
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    direction = 3;
                    worldRef.log("🎮 Human will move down next turn");

                } else if (keyCode == KeyEvent.VK_LEFT) {
                    direction = 4;
                    worldRef.log("🎮 Human will move left next turn");
                } else if (keyCode == KeyEvent.VK_A) {
                    activateSpecialAbilityCooldown();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    @Override
    public String draw() {
        return "🥷";
    }

    @Override
    public void action() {
        if (specialAbilityCooldown > 0) {
            power = 5 + specialAbilityCooldown;
            specialAbilityCooldown--;
        }

        if (direction == 0) return;

        int new_x, new_y;
        switch (direction) {
            case 1:
                new_x = x;
                new_y = y - 1;
                break;
            case 2:
                new_x = x + 1;
                new_y = y;
                break;
            case 3:
                new_x = x;
                new_y = y + 1;
                break;
            case 4:
                new_x = x - 1;
                new_y = y;
                break;
            default:
                throw new IllegalStateException("Human: Unexpected direction value: " + direction);
        }

        if (new_x < 0 || new_x >= worldRef.getWidth() || new_y < 0 || new_y >= worldRef.getHeight()) {
            worldRef.log("🎮 Human tried to move out of the map, but he hit the wall");
        } else if (worldRef.getOrganism(new_x, new_y) != null) {
            worldRef.getOrganism(new_x, new_y).collision(this);
        } else {
            move(new_x, new_y);
        }

        direction = 0; // reset direction
    }

    private void activateSpecialAbilityCooldown() {
        if (specialAbilityCooldown > 0) {
            worldRef.log("🎮 Human special ability is on cooldown! " + specialAbilityCooldown + " turns left");
        } else {
            specialAbilityCooldown = 5;
            worldRef.log("🎮 Human special ability activated!");
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(specialAbilityCooldown);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        specialAbilityCooldown = in.readInt();
    }
}