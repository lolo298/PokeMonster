package com.pokemonSimulator.Utils.Values.enums;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.Values.Interfaces.ISprite;
import com.pokemonSimulator.Utils.SpriteLoader;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.Range;
import javafx.scene.image.Image;

public enum Terrain implements ISprite {
    NORMAL(new Range(-1, -1)),
    FLOOD(new Range(1, 3));

    private final Range range;
    private Integer duration;
    private BattleMon creator;

    Terrain(Range range) {
        this.range = range;
    }

    public Integer getDuration() {
        return duration;
    }

    public void turn() {
        if (this == NORMAL) return;
        duration.decrement();
    }

    public void apply(BattleMon creator) {
        this.creator = creator;
        this.duration = range.generateValue();
    }

    public BattleMon getCreator() {
        return creator;
    }

    @Override
    public Image getSprite() {
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Battle/" + this.name() + "_" + TerrainSpriteTypes.BG + ".png");
    }

    @Override
    public Image getSprite(int width, int height) {
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Battle/" + this.name() + "_" + TerrainSpriteTypes.BG + ".png", width, height, true, false);
    }

    public Image getSprite(TerrainSpriteTypes type) {
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Battle/" + this.name() + "_" + type + ".png");
    }
}
