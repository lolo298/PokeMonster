package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Types.Skills.StatusSkillType;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Types;

public class GroundType extends StatusSkillType {

    public GroundType() {
        super(Types.GROUND);
    }


    @Override
    public void useSkill(BattleMon target) {
        target.setStatus(Status.HIDDEN);
    }
}
