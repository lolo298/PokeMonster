package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Serializable;

public class Float implements Serializable {
    private double value;

    public Float(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public java.lang.String serialize() {
        return this.value + "";
    }


    public Float minusLeft(double other) {
        this.value = this.value - other;
        return this;
    }

    public Float minusRight(double other) {
        this.value = other - this.value;
        return this;
    }

    public Float plus(double other) {
        this.value = this.value + other;
        return this;
    }

}
