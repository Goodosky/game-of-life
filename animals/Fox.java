package simulation.animals;

import simulation.Organism;
import simulation.Animal;
import simulation.GameOfLife;

import java.io.Serializable;

public class Fox extends Animal {
    public Fox(int x, int y, GameOfLife worldRef) {
        super(x, y, 3, 7, "Fox", worldRef);
    }

    @Override
    public String draw() {
        return "🦊";
    }

    @Override
    public void action() {
        // Draw a move direction
        int[] newCoordinates = worldRef.getRandomNeighborPosition(this, range, true);
        int new_x = newCoordinates[0];
        int new_y = newCoordinates[1];

        Organism opponent = worldRef.getOrganism(new_x, new_y);

        if(opponent == null) {
            move(new_x, new_y);
        } else if (power >= opponent.getPower()) {
            opponent.collision(this);
        } else {
            worldRef.log("🎁 Fox gave up attacking the stronger one");

        }
    }
}
