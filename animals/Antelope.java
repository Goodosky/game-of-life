package simulation.animals;

import simulation.*;


public class Antelope extends Animal {
    public Antelope(int x, int y, GameOfLife worldRef) {
        super(x, y, 4, 4, "Antelope", worldRef);
        this.range = 2;
    }

    @Override
    public String draw() {
        return "🦌";
    }

    @Override
    public void fight(Organism attacker) {
        double chance = Math.random();

        if(chance < 0.5) {
            super.fight(attacker);
            return;
        }

        int[] newCoordinates = worldRef.getRandomNeighborPosition(this, 1, false);
        int new_x = newCoordinates[0];
        int new_y = newCoordinates[1];

        if(x == new_x && y == new_y) {
            worldRef.log("🎁🎁🎁🎁🎁🎁 Antelope had nowhere to run! He has to fight now...");
            super.fight(attacker);
        } else {
            worldRef.log("🎁🎁🎁🎁🎁🎁 Antelope escaped from the fight");
            move(new_x, new_y);
        }
    }
}
