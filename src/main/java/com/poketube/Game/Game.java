package com.poketube.Game;

import com.poketube.Game.Monsters.Attack;
import com.poketube.Game.Monsters.BattleMon;
import com.poketube.Game.Types.Type;
import com.poketube.Utils.Logger;
import com.poketube.Utils.Random;
import com.poketube.Utils.Values.AttackType;
import com.poketube.Utils.Values.Integer;

import java.util.LinkedList;

public class Game {

    private LinkedList<BattleMon> team1;
    private LinkedList<BattleMon> team2;
    private BattleMon player1Mon;
    private BattleMon player2Mon;


    private LinkedList<BattleMon> activeTeam;
    private LinkedList<BattleMon> enemyTeam;
    private BattleMon activeMon;
    private BattleMon enemyMon;


    private static final int TEAM_SIZE = 4;
    private static final int ATTACK_PER_MON = 4;

    public int getPlayerTurn() {
        return playerTurn;
    }

    private int playerTurn = 1;

    private Attack player1Attack;
    private Attack player2Attack;

    public Game(LinkedList<BattleMon> team1, LinkedList<BattleMon> team2) {
        //team values
        this.team1 = team1;
        this.team2 = team2;
        this.player1Mon = team1.getFirst();
        this.player2Mon = team2.getFirst();

        //per-turn values
        this.activeMon = this.player1Mon;
        this.enemyMon = this.player2Mon;
        this.activeTeam = this.team1;
        this.enemyTeam = this.team2;
    }

    public LinkedList<BattleMon> getTeam1() {
        return team1;
    }

    public LinkedList<BattleMon> getTeam2() {
        return team2;
    }

    public void start() {
        System.out.println("Game started");
        this.playerTurn = 1;
        this.activeTeam = this.team1;

        team1.forEach(BattleMon::lock);
        team2.forEach(BattleMon::lock);
    }

    public void end() {
        System.out.println("Game ended");
    }

    public void nextPlayer() {
        this.playerTurn = this.playerTurn == 1 ? 2 : 1;

        //switch active and enemy team
        this.activeTeam = this.playerTurn == 1 ? this.team1 : this.team2;
        this.enemyTeam = this.playerTurn == 1 ? this.team2 : this.team1;

        //switch active and enemy mon
        var tmp = this.activeMon;
        this.activeMon = this.enemyMon;
        this.enemyMon = tmp;
    }

    public void battle() {
        Logger.log("Fighting");
        //first player to attack
        int priority;

        //check pokemons speed
        if (player1Mon.getSpeed().compareTo(player2Mon.getSpeed()) > 0) {
            priority = 1;
        } else if (player1Mon.getSpeed().compareTo(player2Mon.getSpeed()) < 0) {
            priority = 2;
        } else {
            priority = (int) Random.generateValue(1, 2);
        }

        Logger.log("Priority to player " + priority);


        useAttack(priority);
        useAttack(priority == 1 ? 2 : 1);


    }

    private void useAttack(int player) {
        Logger.warn("Player " + player + " attacking");
        BattleMon attacker;
        BattleMon defender;
        Attack attack;
        switch (player) {
            case 2: {
                attack = player2Attack;
                attacker = player2Mon;
                defender = player1Mon;
                break;
            }
            case 1:
            default: {
                attack = player1Attack;
                attacker = player1Mon;
                defender = player2Mon;
                break;
            }
        };

        Logger.log("AttackType: " + attack.getAttackType().name());
        if (attack.getAttackType() == AttackType.DAMAGE) {

            Type attackerType = attacker.getType();
            Type defenderType = defender.getType();

            float attackStat = attacker.getAttack().getValue();
            float attackPower = attack.getPower().getValue();
            float defenseStat = defender.getDefense().getValue();
            float avantage = calculateAvantage(attackerType, defenderType);
            double coef = Random.generateValue(0.85, 1);

            System.out.printf("attackStat: %f%n", attackStat);
            System.out.printf("attackPower: %f%n", attackPower);
            System.out.printf("defenseStat: %f%n", defenseStat);
            System.out.printf("avantage: %f%n", avantage);
            System.out.printf("coef: %f%n", coef);


            double damage = ((11 * attackStat * attackPower)/(25 * defenseStat) + 2) * avantage * coef;
            Logger.warn("damage " + damage);

            defender.hit(new Integer((int)Math.floor(damage)));
        }

        attack.use();
    }

    private float calculateAvantage(Type attakerType, Type defenderType) {
        if (attakerType.getStrengths().contains(defenderType.getType())) {
            Logger.log(String.format("%s weak to %s", defenderType.getType(), attakerType.getType()));
            return 2;
        }

        if (attakerType.getWeaknesses().contains(defenderType.getType())) {
            return 0.5f;
        }

        return 1;
    }

    public LinkedList<BattleMon> getActiveTeam() {
        return this.activeTeam;
    }

    public LinkedList<BattleMon> getEnemyTeam() {
        return this.enemyTeam;
    }

    public BattleMon getActiveMon() {
        return this.activeMon;
    }

    public BattleMon getEnemyMon() {
        return this.enemyMon;
    }

    public void setActivePlayerAttack(Attack attack) {
        if (playerTurn == 1) {
            this.player1Attack = attack;
        } else {
            this.player2Attack = attack;
        }
    }
}
