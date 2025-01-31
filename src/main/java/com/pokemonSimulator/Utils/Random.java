package com.pokemonSimulator.Utils;

public class Random {
    public static int generateInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public static double generateDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
