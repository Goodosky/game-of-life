package simulation.animals;

import simulation.*;

public class Sheep extends Animal {
    public Sheep(int x, int y, GameOfLife worldRef) {
        super(x, y, 4, 4, "Sheep", worldRef);
    }

    @Override
    public String draw() {
        return "🐑";
    }
}
