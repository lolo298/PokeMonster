package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Serializable;

public class Target implements Serializable {
    protected Targets target;

    public Target(Targets target) {
        this.target = target;
    }

    public Targets getTarget() {
        return this.target;
    }

    @Override
    public java.lang.String toString() {
        return this.target.toString();
    }

    public java.lang.String serialize() {
        return this.target.serialize();
    }
}
