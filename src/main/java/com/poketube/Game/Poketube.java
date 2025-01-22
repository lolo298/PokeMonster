package com.poketube.Game;

import com.poketube.Game.Monsters.Attack;
import com.poketube.Game.Monsters.BattleMon;
import com.poketube.Game.Monsters.Monster;
import com.poketube.Utils.ConfigLoader;
import com.poketube.Utils.Deserializer;
import com.poketube.Utils.Errors.InvalidDataError;
import com.poketube.Utils.Serializer;
import com.poketube.Utils.Logger;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Poketube {
    public static final String MONSTERS_FILE = "monsters.txt";
    public static final String ATTACKS_FILE = "attacks.txt";
    public static final String PRESETS_FILE = "presets.txt";

    private Serializer serializer;
    private Deserializer deserializer;

    private List<Monster> monsters;
    private List<Attack> attacks;

    public static Poketube instance;

    private static final LinkedList<BattleMon> team1 = new LinkedList<>();
    private static final LinkedList<BattleMon> team2 = new LinkedList<>();

    public Poketube() {
        if (instance != null) {
            throw new IllegalStateException("Poketube already initialized");
        }
    }

    public static Poketube getInstance() {
        if (instance == null) {
            instance = new Poketube();
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
        System.out.println(this.monsters);
        Logger.log("Welcome to Poketube!");
    }

    private void LoadMonsters() {
        // Load monsters from file
        File f;
        try {
            f = ConfigLoader.loadConfig(MONSTERS_FILE);
        } catch (FileNotFoundException e) {
            Logger.warn("Monsters file not found, creating new one");
            try {
                f = ConfigLoader.createConfig(MONSTERS_FILE);
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
            System.out.println(rawMonsters);
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
        }
    }

    private void LoadAttacks() {
        // Load attacks from file
        File f;
        try {
            f = ConfigLoader.loadConfig(ATTACKS_FILE);
        } catch (FileNotFoundException e) {
            Logger.warn("Attacks file not found, creating new one");
            try {
                f = ConfigLoader.createConfig(ATTACKS_FILE);
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
            System.out.println(rawAttacks);
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
        BattleMon battleMon = new BattleMon();
        battleMon.setName(monster.getName());
        battleMon.setType(monster.getType());
        battleMon.setAttack(monster.getAttack().generateValue());
        battleMon.setDefense(monster.getDefense().generateValue());
        battleMon.setHealth(monster.getHealth().generateValue());
        battleMon.setSpeed(monster.getSpeed().generateValue());
        return battleMon;
    }

}
