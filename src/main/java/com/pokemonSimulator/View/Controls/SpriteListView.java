package com.pokemonSimulator.View.Controls;

import com.pokemonSimulator.Utils.ISprite;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteListView<T extends ISprite> extends ListView<T> {
    public SpriteListView() {
        super();
        this.setCellFactory((ListView<T> tListView) -> new ListCell<>() {
            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Image sprite = item.getSprite(40, 40);

                setText(item.toString());
                setGraphic(new ImageView(sprite));

            }
        });
    }
}
