package com.pokemonSimulator.Utils.Values.enums;

import com.pokemonSimulator.Utils.Values.Interfaces.ISprite;
import com.pokemonSimulator.Utils.SpriteLoader;
import javafx.scene.image.Image;

public enum Status implements ISprite {
    ALL,
    NONE,
    HIDDEN,
    BURNED,
    PARALYZED;


    @Override
    public Image getSprite() {
        if (this == NONE) {
            return null;
        }
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Status/" + this.name() + ".png");
    }

    @Override
    public Image getSprite(int width, int height) {
        if (this == NONE) {
            return null;
        }
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Status/" + this.name() + ".png", width, height, true, false);
    }
}
