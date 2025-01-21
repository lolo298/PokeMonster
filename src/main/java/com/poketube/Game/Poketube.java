package com.poketube.Game;

import com.poketube.Game.Monsters.Monster;
import com.poketube.Utils.ConfigLoader;
import com.poketube.Utils.Deserializer;
import com.poketube.Utils.Errors.InvalidDataError;
import com.poketube.Utils.Serializer;
import com.poketube.Utils.Logger;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class Poketube {
    public static final String MONSTERS_FILE = "monsters.txt";
    public static final String PRESETS_FILE = "presets.txt";

    private Serializer serializer;
    private Deserializer deserializer;

    private List<Monster> monsters;

    public static Poketube instance;

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

    public List<Monster> getMonsters() {
        return monsters;
    }

    public Monster findMonster(String name) {
        return monsters.stream().filter(monster -> monster.toString().equals(name)).findFirst().orElse(null);
    }
}
