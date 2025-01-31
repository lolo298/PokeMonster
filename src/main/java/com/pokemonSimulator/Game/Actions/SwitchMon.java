package com.pokemonSimulator.Game.Actions;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.Values.enums.ActionType;

public class SwitchMon extends Action {
    private final BattleMon target;

    public SwitchMon(BattleMon target) {
        super(ActionType.SWITCH);
        this.target = target;
    }

    public BattleMon getTarget() {
        return target;
    }
}
