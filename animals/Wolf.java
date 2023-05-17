package simulation.animals;

import simulation.*;

public class Wolf extends Animal {
    public Wolf(int x, int y, GameOfLife worldRef) {
        super(x, y, 9, 5, "Wolf", worldRef);
    }

    @Override
    public String draw() {
        return "🐺";
    }
}
