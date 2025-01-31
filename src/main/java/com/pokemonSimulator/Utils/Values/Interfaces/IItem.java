package com.pokemonSimulator.Utils.Values.Interfaces;

import com.pokemonSimulator.Game.Monsters.BattleMon;

public interface IItem {
    public void use(BattleMon target);
    public boolean canUse(BattleMon target);
}
