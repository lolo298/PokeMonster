package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;
import com.pokemonSimulator.Utils.Values.enums.Targets;

public class Target implements Serializable {
    protected Targets target;

    public Target(Targets target) {
        this.target = target;
    }

    @Override
    public java.lang.String toString() {
        return this.target.toString();
    }

    public java.lang.String serialize() {
        return this.target.serialize();
    }
}
