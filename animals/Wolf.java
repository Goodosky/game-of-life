package simulation.animals;

import simulation.Animal;
import simulation.GameOfLife;

import java.io.Serializable;

public class Wolf extends Animal {
    public Wolf(int x, int y, GameOfLife worldRef) {
        super(x, y, 9, 5, "Wolf", worldRef);
    }

    @Override
    public String draw() {
        return "🐺";
    }
}
