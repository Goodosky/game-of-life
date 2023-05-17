package simulation.plants;

import simulation.*;

public class Guarana extends Plant {
    public Guarana(int x, int y, GameOfLife worldRef) {
        super(x, y, 0,  "Guarana", worldRef);
    }

    @Override
    public String draw() {
        return "🍇";
    }

    @Override
    public void collision(Organism attacker) {
        // Attacker kills Guarana
        worldRef.log(attacker.getName() + " (attacker) ate" + name + " at (" + x + ", " + y + ")");
        worldRef.removeOrganism(this);
        worldRef.moveOrganism(attacker, x, y);

        // but also gets +3 power
        worldRef.log(attacker.getName() + " ate Guarana and got +3 power");
        attacker.setPower(attacker.getPower() + 3);
    }
}
