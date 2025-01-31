package com.pokemonSimulator.Utils;

import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Utils.Errors.CantCreateFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public class ConfigLoader {
    public static void initConfig() throws CantCreateFile {
        File f = new File(Constants.CONFIG_FOLDER);
        if (!f.exists()) {
            Logger.warn("Config folder not found, creating new one");
            boolean res = f.mkdir();
            if (!res) {
                Logger.error("Unable to create config folder");
                throw new CantCreateFile("Unable to create config folder");
            }
        }
    }

    public static File loadConfig(String path) throws FileNotFoundException, CantCreateFile {
        ConfigLoader.initConfig();
        path = Constants.CONFIG_FOLDER + File.separator + path;

        File f = new File(path);

        if (!f.exists()) {
            Logger.error("Config file not found: " + path);
            throw new FileNotFoundException("Config file not found: " + path);
        }

        Logger.log("Config file loaded: " + path);
        return f;
    }

    public static File createConfig(String path) throws FileAlreadyExistsException, CantCreateFile {
        ConfigLoader.initConfig();
        path = Constants.CONFIG_FOLDER + File.separator + path;

        File f = new File(path);
        if (f.exists()) {
            throw new FileAlreadyExistsException("Config file already exists: " + path);
        }

        try {
            boolean res = f.createNewFile();
            if (!res) {
                throw new Exception("Unable to create config file: " + path);
            }
        } catch (Exception e) {
            Logger.error("Unable to create config file: " + e);
            throw new CantCreateFile("Unable to create config file: " + path);
        }

        Logger.log("Config file created: " + path);
        return f;
    }

}
