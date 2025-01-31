package com.pokemonSimulator.Game.Items;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Values.Buff;
import com.pokemonSimulator.Utils.Values.enums.BuffStat;
import com.pokemonSimulator.Utils.Values.enums.ItemTarget;
import com.pokemonSimulator.Utils.Values.enums.Targets;
import com.pokemonSimulator.Utils.Values.Integer;

public class StatBoost extends Item {
    private final Buff buff;

    public StatBoost(java.lang.String name, BuffStat stat, int stageBoost, int quantity, ItemTarget target) {
        super(name, quantity, target);
        this.buff = new Buff(stat, new Integer(stageBoost), Targets.SELF);
    }

    @Override
    public void use(BattleMon target) {
        Logger.log("Using " + name + " on " + target.getName());
        target.buff(buff);
        super.use();
    }

    @Override
    public boolean canUse(BattleMon target) {
        return target.canBuff(buff);
    }
}
