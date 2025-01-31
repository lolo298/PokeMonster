package com.pokemonSimulator.Game;

import com.pokemonSimulator.Game.Actions.Action;
import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Actions.SwitchMon;
import com.pokemonSimulator.Game.Actions.UseItem;
import com.pokemonSimulator.Game.Items.Item;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Types.GroundType;
import com.pokemonSimulator.Game.Types.Skills.SelfSkillType;
import com.pokemonSimulator.Game.Types.Skills.StatusSkillType;
import com.pokemonSimulator.Game.Types.Skills.TerrainSkillType;
import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Game.Types.WaterType;
import com.pokemonSimulator.Utils.BattleLogger;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Random;
import com.pokemonSimulator.Utils.Values.Buff;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;

import java.util.LinkedList;
import java.util.List;

public class Game {
    private final BattleLogger battleLogger;

    private final List<Item> team1items;
    private final List<Item> team2items;

    private final LinkedList<BattleMon> team1;
    private final LinkedList<BattleMon> team2;
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

    public Game(LinkedList<BattleMon> team1, LinkedList<BattleMon> team2, List<Item> items) {
        this.team1items = items.stream().map(Item::clone).toList();
        this.team2items = items.stream().map(Item::clone).toList();

        battleLogger = new BattleLogger();

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
        battleLogger.playerTurnLog(playerTurn);

        terrainState = Terrain.NORMAL;

        team1.forEach(BattleMon::lock);
        team2.forEach(BattleMon::lock);
    }

    public void nextPlayer() {
        this.playerTurn = this.playerTurn == 1 ? 2 : 1;
        battleLogger.playerTurnLog(playerTurn);

        reloadActiveMon();
    }

    public void battle() {
        //first player to attack
        int priority;

        if (player1Action instanceof SwitchMon) {
            priority = 1;
        } else if (player2Action instanceof SwitchMon) {
            priority = 2;
        } else if (player1Action instanceof UseItem) {
            priority = 1;
        } else if (player2Action instanceof UseItem) {
            priority = 2;
        } else {
            //check pokemons speed
            if (player1Mon.getSpeed().compareTo(player2Mon.getSpeed()) > 0) {
                priority = 1;
            } else if (player1Mon.getSpeed().compareTo(player2Mon.getSpeed()) < 0) {
                priority = 2;
            } else {
                priority = Random.generateInt(1, 2);
            }
        }

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
            case BUFF:
                buff(attacker, defender, action);
                break;
            case ITEM:
                useItem(action);
                break;
        }

        if (action instanceof Attack attack) {
            attack.use();
        }


        if (isTeamDefeated(team1)) {
            winner = 2;
        } else if (isTeamDefeated(team2)) {
            winner = 1;
        }
    }

    private boolean attack(double accuracy, int damage, BattleMon attacker, BattleMon defender) {
        Type attackerType = attacker.getType();

        if (attacker.getStatus() == Status.PARALYZED) {
            double random = Random.generateDouble(0, 1);
            if (random > 0.25) {
                battleLogger.missLog(attacker, Status.PARALYZED);
                return false;
            }
        }

        if (terrainState == Terrain.FLOOD) {
            if (!(attackerType instanceof WaterType)) {
                double random = Random.generateDouble(0, 1);
                if (random < Constants.SLIP) {
                    battleLogger.missLog(attacker, Terrain.FLOOD);
                    attacker.hit(new Integer(damage / 4));
                    return false;
                }
            }
        }

        double random = Random.generateDouble(0, 1);
        if (random < accuracy) {
            defender.hit(new Integer(damage));
        } else {
            battleLogger.missLog(attacker);
        }

        return true;
    }

    private void damage(BattleMon attacker, BattleMon defender, Action action) {
        Attack attack = (Attack) action;
        Type attackerType = attacker.getType();
        Type defenderType = defender.getType();

        battleLogger.attackLog(attacker, defender, attack);

        float attackStat = attacker.getAttack().getValue();
        float attackPower = attack.getPower().getValue();

        float defenseStat = defender.getDefense().getValue();
        float avantage = calculateAvantage(attackerType, defenderType);
        double coef = Random.generateDouble(0.85, 1);

        int damage = (int) Math.floor(((11 * attackStat * attackPower) / (25 * defenseStat) + 2) * avantage * coef);

        double accuracy = attack.getAccuracy().getValue();

        boolean attackUsed = attack(accuracy, damage, attacker, defender);


        if (attackUsed) {
            battleLogger.effectiveLog(avantage);
            if (attackerType instanceof TerrainSkillType type) {
                type.useSkill(attacker);
            }

            if (attackerType instanceof StatusSkillType type && attack.getType().equals(type)) {
                type.useSkill(type instanceof GroundType ? attacker : defender);
            }

            if (attack.hasBuff()) {
                buff(attacker, defender, action);
            }

            if (defender.isFainted()) {
                battleLogger.faintedLog(defender);
            }
        }
    }

    public void switchMon(int player, Action action) {
        SwitchMon switchMon = (SwitchMon) action;

        switch (player) {
            case 2: {
                if (terrainState != Terrain.NORMAL && terrainState.getCreator() == player2Mon) {
                    terrainState = Terrain.NORMAL;
                }
                player2Mon.clearBuff();
                player2Mon = switchMon.getTarget();
                battleLogger.sendMonOut(player2Mon);
                break;
            }
            case 1:
            default: {
                if (terrainState != Terrain.NORMAL && terrainState.getCreator() == player1Mon) {
                    terrainState = Terrain.NORMAL;
                }
                player1Mon.clearBuff();
                player1Mon = switchMon.getTarget();
                battleLogger.sendMonOut(player1Mon);
                break;
            }
        }

        reloadActiveMon();
    }

    public void useItem(Action action) {
        UseItem useItem = (UseItem) action;
        Item item = useItem.getItem();
        BattleMon target = useItem.getTarget();

        item.use(target);
        battleLogger.useItemLog(target, item, playerTurn);
    }

    private float calculateAvantage(Type attakerType, Type defenderType) {
        if (attakerType.getStrengths().contains(defenderType.getType())) {
            return 2;
        }

        if (attakerType.getWeaknesses().contains(defenderType.getType())) {
            return 0.5f;
        }

        return 1;
    }

    private void struggle(BattleMon attacker, BattleMon defender) {
        battleLogger.struggleLog(attacker);

        double attackStat = attacker.getAttack().getValue();
        double defenseStat = defender.getDefense().getValue();
        double coef = Random.generateDouble(.85, 1);
        int damage = (int) Math.floor(20 * (attackStat / defenseStat) * coef);
        attack(1, damage, attacker, defender);
    }

    private void buff(BattleMon attacker, BattleMon defender, Action action) {
        Attack attack = (Attack) action;
        Buff buff = attack.getBuff();

        battleLogger.buffLog(attacker, defender, buff);

        switch (buff.getTarget()) {
            case SELF -> attacker.buff(buff);
            case ENEMY -> defender.buff(buff);
        }
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
            this.activeTeam = this.team1;
            this.enemyTeam = this.team2;
        } else {
            this.activeMon = this.player2Mon;
            this.enemyMon = this.player1Mon;
            this.activeTeam = this.team2;
            this.enemyTeam = this.team1;
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

    public List<Item> getActiveItems() {
        return playerTurn == 1 ? team1items : team2items;
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
        battleLogger.terrainLog(terrain);
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

    public BattleLogger getBattleLogger() {
        return battleLogger;
    }
}
