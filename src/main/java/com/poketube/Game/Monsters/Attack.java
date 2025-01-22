package com.poketube.Game.Monsters;

import com.poketube.Game.Types.Type;
import com.poketube.Utils.Serializable;
import com.poketube.Utils.Values.Buff;
import com.poketube.Utils.Values.String;
import com.poketube.Utils.Values.Integer;
import com.poketube.Utils.Values.Float;

public class Attack implements Serializable {
    private String name;
    private Type type;
    private Integer pp;
    private Integer power;
    private Float accuracy;

    private Buff buff;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public Integer getPp() { return pp; }
    public void setPp(Integer pp) { this.pp = pp; }
    public void setNbUse(Integer nbUse) { this.pp = nbUse; }
    public Integer getPower() { return power; }
    public void setPower(Integer power) { this.power = power; }
    public Float getAccuracy() { return accuracy; }
    public void setAccuracy(Float accuracy) { this.accuracy = accuracy; }
    public void setFail(Float fail) { this.accuracy = fail.minusRight(1); }
    public void setFail(Integer fail) { this.accuracy = new Float(fail.minusRight(1).getValue()); }
    public Buff getBuff() { return buff; }
    public void setBuff(Buff buff) { this.buff = buff; }

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
