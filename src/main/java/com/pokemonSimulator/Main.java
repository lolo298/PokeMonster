package com.pokemonSimulator;

import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.View.App;

class Main {
    public static void main(String[] args) {
        Logger.setLevel(Logger.LogLevel.INFO);

        PokemonSimulator pokemonSimulator = PokemonSimulator.getInstance();
        pokemonSimulator.start();


        App.startWindow();
    }
}
