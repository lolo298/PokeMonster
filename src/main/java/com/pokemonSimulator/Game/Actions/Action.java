package com.pokemonSimulator.Game.Actions;

import com.pokemonSimulator.Utils.Values.ActionType;

public class Action {
    ActionType actionType;

    public Action(ActionType type) {
        this.actionType = type;
    }

    public ActionType getActionType() {
        return actionType;
    }

    protected void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }


}
