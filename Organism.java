package simulation;

import java.io.Serializable;

public abstract class Organism implements Serializable {
    protected int x;
    protected int y;
    protected int power;
    protected int initiative;
    protected int age;
    protected GameOfLife worldRef;
    protected String name;
    protected boolean isAlive;


    public Organism(int x, int y, int power, int initiative, String name, GameOfLife worldRef) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.initiative = initiative;
        this.name = name;
        this.worldRef = worldRef;

        this.age = 0;
        this.isAlive = true;

    }

    public abstract void action();
    public abstract void collision(Organism attacker);
    public abstract String draw();

    public void reproduce() {
        int[] newCoordinates = worldRef.getRandomNeighborPosition(this, 1, false);
        int new_x = newCoordinates[0];
        int new_y = newCoordinates[1];

        if  (x != new_x || y != new_y) {
            // Create a new organism of the same species
            worldRef.addOrganism(name, new_x, new_y);
            worldRef.log("💞 " + name + " has added at (" + new_x + ", " + new_y + ")");
        } else {
            worldRef.log("💔 " + name + " tried to reproduce, but there was no space around");
        }
    }

    public void fight(Organism attacker) {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAge() {
        return age;
    }

    public int getPower() {
        return power;
    }

    public int getInitiative() {
        return initiative;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void increaseAge() {
        age++;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setWorldRef(GameOfLife worldRef) {
        this.worldRef = worldRef;
    }

    public void setPower(int power) {
        this.power = power;
    }


}
