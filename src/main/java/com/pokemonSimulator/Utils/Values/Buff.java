package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Serializable;
import com.pokemonSimulator.Utils.Values.enums.Targets;

public class Buff implements Serializable {

    private final String stat;
    private final Integer stage;
    private final Targets target;

    public Buff(String stat, Integer stage, Targets target) {
        this.stat = stat;
        this.stage = stage;
        this.target = target;
    }


    @Override
    public java.lang.String serialize() {
        return this.stat.serialize() + "$" + this.stage.serialize() + "$" + this.target.serialize();
    }
}
