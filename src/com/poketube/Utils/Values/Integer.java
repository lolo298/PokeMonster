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
}
