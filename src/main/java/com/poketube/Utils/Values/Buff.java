package com.poketube.Utils.Values;

import com.poketube.Utils.Serializable;

public class Buff implements Serializable {

    private String stat;
    private Integer stage;
    private Targets target;

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
