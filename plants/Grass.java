package simulation.plants;

import simulation.*;

public class Grass extends Plant {
    public Grass(int x, int y, GameOfLife worldRef) {
        super(x, y, 0,  "Grass", worldRef);
    }

    @Override
    public String draw() {
        return "🌱";
    }
}
