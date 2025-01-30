package com.pokemonSimulator.Game.Actions;

import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Serializable;
import com.pokemonSimulator.Utils.Values.Float;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.*;
import com.pokemonSimulator.Utils.Values.enums.ActionType;

public class Attack extends Action implements Serializable, Cloneable {
    private String name;
    private Type type;
    private Integer power;
    private Float accuracy;
    private Buff buff;

    public Attack() {
        super(ActionType.DAMAGE);
    }

    public Attack(ActionType type) {
        super(type);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public void setFail(Float fail) {
        this.accuracy = fail.minusRight(1);
    }

    public void setFail(Integer fail) {
        this.accuracy = new Float(fail.minusRight(1).getValue());
    }

    public Buff getBuff() {
        return buff;
    }

    public void setBuff(Buff buff) {
        this.buff = buff;
        this.setActionType(buff != null ? ActionType.BUFF : ActionType.DAMAGE);
    }

    @Override
    public java.lang.String toString() {
        return this.getName().getValue();
    }

    public java.lang.String serialize() {
        return "name: " + this + "\n" +
                "type: " + this.getType() + "\n" +
                "pp: " + this.pp.serialize() + "\n" +
                "power: " + this.power.serialize() + "\n" +
                "accuracy: " + this.accuracy.serialize() + "\n";
    }

    @Override
    public Attack clone() {
        Attack attack = new Attack(this.actionType);
        attack.setName(this.name);
        attack.setType(this.type);
        attack.setPower(this.power);
        attack.setAccuracy(this.accuracy);
        attack.setBuff(this.buff != null ? this.buff : null);
        attack.setPp(this.getPp());
        return attack;
    }

}
