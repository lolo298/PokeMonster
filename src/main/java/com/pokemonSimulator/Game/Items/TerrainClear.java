package com.pokemonSimulator.Game.Items;

import com.pokemonSimulator.Game.Game;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Values.enums.ItemTarget;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;

public class TerrainClear extends Item {
    private final Terrain targetTerrain;

    public TerrainClear(String name, Terrain targetTerrain, int quantity, ItemTarget target) {
        super(name, quantity, target);
        this.targetTerrain = targetTerrain;
    }

    @Override
    public void use(BattleMon target) {
        Game game = PokemonSimulator.game;
        if (game.getTerrainState() != targetTerrain) {
            game.setTerrain(Terrain.NORMAL);
            super.use();
        }
    }

    @Override
    public boolean canUse(BattleMon target) {
        return PokemonSimulator.game.getTerrainState() == targetTerrain;
    }
}
