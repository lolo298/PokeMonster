package com.pokemonSimulator.Utils;

import com.pokemonSimulator.Utils.Errors.InvalidSprite;
import javafx.scene.image.Image;

import java.util.Objects;

public class SpriteLoader {
    public static Image loadSprite(String path) throws InvalidSprite {
        try {
            return new Image(Objects.requireNonNull(SpriteLoader.class.getResourceAsStream(path)));
        } catch (Exception e) {
            throw new InvalidSprite("Invalid sprite path: " + path);
        }
    }

    public static Image loadSprite(String path, int width, int height, boolean preserveRatio, boolean smooth) throws InvalidSprite {
        try {
            return new Image(Objects.requireNonNull(SpriteLoader.class.getResourceAsStream(path)), width, height, preserveRatio, smooth);
        } catch (Exception e) {
            throw new InvalidSprite("Invalid sprite path: " + path);
        }
    }
}
