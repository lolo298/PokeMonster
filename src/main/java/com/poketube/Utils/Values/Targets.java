package com.poketube.Utils.Values;

import com.poketube.Utils.Serializable;

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
