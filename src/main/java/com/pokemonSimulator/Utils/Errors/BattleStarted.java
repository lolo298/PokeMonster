package com.pokemonSimulator.Utils.Errors;

public class BattleStarted extends RuntimeException {
    public BattleStarted(String message) {
        super(message);
    }
}
