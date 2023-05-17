package simulation.animals;

import simulation.Animal;
import simulation.GameOfLife;

import java.io.Serializable;

public class Sheep extends Animal {
    public Sheep(int x, int y, GameOfLife worldRef) {
        super(x, y, 4, 4, "Sheep", worldRef);
    }

    @Override
    public String draw() {
        return "🐑";
    }
}
