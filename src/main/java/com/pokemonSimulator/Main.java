package com.pokemonSimulator;

import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.View.App;

class Main {
    public static void main(String[] args) {
        Logger.setLevel(Logger.LogLevel.NONE);

        PokemonSimulator pokemonSimulator = PokemonSimulator.getInstance();
        pokemonSimulator.start();

        App.startWindow();
    }
}
