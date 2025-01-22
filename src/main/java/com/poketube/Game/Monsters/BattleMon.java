package com.poketube.Game.Monsters;

import com.poketube.Game.Types.Type;
import com.poketube.Utils.Errors.TooManyAttacks;
import com.poketube.Utils.Logger;
import com.poketube.Utils.Values.String;
import com.poketube.Utils.Values.Integer;

public class BattleMon {
    private String name;
    private Type type;

    private Integer attack;
    private Integer defense;
    private Integer health;
    private Integer speed;

    private Attack[] attacks = new Attack[4];


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
    public Integer getAttack() {
        return attack;
    }
    public void setAttack(Integer attack) {
        this.attack = attack;
    }
    public Integer getDefense() {
        return defense;
    }
    public void setDefense(Integer defense) {
        this.defense = defense;
    }
    public Integer getHealth() {
        return health;
    }
    public void setHealth(Integer health) {
        this.health = health;
    }
    public Integer getSpeed() { return speed; }
    public void setSpeed(Integer speed) { this.speed = speed; }

    @Override
    public java.lang.String toString() {
        return this.getName().toString();
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public void setAttacks(Attack[] attacks) throws TooManyAttacks {
        if (attacks.length > 4) {
            throw new TooManyAttacks("A monster can only have 4 attacks");
        }

        this.attacks = attacks;
    }
}
