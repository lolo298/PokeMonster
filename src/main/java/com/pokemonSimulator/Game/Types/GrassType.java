package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Game.Types.Skills.SelfSkillType;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.enums.Terrain;
import com.pokemonSimulator.Utils.Values.enums.Types;

public class GrassType extends SelfSkillType {

    public GrassType() {
        super(Types.GRASS);
    }

    @Override
    public void useSkill(BattleMon user) {
        if (PokemonSimulator.game.getTerrainState() == Terrain.FLOOD) {
            user.heal(new Integer((int) Math.floor((double) user.getMaxHealth().getValue() / 20)));
        }

    }
}
