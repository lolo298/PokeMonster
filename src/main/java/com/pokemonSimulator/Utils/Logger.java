package com.pokemonSimulator.Utils;

public class Logger {
    private static LogLevel level;

    public static void log(String message) {
        if (level.compareTo(LogLevel.INFO) < 0) {
            return;
        }
        System.out.println("\u001B[0;96m[INFO] " + message + "\u001B[0m");
    }

    public static void warn(String message) {
        if (level.compareTo(LogLevel.WARNING) < 0) {
            return;
        }
        System.out.println("\u001B[0;93m[WARNING] " + message + "\u001B[0m");
    }

    public static void error(String message) {
        if (level.compareTo(LogLevel.ERROR) < 0) {
            return;
        }
        System.err.println("\u001B[0;91m[ERROR] " + message + "\u001B[0m");
    }

    public enum LogLevel {
        NONE,
        ERROR,
        WARNING,
        INFO
    }

    public static void setLevel(LogLevel level) {
        Logger.level = level;
    }
}
