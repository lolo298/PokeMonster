package com.poketube.Utils;

import com.poketube.Game.Monsters.Monster;
import com.poketube.Game.Types.Types;
import com.poketube.Game.Types.Type;
import com.poketube.Utils.Errors.InvalidDataError;
import com.poketube.Utils.Values.Integer;
import com.poketube.Utils.Values.String;
import com.poketube.Utils.Values.Range;

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
            if (map == null) {
                map = new HashMap<>();
                map.put(new String("class"), new String(line.trim()));
                continue;
            }

            var tmp = "End" + map.get(new String("class"));

            if (line.equals("End" + map.get(new String("class")))) {
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

//        int tmp = this.lineNumber;
        this.lineNumber = 0;
//        throw new InvalidDataError(tmp, this.DeserializeSource);
        return data;
    }

    private Serializable parseValue(java.lang.String value) throws InvalidDataError {
        if (value == null) {
            throw new InvalidDataError(this.lineNumber, this.DeserializeSource);
        }
        logger.log("checking value: " + value);
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
        List<java.lang.String> types = Arrays.stream(Types.values()).map(Enum::name).toList();

        if (types.contains(value)) {
            var name = value.charAt(0) + value.substring(1).toLowerCase() + "Type";
            Types type = Types.valueOf(value);
            try {
                return (Type) Class.forName("com.poketube.Game.Types." + name).getConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        if (value.matches("[0-9]+")) {
            return new Integer(java.lang.Integer.parseInt(value));
        }

        return new String(value.trim());
    }


    public Serializable hydrate(HashMap<String, Serializable> data) {
        Serializable obj = null;
        String className = (String)data.get(new String("class"));
        if (className == null) {
            return null;
        }

        switch (className.getValue()) {
            case "Monster":
                var keys = data.keySet();
                Monster monster = new Monster();
                for (String key : keys) {
                    if (key.getValue().equals("class")) {
                        continue;
                    }

                    try {
                        var value = data.get(key);
                        var keyVal = key.getValue();
                        var method = "set" +  keyVal.substring(0, 1).toUpperCase() + keyVal.substring(1);
                        var classType = value.getClass();
                        if (value instanceof Type) {
                            classType = Type.class;
                        }
                        monster.getClass().getMethod(method, classType).invoke(monster, data.get(key));
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        continue;
                    }

                }
                obj = monster;
                break;
            default:
                break;
        }

        return obj;
    }

    public java.lang.String readFile(BufferedReader br) throws IOException {
        StringBuilder builder = new StringBuilder();
        java.lang.String line;

        while((line = br.readLine()) != null) {
            builder.append(line);
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
