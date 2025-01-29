package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Serializable;
import com.pokemonSimulator.Utils.Values.Float;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.*;

public class Attack implements Serializable {
    private AttackType attackType = AttackType.DAMAGE;
    private String name;
    private Type type;
    private Integer pp;
    private Integer basePp;
    private Integer power;
    private Float accuracy;

    private Buff buff;

    public Attack() {}
    public Attack(AttackType type) {
        this.attackType = type;
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

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
        this.basePp = pp;
    }

    public void setNbUse(Integer nbUse) {
        this.pp = nbUse;
        this.basePp = nbUse;
    }

    public Integer getBasePp() {
        return basePp;
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
        this.attackType = buff != null ? AttackType.BUFF : AttackType.DAMAGE;
    }

    public void use() {
        this.pp.decrement();
    }

    public AttackType getAttackType() {
        return attackType;
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

    public Attack clone() throws CloneNotSupportedException {
        return (Attack) super.clone();
    }

}
