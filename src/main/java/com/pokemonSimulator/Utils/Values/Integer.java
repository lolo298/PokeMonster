package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;

public class Integer implements Serializable, Comparable<Integer>, Cloneable {
    private int value;

    public Integer(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void decrement() {
        this.value--;
    }

    public java.lang.String serialize() {
        return this.value + "";
    }


    public Integer minusLeft(int other) {
        this.value = this.value - other;
        return this;
    }

    public Integer minusLeft(Integer other) {
        return minusLeft(other.value);
    }

    public Integer minusRight(int other) {
        this.value = other - this.value;
        return this;
    }

    public Integer minusRight(Integer other) {
        return minusRight(other.value);
    }

    public Integer plus(int other) {
        this.value = this.value + other;
        return this;
    }

    public Integer plus(Integer other) {
        return plus(other.value);
    }

    public Integer multiply(int other) {
        this.value = this.value * other;
        return this;
    }

    public Integer multiply(Integer other) {
        return multiply(other.value);
    }

    public Integer divide(int other) {
        this.value = this.value / other;
        return this;
    }

    public Integer divide(Integer other) {
        return divide(other.value);
    }


    @Override
    public int compareTo(Integer o) {
        return compare(this.value, o.value);
    }

    public static int compare(int x, int y) {
        return java.lang.Integer.compare(x, y);
    }

    @Override
    public Integer clone() {
        return new Integer(this.value);
    }

    @Override
    public java.lang.String toString() {
        return this.serialize();
    }
}
