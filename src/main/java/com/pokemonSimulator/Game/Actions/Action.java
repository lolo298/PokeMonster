package com.pokemonSimulator.Game.Actions;

import com.pokemonSimulator.Utils.Values.enums.ActionType;
import com.pokemonSimulator.Utils.Values.Integer;

public class Action {
    ActionType actionType;
    Integer basePp;
    Integer pp;

    public Action(ActionType type) {
        this.actionType = type;
    }

    public ActionType getActionType() {
        return actionType;
    }

    protected void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }


    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp.clone();
        this.basePp = pp;
    }

    public void setNbUse(Integer nbUse) {
        this.pp = nbUse.clone();
        this.basePp = nbUse;
    }

    public Integer getBasePp() {
        return basePp;
    }

    public void use() {
        this.pp.decrement();
    }

}
