package com.pokemonSimulator.Utils.Values.enums;

import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;

public enum BuffStat implements Serializable {
    ATTACK,
    DEFENSE,
    SPEED;

    @Override
    public String toString() {
        return this.name();
    }

    public String serialize() {
        return this.name();
    }
}
