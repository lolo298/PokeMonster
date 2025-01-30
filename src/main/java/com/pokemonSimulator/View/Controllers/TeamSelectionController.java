package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.Monsters.Monster;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Values.enums.Screens;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TeamSelectionController extends Controller {
    private int Step = 0;

    @FXML
    private Button startButton;
    @FXML
    private ListView<Monster> team1List;
    @FXML
    private ListView<Monster> team2List;

    private final LinkedList<Monster> team1 = new LinkedList<>();
    private final LinkedList<Monster> team2 = new LinkedList<>();

    private final ListChangeListener<Monster> team1Change = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends Monster> c) {
            syncTeam(c, team1, team1List);
            startButton.setDisable(team1.isEmpty() || team2.isEmpty());
        }
    };

    private final ListChangeListener<Monster> team2Change = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends Monster> c) {
            syncTeam(c, team2, team2List);
            startButton.setDisable(team1.isEmpty() || team2.isEmpty());
        }
    };


    @FXML
    void initialize() {
        PokemonSimulator app = PokemonSimulator.getInstance();

        List<Monster> monsters = app.getMonsters();


        team1List.getItems().addAll(monsters);
        team2List.getItems().addAll(monsters);

        team1List.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        team2List.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        team1List.getSelectionModel().getSelectedItems().addListener(this.team1Change);
        team2List.getSelectionModel().getSelectedItems().addListener(this.team2Change);
    }

    @FXML
    void startGame() {
        switch (Step) {
            case 0:
                team1List.getSelectionModel().getSelectedItems().removeListener(team1Change);
                team2List.getSelectionModel().getSelectedItems().removeListener(team2Change);

                // Order teams
                team1List.getSelectionModel().clearSelection();
                team2List.getSelectionModel().clearSelection();

                ObservableList<Monster> team1Monsters = team1List.getItems();
                ObservableList<Monster> team2Monsters = team2List.getItems();

                team1Monsters.remove(0, team1Monsters.size());
                team2Monsters.remove(0, team2Monsters.size());

                team1Monsters.addAll(team1);
                team2Monsters.addAll(team2);

                this.initDrag(team1List, team1);
                this.initDrag(team2List, team2);

                Step++;
                break;
            case 1:
                PokemonSimulator app = PokemonSimulator.getInstance();

                for (Monster monster : team1) {
                    app.addMonsterTeam1(monster);
                }
                for (Monster monster : team2) {
                    app.addMonsterTeam2(monster);
                }
                Logger.log("Navigating to select attack screen");
                mainController.switchView(Screens.SELECT_ATTACK);
                break;
        }


    }

    private void syncTeam(ListChangeListener.Change<? extends Monster> c, LinkedList<Monster> team, ListView<Monster> teamList) {
        ObservableList<Monster> selectedItems = teamList.getSelectionModel().getSelectedItems();


        while (c.next()) {

            team.addAll(c.getAddedSubList());
            for (Monster monster : c.getRemoved()) {
                for (int i = 0; i < team.size(); i++) {
                    if (team.get(i).equals(monster)) {
                        team.remove(i);
                    }
                }
            }
        }

        if (selectedItems.size() > Constants.TEAM_SIZE) {
            Monster oldest = team.poll();
            int indexToDeselect = teamList.getItems().indexOf(oldest);
            Platform.runLater(() -> teamList.getSelectionModel().clearSelection(indexToDeselect));
        }
    }

    public void initDrag(ListView<Monster> listView, LinkedList<Monster> team) {
        listView.setCellFactory(lv -> {
            ListCell<Monster> cell = new ListCell<>() {
                @Override
                protected void updateItem(Monster item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };

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
                        int draggedIndex = listView.getItems().indexOf(draggedItem);
                        int dropIndex = cell.getIndex();

                        if (draggedIndex != dropIndex) {
                            listView.getItems().remove(draggedIndex);
                            listView.getItems().add(dropIndex, draggedItem);

                            ListIterator<Monster> iterator = team.listIterator();
                            while (iterator.hasNext()) {
                                Monster monster = iterator.next();
                                if (monster.equals(draggedItem)) {
                                    iterator.remove();
                                    team.add(dropIndex, draggedItem);
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
    }
}