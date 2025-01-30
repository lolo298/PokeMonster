package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Types.Skills.StatusSkillType;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Types;

public class ElectricType extends StatusSkillType {

    public ElectricType() {
        super(Types.ELECTRIC);
    }

    @Override
    public void useSkill(BattleMon target) {
        double random = Random.generateDouble(0, 1);
        if (random < Constants.PARALYZE) {
            target.setStatus(Status.PARALYZED);
        }
    }

}
