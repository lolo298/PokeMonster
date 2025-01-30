package com.pokemonSimulator.Game.Types.Skills;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Values.enums.Types;

public abstract class SelfSkillType extends Type {
    public SelfSkillType(Types type) {
        super(type);
    }

    public abstract void useSkill(BattleMon user);
}
