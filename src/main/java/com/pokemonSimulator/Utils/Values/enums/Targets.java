package com.pokemonSimulator.Utils.Values.enums;

import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;

import java.lang.String;

public enum Targets implements Serializable {
    SELF,
    ENEMY;

    @Override
    public String toString() {
        return this.name();
    }

    public String serialize() {
        return this.name();
    }
}
