package com.pokemonSimulator.Utils;

import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Monsters.Monster;
import com.pokemonSimulator.Game.Types.Type;
import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;
import com.pokemonSimulator.Utils.Values.enums.BuffStat;
import com.pokemonSimulator.Utils.Values.enums.Types;
import com.pokemonSimulator.Utils.Errors.InvalidDataError;
import com.pokemonSimulator.Utils.Values.Float;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.*;
import com.pokemonSimulator.Utils.Values.enums.Targets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Deserializer {

    private int lineNumber;
    private java.lang.String DeserializeSource;


    public List<HashMap<String, Serializable>> deserialize(java.lang.String source) throws InvalidDataError {
        List<HashMap<String, Serializable>> data = new ArrayList<>();
        this.lineNumber = 0;
        this.DeserializeSource = source;
        HashMap<String, Serializable> map = null;

        java.lang.String[] lines = this.DeserializeSource.split("\n");
        for (java.lang.String line : lines) {
            this.lineNumber++;

            if (line.trim().isEmpty()) {
                continue;
            }

            if (map == null) {
                map = new HashMap<>();
                map.put(new String("class"), new String(line.trim()));
                continue;
            }


            if (line.trim().equals("End" + map.get(new String("class")))) {
                data.add(map);
                map = null;
            }

            java.lang.String[] keyVal = line.split(":");
            if (keyVal.length != 2) {
                continue;
            }
            String key = new String(keyVal[0].trim());
            Serializable value = parseValue(keyVal[1]);
            assert map != null;
            map.put(key, value);
        }

        this.lineNumber = 0;
        return data;
    }

    private Serializable parseValue(java.lang.String value) throws InvalidDataError {
        if (value == null) {
            throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
        }
        Logger.log("checking value: " + value);
        value = value.trim();
        if (value.isEmpty()) {
            throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
        }
        if (value.contains("/")) {
            java.lang.String[] split = value.split("/");
            if (split.length != 2) {
                throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
            }

            Serializable min = this.parseValue(split[0]);
            Serializable max = this.parseValue(split[1]);

            if (min instanceof Integer && max instanceof Integer) {
                return new Range((Integer) min, (Integer) max);
            }
            throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
        }
        if (value.contains("$")) {
            java.lang.String[] split = value.split("\\$");
            if (split.length != 3) {
                throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
            }

            Serializable stat = this.parseValue(split[0]);
            Serializable power = this.parseValue(split[1]);
            Serializable target = this.parseValue(split[2]);

            if (stat instanceof BuffStat && power instanceof Integer && target instanceof Targets) {
                return new Buff((BuffStat) stat, (Integer) power, (Targets) target);
            }
            throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
        }


        List<java.lang.String> types = Arrays.stream(Types.values()).map(Enum::name).toList();
        if (types.contains(value)) {
            var name = value.charAt(0) + value.substring(1).toLowerCase() + "Type";
            try {
                return (Type) Class.forName("com.pokemonSimulator.Game.Types." + name).getConstructor().newInstance();
            } catch (Exception e) {
                Logger.warn("Unable to create type: " + e.getMessage());
                Logger.error(e.getMessage());
            }
        }

        List<java.lang.String> targets = Arrays.stream(Targets.values()).map(Enum::name).toList();
        if (targets.contains(value)) {
            return Targets.valueOf(value);
        }

        List<java.lang.String> buffStat = Arrays.stream(BuffStat.values()).map(Enum::name).toList();
        if (buffStat.contains(value)) {
            return BuffStat.valueOf(value);
        }


        if (value.matches("-?[0-9]+")) {
            return new Integer(java.lang.Integer.parseInt(value));
        }

        if (value.matches("-?[0-9]*\\.[0-9]+")) {
            return new Float(java.lang.Float.parseFloat(value));
        }

        return new String(value.trim());
    }


    public Serializable hydrate(HashMap<String, Serializable> data) {
        Serializable obj;
        String className = (String) data.get(new String("class"));
        if (className == null) {
            Logger.warn("Class name not found");
            return null;
        }
        Serializable cls = null;
        switch (className.getValue()) {
            case "Monster":
                cls = new Monster();
                break;
            case "Attack":
                cls = new Attack();
                break;
            default:
                break;
        }

        var keys = data.keySet();
        for (String key : keys) {
            if (key.getValue().equals("class")) {
                continue;
            }

            try {
                var value = data.get(key);
                var keyVal = key.getValue();
                var method = "set" + keyVal.substring(0, 1).toUpperCase() + keyVal.substring(1);
                var classType = value.getClass();
                if (value instanceof Type) {
                    classType = Type.class;
                }

                assert cls != null;
                cls.getClass().getMethod(method, classType).invoke(cls, data.get(key));
            } catch (Exception e) {
                Logger.error("Unable to hydrate " + key + ": " + e.getMessage());
            }

        }
        obj = cls;

        return obj;
    }

    public java.lang.String readFile(BufferedReader br) throws IOException {
        StringBuilder builder = new StringBuilder();
        java.lang.String line;

        while ((line = br.readLine()) != null) {
            builder.append(line);
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
