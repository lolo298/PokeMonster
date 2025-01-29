package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Errors.BattleStarted;
import com.pokemonSimulator.Utils.Errors.InvalidSprite;
import com.pokemonSimulator.Utils.Errors.TooManyAttacks;
import com.pokemonSimulator.Utils.ISprite;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.SpriteLoader;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.SpritesType;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.Tuple;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.net.URISyntaxException;

public class BattleMon implements ISprite {
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

    public Image getSprite(java.lang.String side, SpritesType type) {
        Logger.log("Getting sprite for " + this.getName());

        java.lang.String extension = switch (type) {
            case STATIC -> "png";
            case ANIMATED -> "gif";
        };

        Image sprite = null;

        try {
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/" + this.getName().getValue().toLowerCase() + "_" + side + "." + extension);
        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/default.png");
        }

        return sprite;
    }

    public Image getSprite(java.lang.String side, SpritesType type, int width, int height) {
        Logger.log("Getting sprite for " + this.getName());

        java.lang.String extension = switch (type) {
            case STATIC -> "png";
            case ANIMATED -> "gif";
        };

        Image sprite = null;

        try {
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/" + this.getName().getValue().toLowerCase() + "_" + side + "." + extension, width, height, false, false);
        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/default.png", width, height, true, false);
        }

        return sprite;
    }

    public Image getSprite() {
        return getSprite("front", SpritesType.ANIMATED);
    }

    public Image getSprite(int width, int height) {
        return getSprite("front", SpritesType.ANIMATED, width, height);
    }

    public Tuple<Image, Image> getSprites() throws URISyntaxException {
        var frontImage = getSprite("front", SpritesType.ANIMATED);
        var backImage = getSprite("back", SpritesType.ANIMATED);

        return new Tuple<>(frontImage, backImage);
    }
}
