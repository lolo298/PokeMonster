package com.pokemonSimulator;

import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.View.App;

class Main {
    public static void main(String[] args) {
        PokemonSimulator pokemonSimulator = PokemonSimulator.getInstance();
        pokemonSimulator.start();

        double tmp = Random.generateValue(1, 2);
        Logger.log(Double.toString(tmp));

        App.startWindow();
    }
}
