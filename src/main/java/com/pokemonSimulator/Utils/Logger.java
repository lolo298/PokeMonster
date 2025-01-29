package com.pokemonSimulator.Utils;

public class Logger {
    public static void log(String message) {
        System.out.println("\u001B[0;96m[INFO] " + message + "\u001B[0m");
    }

    public static void error(String message) {
        System.err.println("\u001B[0;91m[ERROR] " + message + "\u001B[0m");
    }

    public static void warn(String message) {
        System.out.println("\u001B[0;93m[WARNING] " + message + "\u001B[0m");
    }
}
