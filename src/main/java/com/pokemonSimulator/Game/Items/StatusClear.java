package com.pokemonSimulator.Game.Items;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.BattleLogger;
import com.pokemonSimulator.Utils.Values.Buff;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.enums.BuffStat;
import com.pokemonSimulator.Utils.Values.enums.ItemTarget;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Targets;

public class StatusClear extends Item {
    private final Status status;

    public StatusClear(java.lang.String name, Status status, int quantity, ItemTarget target) {
        super(name, quantity, target);
        this.status = status;
    }

    @Override
    public void use(BattleMon target) {
        if (target.getStatus() == status || status == Status.ALL) {
            BattleLogger logger = PokemonSimulator.game.getBattleLogger();
            logger.removeStatusLog(target, target.getStatus());
            target.setStatus(Status.NONE);
            super.use();
        }
    }

    @Override
    public boolean canUse(BattleMon target) {
        return target.getStatus() == status || (target.getStatus() != Status.NONE && status == Status.ALL);
    }
}
