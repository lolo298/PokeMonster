package com.pokemonSimulator.Game.Actions;

import com.pokemonSimulator.Game.Items.Item;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.Values.enums.ActionType;

public class UseItem extends Action{
    private final Item item;
    private final BattleMon target;

    public UseItem(Item item, BattleMon target) {
        super(ActionType.ITEM);
        this.item = item;
        this.target = target;
    }

    public Item getItem() {
        return item;
    }

    public BattleMon getTarget() {
        return target;
    }
}
