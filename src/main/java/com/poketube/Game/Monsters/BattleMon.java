package com.poketube.Game.Monsters;

import com.poketube.Game.Types.Type;
import com.poketube.Utils.Errors.BattleStarted;
import com.poketube.Utils.Errors.TooManyAttacks;
import com.poketube.Utils.Logger;
import com.poketube.Utils.Values.Integer;
import com.poketube.Utils.Values.String;
import com.poketube.Utils.Values.Tuple;
import javafx.scene.image.Image;

import java.net.URISyntaxException;

public class BattleMon {
    private boolean locked;
    private final String name;
    private final Type type;

    private final Integer attack;
    private final Integer defense;
    private Integer health;
    private final Integer maxHealth;
    private final Integer speed;

    private Attack[] attacks = new Attack[4];

    public BattleMon(String name, Type type, Integer attack, Integer defense, Integer health, Integer speed) {
        this.speed = speed;
        this.health = health.clone();
        this.maxHealth = health;
        this.defense = defense;
        this.attack = attack;
        this.type = type;
        this.name = name;
    }

    public BattleMon(Monster monster) {
        this(
                monster.getName(),
                monster.getType(),
                monster.getAttack().generateValue(),
                monster.getDefense().generateValue(),
                monster.getHealth().generateValue(),
                monster.getSpeed().generateValue()
        );
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttacks(Attack[] attacks) throws TooManyAttacks, BattleStarted {
        if (locked) {
            throw new BattleStarted("Battle already started");
        }

        if (attacks.length > 4) {
            throw new TooManyAttacks("A monster can only have 4 attacks");
        }

        this.attacks = attacks;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void hit(Integer damage) {
        this.health.minusLeft(damage);
    }

    public void lock() {
        this.locked = true;
    }

    @Override
    public java.lang.String toString() {
        return this.getName().toString();
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public Tuple<Image, Image> getSprite() throws URISyntaxException {
        Logger.log("Getting sprite for " + this.getName());

        // Get sprite from ressource com.poketube.Sprites
        java.net.URL front = this.getClass().getResource("/com/poketube/Sprites/" + this.getName().getValue().toLowerCase() + "_front.gif");
        java.net.URL back = this.getClass().getResource("/com/poketube/Sprites/" + this.getName().getValue().toLowerCase() + "_back.gif");

        if (front == null) {
            front = this.getClass().getResource("/com/poketube/Sprites/default.png");
            assert front != null;
        }

        if (back == null) {
            back = this.getClass().getResource("/com/poketube/Sprites/default.png");
            assert back != null;
        }

        var frontImage = new Image(front.toURI().toString(), 38, 38, true, false);
        var backImage = new Image(back.toURI().toString(), 38, 38, true, false);

        return new Tuple<>(frontImage, backImage);
    }
}
