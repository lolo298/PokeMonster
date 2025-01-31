package com.pokemonSimulator.Utils.Values;

import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;

public class String implements Serializable, Cloneable {
    private java.lang.String value;

    public String(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String serialize() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        String s = (String) o;

        return this.value.equals(s.getValue());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }

    @Override
    public java.lang.String toString() {
        return this.value;
    }

    @Override
    public String clone() {
        return new String(this.value);
    }
}
