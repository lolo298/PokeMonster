package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Errors.BattleStarted;
import com.pokemonSimulator.Utils.Errors.InvalidSprite;
import com.pokemonSimulator.Utils.Errors.TooManyAttacks;
import com.pokemonSimulator.Utils.Values.Interfaces.ISprite;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.Utils.SpriteLoader;
import com.pokemonSimulator.Utils.Values.Buff;
import com.pokemonSimulator.Utils.Values.enums.SpriteOrientation;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.enums.SpritesType;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.Tuple;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;
import javafx.scene.image.Image;

public class BattleMon implements ISprite {
    private boolean locked;
    private final String name;
    private final Type type;

    private final Integer attack;
    private final Integer defense;
    private final Integer health;
    private final Integer maxHealth;
    private final Integer speed;

    private Integer attackBoost = new Integer(0);
    private Integer defenseBoost = new Integer(0);
    private Integer speedBoost = new Integer(0);

    private Status status = Status.NONE;
    private Integer statusDuration = new Integer(0);

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

    private double getBoostMultiplier(Integer boost) {
        if (boost.compareTo(new Integer(0)) >= 0) {
            return (2 + boost.getValue()) / 2.0;
        } else {
            return 2.0 / (2 - boost.getValue());
        }
    }

    public Integer getAttack() {
        return new Integer((int) (attack.getValue() * getBoostMultiplier(attackBoost)));
    }

    public void setAttacks(Attack[] attacks) throws TooManyAttacks, BattleStarted {
        if (locked) {
            throw new BattleStarted("Battle already started");
        }

        if (attacks.length > 4) {
            throw new TooManyAttacks("A monster can only have 4 attacks");
        }

        Attack[] clonedAttacks = new Attack[4];
        for (int i = 0; i < attacks.length; i++) {
            Attack attack = attacks[i];
            if (attack != null) {
                clonedAttacks[i] = attack.clone();
            }
        }

        this.attacks = clonedAttacks;
    }

    public Integer getDefense() {
        return new Integer((int) (defense.getValue() * getBoostMultiplier(defenseBoost)));
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public Integer getSpeed() {
        return new Integer((int) (speed.getValue() * getBoostMultiplier(speedBoost)));
    }

    public void hit(Integer damage) {
        this.health.minusLeft(damage);
    }

    public void heal(Integer amount) {
        this.heal(amount.getValue());
    }

    public void heal(int amount) {
        this.health.plus(amount);
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

    public boolean isFainted() {
        return this.health.compareTo(new Integer(0)) <= 0;
    }

    public void setStatus(Status status) {
        if (status == Status.HIDDEN && this.status != Status.HIDDEN) {
            defenseBoost.plus(2);
        }
        this.status = status;
        this.statusDuration = new Integer(0);
    }

    public void turn() {
        this.statusDuration.plus(1);
        switch (status) {
            case PARALYZED -> {
                double random = Random.generateDouble(0, 1);
                double chance = (double) statusDuration.getValue() / Constants.PARALYZE_DURATION;
                if (random < chance) {
                    status = Status.NONE;
                    statusDuration = new Integer(0);
                }
            }
            case HIDDEN -> {
                double random = Random.generateDouble(0, 1);
                double chance = (double) statusDuration.getValue() / Constants.HIDDEN_DURATION;
                if (random < chance) {
                    status = Status.NONE;
                    defenseBoost.minusLeft(2);
                    statusDuration = new Integer(0);
                }
            }
            case BURNED -> {
                if (PokemonSimulator.game.getTerrainState() == Terrain.FLOOD) {
                    status = Status.NONE;
                    statusDuration = new Integer(0);
                } else {
                    int damage = getAttack().getValue() / 10;
                    hit(new Integer(damage));
                }
            }
        }
    }

    public Status getStatus() {
        return status;
    }

    public Integer getStatusDuration() {
        return statusDuration;
    }

    public boolean canBuff(Buff buff) {
        switch (buff.getStat()) {
            case ATTACK -> {
                return attackBoost.compareTo(new Integer(6)) < 0 && attackBoost.compareTo(new Integer(-6)) > 0;
            }
            case DEFENSE -> {
                return defenseBoost.compareTo(new Integer(6)) < 0 && defenseBoost.compareTo(new Integer(-6)) > 0;
            }
            case SPEED -> {
                return speedBoost.compareTo(new Integer(6)) < 0 && speedBoost.compareTo(new Integer(-6)) > 0;
            }
        }
        return false;
    }

    public void buff(Buff buff) {
        if (!canBuff(buff)) {
            return;
        }
        Logger.warn("Buffing " + this.getName() + " with " + buff.getStat() + " by " + buff.getStage());
        switch (buff.getStat()) {
            case ATTACK -> {
                attackBoost.plus(buff.getStage());
            }
            case DEFENSE -> {
                defenseBoost.plus(buff.getStage());
            }
            case SPEED -> {
                speedBoost.plus(buff.getStage());
            }
        }
    }

    public void clearBuff() {
        attackBoost = new Integer(0);
        defenseBoost = new Integer(0);
        speedBoost = new Integer(0);
    }

    public Image getSprite(SpriteOrientation side, SpritesType type) {
        java.lang.String extension = switch (type) {
            case STATIC -> "png";
            case ANIMATED -> "gif";
        };

        java.lang.String orientation = switch (side) {
            case BACK -> "back";
            case FRONT -> "front";
        };

        Image sprite;

        try {
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/" + this.getName().getValue().toLowerCase() + "_" + orientation + "." + extension);
        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/default.png");
        }

        return sprite;
    }

    public Image getSprite(SpriteOrientation side, SpritesType type, int width, int height) {
        java.lang.String extension = switch (type) {
            case STATIC -> "png";
            case ANIMATED -> "gif";
        };

        java.lang.String orientation = switch (side) {
            case BACK -> "back";
            case FRONT -> "front";
        };

        Image sprite;

        try {
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/" + this.getName().getValue().toLowerCase() + "_" + orientation + "." + extension, width, height, false, false);
        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/default.png", width, height, true, false);
        }

        return sprite;
    }

    public Image getSprite() {
        return getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED);
    }

    public Image getSprite(int width, int height) {
        return getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED, width, height);
    }

    public Tuple<Image, Image> getSprites() {
        var frontImage = getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED);
        var backImage = getSprite(SpriteOrientation.BACK, SpritesType.ANIMATED);

        return new Tuple<>(frontImage, backImage);
    }
}
