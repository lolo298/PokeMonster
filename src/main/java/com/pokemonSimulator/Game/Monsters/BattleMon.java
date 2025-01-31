package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.BattleLogger;
import com.pokemonSimulator.Utils.Errors.BattleStarted;
import com.pokemonSimulator.Utils.Errors.TooManyAttacks;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.Utils.Values.Buff;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;

public class BattleMon extends MonsterSprite {
    private boolean locked;
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

    private BattleLogger getBattleLogger() {
        return PokemonSimulator.game.getBattleLogger();
    }

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
        BattleLogger battleLogger = getBattleLogger();
        battleLogger.damageLog(this, damage.getValue());
        this.health.minusLeft(damage);
    }

    public void heal(Integer amount) {
        this.heal(amount.getValue());
    }

    public void heal(int amount) {
        BattleLogger battleLogger = getBattleLogger();
        battleLogger.healLog(this, amount);
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
        BattleLogger battleLogger = getBattleLogger();
        battleLogger.statusLog(this, status);
        if (status == Status.HIDDEN && this.status != Status.HIDDEN) {
            defenseBoost.plus(2);
        }
        this.status = status;
        this.statusDuration = new Integer(0);
    }

    public void turn() {
        BattleLogger battleLogger = getBattleLogger();
        this.statusDuration.plus(1);
        switch (status) {
            case PARALYZED -> {
                double random = Random.generateDouble(0, 1);
                double chance = (double) statusDuration.getValue() / Constants.PARALYZE_DURATION;
                if (random < chance) {
                    status = Status.NONE;
                    statusDuration = new Integer(0);
                    battleLogger.removeStatusLog(this, Status.PARALYZED);
                }
            }
            case HIDDEN -> {
                double random = Random.generateDouble(0, 1);
                double chance = (double) statusDuration.getValue() / Constants.HIDDEN_DURATION;
                if (random < chance) {
                    status = Status.NONE;
                    defenseBoost.minusLeft(2);
                    statusDuration = new Integer(0);
                    battleLogger.removeStatusLog(this, Status.HIDDEN);
                }
            }
            case BURNED -> {
                if (PokemonSimulator.game.getTerrainState() == Terrain.FLOOD) {
                    status = Status.NONE;
                    statusDuration = new Integer(0);
                    battleLogger.removeStatusLog(this, Status.BURNED);
                } else {
                    int damage = getAttack().getValue() / 10;
                    battleLogger.damageLog(this, damage, Status.BURNED);
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
        BattleLogger battleLogger = getBattleLogger();

        switch (buff.getStat()) {
            case ATTACK -> {
                boolean canBuff = attackBoost.compareTo(new Integer(6)) < 0 && attackBoost.compareTo(new Integer(-6)) > 0;
                battleLogger.canBuffLog(canBuff, this, buff);
                return canBuff;
            }
            case DEFENSE -> {
                boolean canBuff = defenseBoost.compareTo(new Integer(6)) < 0 && defenseBoost.compareTo(new Integer(-6)) > 0;
                battleLogger.canBuffLog(canBuff, this, buff);
                return canBuff;
            }
            case SPEED -> {
                boolean canBuff = speedBoost.compareTo(new Integer(6)) < 0 && speedBoost.compareTo(new Integer(-6)) > 0;
                battleLogger.canBuffLog(canBuff, this, buff);
                return canBuff;
            }
        }
        return false;
    }

    public void buff(Buff buff) {
        if (!canBuff(buff)) {
            return;
        }
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

}
