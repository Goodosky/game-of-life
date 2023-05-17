package simulation.plants;

import simulation.*;

public class Nightshade extends Plant {
    public Nightshade(int x, int y, GameOfLife worldRef) {
        super(x, y, 99,  "Nightshade", worldRef);
    }

    @Override
    public String draw() {
        return "🫐";
    }

    @Override
    public void collision(Organism attacker) {
        // Attacker kills Nightshade
        worldRef.log(attacker.getName() + " (attacker) ate" + name + " at (" + x + ", " + y + ")");
        worldRef.removeOrganism(this);

        // but attacker also dies
        worldRef.log(attacker.getName() + " ate Nightshade and died");
        worldRef.removeOrganism(attacker);
    }
}
