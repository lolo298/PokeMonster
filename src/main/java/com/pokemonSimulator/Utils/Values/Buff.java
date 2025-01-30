package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Serializable;
import com.pokemonSimulator.Utils.Values.enums.BuffStat;
import com.pokemonSimulator.Utils.Values.enums.Targets;

public class Buff implements Serializable {

    private final BuffStat stat;
    private final Integer stage;
    private final Targets target;

    public Buff(BuffStat stat, Integer stage, Targets target) {
        this.stat = stat;
        this.stage = stage;
        this.target = target;
    }

    public BuffStat getStat() {
        return stat;
    }

    public Integer getStage() {
        return stage;
    }

    public Targets getTarget() {
        return target;
    }

    @Override
    public java.lang.String serialize() {
        return this.stat.serialize() + "$" + this.stage.serialize() + "$" + this.target.serialize();
    }
}
