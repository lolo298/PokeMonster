package com.pokemonSimulator.Game;

import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Items.*;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Monsters.Monster;
import com.pokemonSimulator.Utils.ConfigLoader;
import com.pokemonSimulator.Utils.Deserializer;
import com.pokemonSimulator.Utils.Errors.InvalidDataError;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Serializer;
import com.pokemonSimulator.Utils.Values.enums.BuffStat;
import com.pokemonSimulator.Utils.Values.enums.ItemTarget;
import com.pokemonSimulator.Utils.Values.enums.Status;
import com.pokemonSimulator.Utils.Values.enums.Terrain;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PokemonSimulator {
    private Serializer serializer;
    private Deserializer deserializer;

    private List<Monster> monsters;
    private List<Attack> attacks;

    public static PokemonSimulator instance;

    private static final LinkedList<BattleMon> team1 = new LinkedList<>();
    private static final LinkedList<BattleMon> team2 = new LinkedList<>();
    private static final ArrayList<Item> items = new ArrayList<>();

    public static Game game;

    public PokemonSimulator() {
        if (instance != null) {
            throw new IllegalStateException("PokemonSimulator already initialized");
        }
    }

    public static PokemonSimulator getInstance() {
        if (instance == null) {
            instance = new PokemonSimulator();
        }
        return instance;
    }

    public void start() {
        this.Init();
    }


    private void Init() {
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();

        this.LoadMonsters();
        this.LoadAttacks();


        //load items
        items.add(new Potion(20, 5, ItemTarget.ANY));
        items.add(new StatBoost("X Attack", BuffStat.ATTACK, 1, 5, ItemTarget.ACTIVE));
        items.add(new StatBoost("X Defense", BuffStat.DEFENSE, 1, 5, ItemTarget.ACTIVE));
        items.add(new StatBoost("X Speed", BuffStat.SPEED, 1, 5, ItemTarget.ACTIVE));
        items.add(new StatusClear("Full Heal", Status.ALL, 1, ItemTarget.ANY));
        items.add(new StatusClear("Paralyze Heal", Status.PARALYZED, 1, ItemTarget.ANY));
        items.add(new StatusClear("Burn Heal", Status.BURNED, 1, ItemTarget.ANY));
        items.add(new TerrainClear("Flood Clear", Terrain.FLOOD, 1, ItemTarget.ACTIVE));


        Logger.log("Welcome to PokemonSimulator!");
    }

    private void LoadMonsters() {
        Logger.log("Loading monsters...");
        // Load monsters from file
        File f;
        try {
            f = ConfigLoader.loadConfig(Constants.MONSTERS_FILE);
        } catch (FileNotFoundException e) {
            Logger.warn("Monsters file not found, creating new one");
            try {
                f = ConfigLoader.createConfig(Constants.MONSTERS_FILE);
            } catch (Exception ex) {
                Logger.error("Unable to create monsters file: " + ex.getMessage());
                return;
            }
        } catch (Exception e) {
            Logger.error("Error loading monsters file: " + e.getMessage());
            return;
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            Logger.error("Monsters file not found: " + e.getMessage());
            return;
        }

        try {
            var data = this.deserializer.readFile(br);
            var rawMonsters = this.deserializer.deserialize(data);
            this.monsters = rawMonsters.stream().map(map -> {
                try {
                    return (Monster) this.deserializer.hydrate(map);
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                    return null;
                }
            }).filter(Objects::nonNull).toList();
        } catch (IOException e) {
            Logger.error("Error reading monsters file: " + e.getMessage());
        } catch (InvalidDataError e) {
            Logger.error(e.getMessage());
            var stack = e.getStackTrace();
            for (int i = 0; i < 15; i++) {
                Logger.error(stack[i].toString());
            }
        }
    }

    private void LoadAttacks() {
        Logger.log("Loading attacks...");
        // Load attacks from file
        File f;
        try {
            f = ConfigLoader.loadConfig(Constants.ATTACKS_FILE);
        } catch (FileNotFoundException e) {
            Logger.warn("Attacks file not found, creating new one");
            try {
                f = ConfigLoader.createConfig(Constants.ATTACKS_FILE);
            } catch (Exception ex) {
                Logger.error("Unable to create attacks file: " + ex.getMessage());
                return;
            }
        } catch (Exception e) {
            Logger.error("Error loading attacks file: " + e.getMessage());
            return;
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            Logger.error("Attack file not found: " + e.getMessage());
            return;
        }

        try {
            var data = this.deserializer.readFile(br);
            var rawAttacks = this.deserializer.deserialize(data);
            this.attacks = rawAttacks.stream().map(map -> {
                try {
                    return (Attack) this.deserializer.hydrate(map);
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                    return null;
                }
            }).filter(Objects::nonNull).toList();
        } catch (IOException e) {
            Logger.error("Error reading attack file: " + e.getMessage());
        } catch (InvalidDataError e) {
            Logger.error(e.getMessage());
        }
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public Monster findMonster(String name) {
        return monsters.stream().filter(monster -> monster.toString().equals(name)).findFirst().orElse(null);
    }

    public void addMonsterTeam1(Monster monster) {
        team1.add(this.generateBattleMon(monster));
    }

    public void addMonsterTeam2(Monster monster) {
        team2.add(this.generateBattleMon(monster));
    }

    public LinkedList<BattleMon> getTeam1() {
        return team1;
    }

    public LinkedList<BattleMon> getTeam2() {
        return team2;
    }

    private BattleMon generateBattleMon(Monster monster) {
        return new BattleMon(monster);
    }

    public Game startGame() {
        game = new Game(team1, team2, items);
        game.start();
        return game;
    }

    public static void reset() {
        team1.clear();
        team2.clear();

        game = null;
    }

}
