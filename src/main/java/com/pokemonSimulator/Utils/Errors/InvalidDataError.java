package com.pokemonSimulator.Utils.Errors;

public class InvalidDataError extends Exception {
    private int line;
    private String source;

    public InvalidDataError(int line, String source) {
        this.line = line;
        this.source = source;
    }

    @Override
    public String getMessage() {

        String[] lines = source.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            builder.append(i).append(": ").append(lines[i]).append("\n");
        }

        return "Invalid data at line " + line + ": \n" + builder;
    }
}
