package simulation.animals;

import simulation.*;


public class Turtle extends Animal {
    public Turtle(int x, int y, GameOfLife worldRef) {
        super(x, y, 2, 1, "Turtle", worldRef);
    }

    @Override
    public String draw() {
        return "🐢";
    }

    @Override
    public void action() {
        double chance = Math.random();

        if(chance > 0.75) {
            super.action();
        } else {
            worldRef.log("🎁 Turtle is too lazy to move...");
        }
    }

    @Override
    public void fight(Organism attacker) {
        if(attacker.getPower() < 5) {
            worldRef.log("🎁 Turtle defended himself from " + attacker.getName());
        } else {
            super.fight(attacker);
        }
    }
}
