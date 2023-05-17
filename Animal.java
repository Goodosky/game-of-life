package simulation;

import java.io.Serializable;

public abstract class Animal extends Organism {
    protected int range = 1;

    public Animal(int x, int y, int power, int initiative, String name, GameOfLife worldRef) {
        super(x, y, power, initiative, name, worldRef);
    }
    @Override
    public void collision(Organism attacker) {
        if(attacker.getName().equals(name)) {
            // if collision is with the same species, try to reproduce (only if both are older than 6)
            if(attacker.getAge() > 6 && age > 6) reproduce();
        } else {
            fight(attacker);
        }
    }

    @Override
    public void action() {
        // Draw a move direction
        int[] newCoordinates = worldRef.getRandomNeighborPosition(this, range, true);
        int new_x = newCoordinates[0];
        int new_y = newCoordinates[1];

        if(worldRef.getOrganism(new_x, new_y) != null) {
            worldRef.getOrganism(new_x, new_y).collision(this);
        } else {
            move(new_x, new_y);
        }

    }

    public void move(int new_x, int new_y) {
        worldRef.log("🐾 " + name + " moved from (" + x + "," + y + ") to (" + new_x + "," + new_y + ")");
        worldRef.moveOrganism(this, new_x, new_y);
    }
}
