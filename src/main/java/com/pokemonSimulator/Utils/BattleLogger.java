package com.pokemonSimulator.Utils;

import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Items.Item;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.Values.Buff;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class BattleLogger {

//    private LinkedList<String> logs = new LinkedList<>();
    private final SimpleListProperty<String> logs = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));

    public void log(String message) {
        logs.add(message);
    }

    public void playerTurnLog(int player) {
        logs.add("Player " + player + "'s turn!");
    }

    public void attackLog(BattleMon attacker, BattleMon target, Attack move) {
        logs.add(attacker + " used " + move + "!");
    }

    public void struggleLog(BattleMon attacker) {
        logs.add(attacker + " used Struggle!");
    }

    public void damageLog(BattleMon target, int damage) {
        logs.add(target + " took " + damage + " damage!");
    }

    public void damageLog(BattleMon target, int damage, Status status) {
        logs.add(target + " took " + damage + " damage due to " + status + "!");
    }

    public void faintedLog(BattleMon target) {
        logs.add(target + " fainted!");
    }

    public void effectiveLog(float multiplier) {
        if (multiplier == 0.0f) {
            logs.add("It had no effect!");
            return;
        }

        if (multiplier > 1.0f) {
            logs.add("It's super effective!");
            return;
        }

        if (multiplier < 1.0f) {
            logs.add("It's not very effective...");
        }
    }

    public void missLog(BattleMon target) {
        logs.add(target + " missed!");
    }

    public void missLog(BattleMon target, Terrain terrain) {
        logs.add(target + " missed because of the " + terrain + " terrain!");
    }

    public void missLog(BattleMon target, Status status) {
        if (status != Status.NONE) {
            logs.add(target + " is " + status + " and cannot move!");
        }
    }

    public void healLog(BattleMon target, int healAmount) {
        logs.add(target + " healed for " + healAmount + " HP!");
    }

    public void statusLog(BattleMon target, Status status) {
        logs.add(target + " is now " + status + "!");
    }

    public void removeStatusLog(BattleMon target, Status status) {
        logs.add(target + " is no longer " + status + "!");
    }

    public void terrainLog(Terrain terrain) {
        if (terrain == Terrain.NORMAL) {
            logs.add("The terrain has returned to normal!");
            return;
        }
        logs.add("The terrain is now " + terrain + "!");
    }

    public void buffLog(BattleMon attacker, BattleMon defender, Buff buff) {
        BattleMon target = switch (buff.getTarget()) {
            case SELF -> attacker;
            case ENEMY -> defender;
        };

        if (buff.getStage().getValue() < 0) {
            logs.add(target + " has decreased " + buff.getStat() + " by " + Math.abs(buff.getStage().getValue()) + " stages!");
            return;
        }
        logs.add(target + " has increased " + buff.getStat() + " by " + buff.getStage() + " stages!");
    }

    public void canBuffLog(boolean canBuff, BattleMon target, Buff buff) {
        if (canBuff) {
            return;
        }
        if (buff.getStage().getValue() < 0) {
            logs.add(target + " cannot decrease " + buff.getStat() + " any further!");
            return;
        }

        logs.add(target + " cannot increase " + buff.getStat() + " any further!");
    }

    public void sendMonOut(BattleMon mon) {
        logs.add(mon + " has been sent out!");
    }

    public void cantSwitchLog(boolean onField, boolean fainted) {
        if (fainted) {
            logs.add("That Pokemon has fainted and cannot be sent out!");
        }
        if (onField) {
            logs.add("The Pokemon is already on the field!");
            return;
        }
        logs.add("You cannot send out that Pokemon!");
    }

    public void cantUseItemLog(Item item) {
        logs.add("That would not have any effect!");
    }

    public void useItemLog(BattleMon target, Item item, int player) {
        logs.add("Player " + player + " used " + item + " on " + target + "!");
    }

    public void noPpLog(Attack move) {
        logs.add("There is no PP left for " + move + "!");
    }

    public SimpleListProperty<String> getLogs() {
        return logs;
    }
}
