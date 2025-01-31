package com.pokemonSimulator.Game.Items;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.Values.enums.ItemTarget;

public class Potion extends Item {
    private final int healAmount;

    public Potion(int healAmount, int quantity, ItemTarget target) {
        super("Potion", quantity, target);
        this.healAmount = healAmount;
    }

    @Override
    public void use(BattleMon target) {
        target.heal(healAmount);
        super.use();
    }

    @Override
    public boolean canUse(BattleMon target) {
        return target.getHealth().compareTo(target.getMaxHealth()) < 0;
    }
}
