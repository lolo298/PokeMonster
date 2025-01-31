package com.pokemonSimulator.Game.Monsters;

import com.pokemonSimulator.Utils.Errors.InvalidSprite;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.SpriteLoader;
import com.pokemonSimulator.Utils.Values.Interfaces.ISprite;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.Tuple;
import com.pokemonSimulator.Utils.Values.enums.SpriteOrientation;
import com.pokemonSimulator.Utils.Values.enums.SpritesType;
import javafx.scene.image.Image;

public class MonsterSprite implements ISprite {

    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Image getSprite(SpriteOrientation side, SpritesType type) {
        java.lang.String extension = switch (type) {
            case STATIC -> "png";
            case ANIMATED -> "gif";
        };

        java.lang.String orientation = switch (side) {
            case BACK -> "back";
            case FRONT -> "front";
        };

        Image sprite;

        try {
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/" + this.getName().getValue().toLowerCase() + "_" + orientation + "." + extension);
        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/default.png");
        }

        return sprite;
    }

    public Image getSprite(SpriteOrientation side, SpritesType type, int width, int height) {
        java.lang.String extension = switch (type) {
            case STATIC -> "png";
            case ANIMATED -> "gif";
        };

        java.lang.String orientation = switch (side) {
            case BACK -> "back";
            case FRONT -> "front";
        };

        Image sprite;

        try {
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/" + this.getName().getValue().toLowerCase() + "_" + orientation + "." + extension, width, height, false, false);
        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
            sprite = SpriteLoader.loadSprite("/com/pokemonSimulator/Sprites/default.png", width, height, true, false);
        }

        return sprite;
    }

    public Image getSprite() {
        return getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED);
    }

    public Image getSprite(int width, int height) {
        return getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED, width, height);
    }

    public Tuple<Image, Image> getSprites() {
        var frontImage = getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED);
        var backImage = getSprite(SpriteOrientation.BACK, SpritesType.ANIMATED);

        return new Tuple<>(frontImage, backImage);
    }
}
