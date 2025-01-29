package com.pokemonSimulator.Utils;

public class Random {
    public static <T extends Number> double generateValue(T min, T max) {
        return Math.floor(Math.random() * (max.doubleValue() - min.doubleValue() + 1) + min.doubleValue());
    }
}
