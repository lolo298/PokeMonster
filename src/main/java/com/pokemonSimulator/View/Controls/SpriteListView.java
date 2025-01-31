package com.pokemonSimulator.View.Controls;

import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.Monsters.Monster;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Values.Interfaces.ISprite;
import com.pokemonSimulator.Utils.Values.enums.Status;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import java.util.ListIterator;

public class SpriteListView<T extends ISprite> extends ListView<T> {

    private BooleanProperty dragEnabled;

    public SpriteListView() {
        super();
        updateCellFactory();


        dragEnabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                updateCellFactory();
            }
        });
    }

    public void updateCellFactory() {
        var items = this.getItems();
        this.setCellFactory((ListView<T> tListView) -> {
            var cell = new ListCell<T>() {
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

                    if (item instanceof BattleMon mon) {
                        if (mon.isFainted()) {
                            ColorAdjust colorAdjust = new ColorAdjust();
                            colorAdjust.setBrightness(-0.5);

                            getGraphic().setEffect(colorAdjust);
                        } else if (mon.getStatus() != Status.NONE) {
                            setText(mon.getName() + " (" + mon.getStatus() + ")");
                        }
                    }
                }
            };

            if (!getDragEnabled()) {
                return cell;
            }

            cell.setOnDragDetected(e -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(cell.getItem().toString());
                    db.setContent(content);
                    e.consume();
                }
            });

            cell.setOnDragOver(e -> {
                if (e.getGestureSource() != cell && e.getDragboard().hasString()) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
                e.consume();
            });

            cell.setOnDragDropped(e -> {
                if (e.getGestureSource() != cell && e.getDragboard().hasString()) {
                    Dragboard db = e.getDragboard();
                    boolean success = false;

                    if (db.hasString()) {
                        Monster draggedItem = PokemonSimulator.getInstance().findMonster(db.getString());
                        int draggedIndex = this.getItems().indexOf(draggedItem);
                        T dragged = this.getItems().get(draggedIndex);
                        int dropIndex = cell.getIndex();


                        if (draggedIndex != dropIndex) {
                            this.getItems().remove(draggedIndex);
                            this.getItems().add(dropIndex, dragged);

                            ListIterator<T> iterator = items.listIterator();
                            while (iterator.hasNext()) {
                                T monster = iterator.next();
                                if (monster.equals(draggedItem)) {
                                    iterator.remove();
                                    items.add(dropIndex, dragged);
                                    break;
                                }
                            }
                        }

                        success = true;
                    }
                    e.setDropCompleted(success);
                    e.consume();
                }
            });

            return cell;


        });

        this.setItems(items);

    }

    public boolean getDragEnabled() {
        return dragEnabledProperty().get();
    }

    public BooleanProperty dragEnabledProperty() {
        if (dragEnabled == null) {
            dragEnabled = new SimpleBooleanProperty(this, "dragEnabled", false);
        }
        return dragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled) {
        dragEnabledProperty().set(dragEnabled);
    }
}
