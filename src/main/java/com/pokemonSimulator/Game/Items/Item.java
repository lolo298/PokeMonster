package com.pokemonSimulator.Game.Items;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Utils.SpriteLoader;
import com.pokemonSimulator.Utils.Values.Integer;
import com.pokemonSimulator.Utils.Values.Interfaces.IItem;
import com.pokemonSimulator.Utils.Values.Interfaces.ISprite;
import com.pokemonSimulator.Utils.Values.Interfaces.Serializable;
import com.pokemonSimulator.Utils.Values.String;
import com.pokemonSimulator.Utils.Values.enums.ItemTarget;
import javafx.scene.image.Image;

public abstract class Item implements IItem, Serializable, ISprite, Cloneable {

    protected String name;
    protected Integer quantity;
    protected ItemTarget itemTarget;

    public Item(java.lang.String name, int quantity, ItemTarget itemTarget) {
        this.name = new String(name);
        this.quantity = new Integer(quantity);
        this.itemTarget = itemTarget;
    }

    public ItemTarget getItemTarget() {
        return itemTarget;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public Image getSprite() {
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Items/" + this.name.toString().replaceAll(" +", "_") + ".png");
    }

    @Override
    public Image getSprite(int width, int height) {
        return SpriteLoader.loadSprite("/com/pokemonSimulator/Items/" + this.name.toString().replaceAll(" +", "_") + ".png", width, height, true, false);
    }

    @Override
    public java.lang.String serialize() {
        return this.name.serialize();
    }

    @Override
    public java.lang.String toString() {
        return this.name.toString();
    }

    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    protected void use() {
        this.quantity.decrement();
    }
}
