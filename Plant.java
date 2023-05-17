package simulation;

public abstract class Plant extends Organism {
    private static final double PLANT_REPRODUCTION_CHANCE = 0.02;

    public Plant(int x, int y, int power, String name, GameOfLife worldRef) {
        super(x, y, power, 0, name, worldRef);
    }
    @Override
    public void collision(Organism attacker) {
        if (attacker.getPower() >= power) {
            // Attacker kills defender
            worldRef.log("💀 " + attacker.getName() + " (attacker) ate " + name + " at (" + x + ", " + y + ")");
            worldRef.removeOrganism(this);
            worldRef.moveOrganism(attacker, x, y);
        } else {
            // Defender kills attacker
            worldRef.log("💀 " + name + " (defender) ate " + attacker.getName() + " at (" + x + ", " + y + ")");
            worldRef.removeOrganism(attacker);
        }
    }

    @Override
    public void action() {
        double chance = Math.random();
        if(chance < PLANT_REPRODUCTION_CHANCE) {
            reproduce();
        }
    }
}
