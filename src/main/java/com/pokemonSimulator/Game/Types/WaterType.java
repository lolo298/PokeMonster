package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.Game;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Values.enums.Terrain;
import com.pokemonSimulator.Game.Types.Skills.TerrainSkillType;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.Utils.Values.enums.Types;

public class WaterType extends TerrainSkillType {

    public WaterType() {
        super(Types.WATER);
    }

    @Override
    public void useSkill(BattleMon creator) {
        Game game = PokemonSimulator.game;

        double random = Random.generateDouble(0, 1);
        if (random < Constants.FLOOD) {
            Terrain terrain = Terrain.FLOOD;
            terrain.apply(creator);
            game.setTerrain(terrain);
        }
    }
}
