package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Serializable;
import com.pokemonSimulator.Utils.Values.Range;
import com.pokemonSimulator.Utils.Values.String;

public class Monster implements Serializable {
    private String name;
    private Type type;

    private Range attack;
    private Range defense;
    private Range health;
    private Range speed;

    public Range getAttack() {
        return attack;
    }

    public void setAttack(Range attack) {
        this.attack = attack;
    }

    public Range getDefense() {
        return defense;
    }

    public void setDefense(Range defense) {
        this.defense = defense;
    }

    public Range getHealth() {
        return health;
    }

    public void setHealth(Range health) {
        this.health = health;
    }

    public Range getSpeed() {
        return speed;
    }

    public void setSpeed(Range speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public java.lang.String toString() {
        return this.getName().getValue();
    }

    public java.lang.String serialize() {
        return "name: " + this + "\n" +
                "type: " + this.getType() + "\n" +
                "health: " + this.health.serialize() + "\n" +
                "attack: " + this.attack.serialize() + "\n" +
                "defense: " + this.defense.serialize() + "\n" +
                "speed: " + this.speed.serialize();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Monster m) {
            return m.serialize().equals(this.serialize());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.serialize().hashCode();
    }

}
