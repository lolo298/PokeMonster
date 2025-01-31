package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;

public class Range implements Serializable {
    private Integer min;
    private Integer max;

    public Range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Range(int min, int max) {
        this.min = new Integer(min);
        this.max = new Integer(max);
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer generateValue() {
        return new Integer((int) Math.round(Math.random() * (this.max.getValue() - this.min.getValue() + 1) + this.min.getValue()));
    }

    public java.lang.String serialize() {
        return this.min.serialize() + "/" + this.max.serialize();
    }

    public java.lang.String toString() {
        return this.serialize();
    }
}
