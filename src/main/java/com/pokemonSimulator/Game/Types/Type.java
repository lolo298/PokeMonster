package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Utils.Serializable;

import java.util.List;

public abstract class Type implements Serializable {
    protected Types type;

    public Type(Types type) {
        this.type = type;
    }

    public Types getType() {
        return this.type;
    }

    public List<Types> getWeaknesses() {
        return this.type.getWeaknesses();
    }

    public List<Types> getStrengths() {
        return this.type.getStrengths();
    }

    public abstract boolean hasSkill();

    //TODO
//    public abstract void useSkill();

    @Override
    public String toString() {
        return this.type.toString();
    }

    public String serialize() {
        return this.type.serialize();
    }
}
