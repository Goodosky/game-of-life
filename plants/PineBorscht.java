package simulation.plants;

import simulation.*;

public class PineBorscht extends Plant {
    public PineBorscht(int x, int y, GameOfLife worldRef) {
        super(x, y, 10,  "PineBorscht", worldRef);
    }

    @Override
    public String draw() {
        return "🌾";
    }

    @Override
    public void collision(Organism attacker) {
        // Attacker kills PineBorscht
        worldRef.log(attacker.getName() + " (attacker) ate" + name + " at (" + x + ", " + y + ")");
        worldRef.removeOrganism(this);

        // but attacker also dies
        worldRef.log(attacker.getName() + " ate PineBorscht and died");
        worldRef.removeOrganism(attacker);
    }

    @Override
    public void action() {
        super.action();

        // PineBorscht also kills all organisms in its vicinity
        for(int i = x-1; i <= x+1; i++) {
            for(int j = y-1; j <= y+1; j++) {
                if (i < 0 || i >= GameOfLife.WIDTH || j < 0 || j >= GameOfLife.HEIGHT || i == x && j == y) {
                    continue; // Skip if out of bounds or self
                }

                Organism organism = worldRef.getOrganism(i, j);
                if(organism != null) {
                    worldRef.log("💀 " + name + " killed " + organism.getName() + " at (" + i + ", " + j + ")");
                    worldRef.removeOrganism(organism);
                }
            }
        }
    }
}
