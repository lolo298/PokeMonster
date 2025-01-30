package com.pokemonSimulator.Game;

import com.pokemonSimulator.Game.Actions.Action;
import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Actions.SwitchMon;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Types.GroundType;
import com.pokemonSimulator.Game.Types.Skills.SelfSkillType;
import com.pokemonSimulator.Game.Types.Skills.StatusSkillType;
import com.pokemonSimulator.Game.Types.Skills.TerrainSkillType;
import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Game.Types.WaterType;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;

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

    private Terrain terrainState;


    public int getPlayerTurn() {
        return playerTurn;
    }

    private int playerTurn = 1;

    private Action player1Action;
    private Action player2Action;

    private int winner = -1;

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
        this.playerTurn = 1;
        this.activeTeam = this.team1;

        terrainState = Terrain.NORMAL;

        team1.forEach(BattleMon::lock);
        team2.forEach(BattleMon::lock);
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

        if (player1Action instanceof SwitchMon) {
            priority = 1;
        } else if (player2Action instanceof SwitchMon) {
            priority = 2;
        } else {
            //check pokemons speed
            if (player1Mon.getSpeed().compareTo(player2Mon.getSpeed()) > 0) {
                priority = 1;
            } else if (player1Mon.getSpeed().compareTo(player2Mon.getSpeed()) < 0) {
                priority = 2;
            } else {
                priority = (int) Random.generateInt(1, 2);
            }
        }
        Logger.log("Priority to player " + priority);

        //check terrain
        if (terrainState != Terrain.NORMAL) {
            java.util.Random random = new java.util.Random();
            if (random.nextBoolean() || terrainState.getDuration().getValue() == 0) {
                terrainState = Terrain.NORMAL;
            } else {
                terrainState.turn();
            }
        }

        // attacks
        useAction(priority);
        useAction(priority == 1 ? 2 : 1);

        //turn end
        //check healing skills (Grass, ...)
        if (!player1Mon.isFainted() && player1Mon.getType() instanceof SelfSkillType st) {
            st.useSkill(player1Mon);
        }

        if (!player2Mon.isFainted() && player2Mon.getType() instanceof SelfSkillType st) {
            st.useSkill(player2Mon);
        }

        player1Mon.turn();
        player2Mon.turn();

    }

    private void useAction(int player) {
        Logger.warn("Player " + player + " attacking");
        BattleMon attacker;
        BattleMon defender;
        Action action;
        switch (player) {
            case 2: {
                action = player2Action;
                attacker = player2Mon;
                defender = player1Mon;
                break;
            }
            case 1:
            default: {
                action = player1Action;
                attacker = player1Mon;
                defender = player2Mon;
                break;
            }
        }

        if (attacker.isFainted()) {
            Logger.warn("Attacker is fainted");
            return;
        }

        Logger.log("ActionType: " + action.getActionType().name());

        switch (action.getActionType()) {
            case DAMAGE:
                damage(attacker, defender, action);
                break;
            case SWITCH:
                switchMon(player, action);
                break;
            case STRUGGLE:
                struggle(attacker, defender);
                break;
        }


        if (isTeamDefeated(team1)) {
            winner = 2;
        } else if (isTeamDefeated(team2)) {
            winner = 1;
        }
    }

    private boolean attack(double accuracy, double damage, BattleMon attacker, BattleMon defender) {
        Type attackerType = attacker.getType();

        if (attacker.getStatus() == Status.PARALYZED) {
            double random = Random.generateDouble(0, 1);
            if (random > 0.25) {
                Logger.warn("Paralyzed");
                return false;
            }
        }

        if (terrainState == Terrain.FLOOD) {
            if (!(attackerType instanceof WaterType)) {
                double random = Random.generateDouble(0, 1);
                if (random < Constants.SLIP) {
                    Logger.warn("Falling due to flood");
                    attacker.hit(new Integer((int) Math.floor(damage / 4)));
                    return true;
                }
            }
        }

        double random = Random.generateDouble(0, 1);
        if (random < accuracy) {
            Logger.warn("damage " + damage);
            defender.hit(new Integer((int) Math.floor(damage)));
        } else {
            Logger.warn("Attack missed");
        }

        return true;
    }

    private void damage(BattleMon attacker, BattleMon defender, Action action) {
        Attack attack = (Attack) action;
        Type attackerType = attacker.getType();
        Type defenderType = defender.getType();

        Logger.log(attacker + " attacking " + defender + " with " + attack);

        float attackStat = attacker.getAttack().getValue();
        float attackPower = attack.getPower().getValue();
        float defenseStat = defender.getDefense().getValue();
        float avantage = calculateAvantage(attackerType, defenderType);
        double coef = Random.generateDouble(0.85, 1);

        double damage = ((11 * attackStat * attackPower) / (25 * defenseStat) + 2) * avantage * coef;

        double accuracy = attack.getAccuracy().getValue();

        boolean attackUsed = attack(accuracy, damage, attacker, defender);


        if (attackUsed) {
            if (attackerType instanceof TerrainSkillType type) {
                type.useSkill(attacker);
            }

            if (attackerType instanceof StatusSkillType type && attack.getType().equals(type)) {
                type.useSkill(type instanceof GroundType ? attacker : defender);
            }
            attack.use();
        }
    }

    public void switchMon(int player, Action action) {
        SwitchMon switchMon = (SwitchMon) action;

        Logger.warn("Switching mons");
        switch (player) {
            case 2: {
                if (terrainState != Terrain.NORMAL && terrainState.getCreator() == player2Mon) {
                    terrainState = Terrain.NORMAL;
                }

                player2Mon = switchMon.getTarget();
                break;
            }
            case 1:
            default: {
                if (terrainState != Terrain.NORMAL && terrainState.getCreator() == player1Mon) {
                    terrainState = Terrain.NORMAL;
                }

                player1Mon = switchMon.getTarget();
                break;
            }
        }

        reloadActiveMon();
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

    private void struggle(BattleMon attacker, BattleMon defender) {
        Logger.warn("Struggling");
        double attackStat = attacker.getAttack().getValue();
        double defenseStat = defender.getDefense().getValue();
        double coef = Random.generateDouble(.85, 1);
        double damage = 20 * (attackStat / defenseStat) * coef;
        attack(1, damage, attacker, defender);

    }

    private boolean isTeamDefeated(LinkedList<BattleMon> team) {
        return team.stream().allMatch(BattleMon::isFainted);
    }

    public LinkedList<BattleMon> getActiveTeam() {
        return this.activeTeam;
    }

    public LinkedList<BattleMon> getEnemyTeam() {
        return this.enemyTeam;
    }

    private void reloadActiveMon() {
        int turn = this.playerTurn;

        if (turn == 1) {
            this.activeMon = this.player1Mon;
            this.enemyMon = this.player2Mon;
        } else {
            this.activeMon = this.player2Mon;
            this.enemyMon = this.player1Mon;
        }
    }

    public void setTurn(int turn) {
        this.playerTurn = turn;
        reloadActiveMon();
    }

    public BattleMon getPlayer1Mon() {
        return player1Mon;
    }

    public BattleMon getPlayer2Mon() {
        return player2Mon;
    }

    public BattleMon getActiveMon() {
        return this.activeMon;
    }

    public BattleMon getEnemyMon() {
        return this.enemyMon;
    }

    public void setActivePlayerAction(Action action) {
        if (playerTurn == 1) {
            this.player1Action = action;
        } else {
            this.player2Action = action;
        }
    }

    public int getWinner() {
        return winner;
    }

    public boolean isOver() {
        return winner != -1;
    }

    public void setTerrain(Terrain terrain) {
        this.terrainState = terrain;
    }

    public Terrain getTerrainState() {
        return terrainState;
    }

    public LinkedList<BattleMon> getWinnerTeam() {
        if (!isOver()) {
            return null;
        }
        return winner == 1 ? team1 : team2;
    }
}
