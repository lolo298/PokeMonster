package com.pokemonSimulator.Utils;

public class Serializer {

    public String serialize(Serializable obj) {
        StringBuilder builder = new StringBuilder();
        String data = obj.serialize();
        String[] lines = data.split("\n");

        for (String line : lines) {
            builder.append("\t").append(line).append("\n");
        }

        builder.deleteCharAt(builder.length() - 1);

        return obj.getClass() + "\n" +
                builder + "\n" +
                "End" + obj.getClass() + "\n\n";
    }


}
