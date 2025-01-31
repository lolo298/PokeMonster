package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;
import com.pokemonSimulator.Utils.Values.Range;

public class Monster extends MonsterSprite implements Serializable {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.getName().getValue();
    }

    public String serialize() {
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
