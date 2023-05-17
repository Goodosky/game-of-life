package simulation.plants;

import simulation.*;

public class Dandelion extends Plant {
    public Dandelion(int x, int y, GameOfLife worldRef) {
        super(x, y, 0,  "Dandelion", worldRef);
    }

    @Override
    public String draw() {
        return "🌼";
    }

    @Override
    public void action() {
        for(int i = 0; i < 3; i++) {
            super.action();
        }
    }
}
