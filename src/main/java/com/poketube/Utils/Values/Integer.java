package com.poketube.Utils.Values;

import com.poketube.Utils.Serializable;

public class Integer implements Serializable {
    private int value;
    public Integer(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public java.lang.String serialize() {
        return this.value + "";
    }


    public Integer minusLeft(int other) {
        this.value = this.value - other;
        return this;
    }

    public Integer minusRight(int other) {
        this.value = other - this.value;
        return this;
    }

    public Integer plus(int other) {
        this.value = this.value + other;
        return this;
    }
}
