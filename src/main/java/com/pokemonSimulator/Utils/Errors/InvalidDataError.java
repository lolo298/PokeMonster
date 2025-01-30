package com.pokemonSimulator.Utils.Errors;

public class InvalidDataError extends Exception {
    private final int line;
    private final String source;

    public InvalidDataError(int line, String source) {
        this.line = line;
        this.source = source;
    }

    @Override
    public String getMessage() {
        if (line == -1) {
            return "Invalid data";
        }

        String[] lines = source.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = line - 5; i < line + 5; i++) {
            if (line <= 0 || line >= lines.length) {
                continue;
            }
            builder.append(i).append(": ").append(lines[i]).append("\n");
        }

        return "Invalid data at line " + line + ": \n" + builder;
    }
}
