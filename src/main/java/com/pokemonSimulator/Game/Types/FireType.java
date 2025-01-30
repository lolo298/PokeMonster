package com.pokemonSimulator.Game.Types;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Types.Skills.StatusSkillType;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Types;

public class FireType extends StatusSkillType {

    public FireType() {
        super(Types.FIRE);
    }


    @Override
    public void useSkill(BattleMon target) {
        target.setStatus(Status.BURNED);
    }
}
