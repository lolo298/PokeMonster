package com.poketube.Game;

import com.poketube.Game.Monsters.Monster;
import com.poketube.Utils.Deserializer;
import com.poketube.Utils.Errors.InvalidDataError;
import com.poketube.Utils.Serializer;
import com.poketube.Utils.logger;

import java.io.*;
import java.util.List;

public class Poketube {
    public static final String MONSTERS_FILE = "monsters.txt";
    public static final String PRESETS_FILE = "presets.txt";

    private Serializer serializer;
    private Deserializer deserializer;

    private List<Monster> monsters;

    public void start() {
        this.Init();
    }


    private void Init() {
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();

        this.LoadMonsters();
        logger.log("Welcome to Poketube!");
    }

    private void LoadMonsters() {
        // Load monsters from file
        File f = new File(MONSTERS_FILE);

        if (!f.exists()) {
            logger.warn("Monsters file not found, creating new one");
            try {
                boolean res = f.createNewFile();
                if (!res) {
                    logger.error("Unable to create monsters file");
                    return;
                }
            } catch (Exception e) {
                logger.error("Unable to create monsters file: " + e.getMessage());
                return;
            }
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            logger.error("Monsters file not found: " + e.getMessage());
            return;
        }

        try {
            var data = this.deserializer.readFile(br);
            var rawMonsters = this.deserializer.deserialize(data);
            this.monsters = rawMonsters.stream().map(map -> (Monster) this.deserializer.hydrate(map)).toList();
        } catch (IOException e) {
            logger.error("Error reading monsters file: " + e.getMessage());
            return;
        } catch (InvalidDataError e) {
            logger.error(e.getMessage());
            return;
        }
    }

    public List<Monster> getMonsters() {
        return monsters;
    }
}
