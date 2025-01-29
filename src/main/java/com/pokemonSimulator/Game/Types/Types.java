package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Utils.Serializable;

import java.util.ArrayList;
import java.util.List;

public enum Types implements Serializable {
    FIRE, WATER, GRASS, GROUND, ELECTRIC, NORMAL;

    private final List<Types> strengths = new ArrayList<>();
    private final List<Types> weaknesses = new ArrayList<>();

    static {
        FIRE.strengths.add(GRASS);
        FIRE.weaknesses.add(WATER);

        GRASS.strengths.add(GROUND);
        GRASS.weaknesses.add(FIRE);

        GROUND.strengths.add(ELECTRIC);
        GROUND.weaknesses.add(GRASS);

        ELECTRIC.strengths.add(WATER);
        ELECTRIC.weaknesses.add(GROUND);

        WATER.strengths.add(FIRE);
        WATER.weaknesses.add(ELECTRIC);
    }

    public List<Types> getStrengths() {
        return strengths;
    }

    public List<Types> getWeaknesses() {
        return weaknesses;
    }

    public String serialize() {
        return this.name();
    }
}