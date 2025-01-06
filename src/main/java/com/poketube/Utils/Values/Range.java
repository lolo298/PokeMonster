package com.poketube.Utils.Values;

import com.poketube.Utils.Serializable;

public class Range implements Serializable {
    private Integer min;
    private Integer max;

    public Range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
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

    public java.lang.String serialize() {
        return this.min.serialize() + "/" + this.max.serialize();
    }

    public java.lang.String toString() {
        return this.serialize();
    }
}
