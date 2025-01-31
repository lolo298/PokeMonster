package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.Monsters.Monster;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Values.enums.Screens;
import com.pokemonSimulator.View.Controls.SpriteListView;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.LinkedList;
import java.util.List;

public class TeamSelectionController extends Controller {
    private int Step = 0;

    @FXML
    private Label mainLabel;
    @FXML
    private Button startButton;
    @FXML
    public SpriteListView<Monster> team1List;
    @FXML
    public SpriteListView<Monster> team2List;

    @FXML
    private TextField team1Search;
    @FXML
    private TextField team2Search;

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

    private void addMonster(LinkedList<Monster> team, Monster monster) {
        if (team.size() >= Constants.TEAM_SIZE || team.contains(monster)) {
            return;
        }
        team.add(monster);
    }

    private void removeMonster(LinkedList<Monster> team, Monster monster) {
        team.remove(monster);
    }


    @FXML
    void initialize() {
        PokemonSimulator app = PokemonSimulator.getInstance();

        List<Monster> monsters = app.getMonsters();

        mainLabel.setText("Select your team (Up to " + Constants.TEAM_SIZE + " monsters)");

        team1List.getItems().addAll(monsters);
        team2List.getItems().addAll(monsters);

        team1List.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        team2List.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        team1List.getSelectionModel().getSelectedItems().addListener(this.team1Change);
        team2List.getSelectionModel().getSelectedItems().addListener(this.team2Change);


        team1Search.textProperty().addListener((observable, oldValue, newValue) -> {
            team1List.getSelectionModel().getSelectedItems().removeListener(team1Change);
            team1List.getItems().clear();
            for (Monster monster : monsters) {
                if (monster.getName().getValue().toLowerCase().contains(newValue.toLowerCase())) {
                    team1List.getItems().add(monster);
                }
            }
            syncSelected(team1, team1List);
            team1List.getSelectionModel().getSelectedItems().addListener(team1Change);
        });

        team2Search.textProperty().addListener((observable, oldValue, newValue) -> {
            team2List.getSelectionModel().getSelectedItems().removeListener(team2Change);
            team2List.getItems().clear();
            for (Monster monster : monsters) {
                if (monster.getName().getValue().toLowerCase().contains(newValue.toLowerCase())) {
                    team2List.getItems().add(monster);
                }
            }
            syncSelected(team2, team2List);
            team2List.getSelectionModel().getSelectedItems().addListener(team2Change);
        });

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

                mainLabel.setText("Reorder your team, the first monster will be your lead");

                Step++;
                break;
            case 1:
                PokemonSimulator app = PokemonSimulator.getInstance();

                for (Monster monster : team1List.getItems()) {
                    app.addMonsterTeam1(monster);
                }
                for (Monster monster : team2List.getItems()) {
                    app.addMonsterTeam2(monster);
                }
                mainController.switchView(Screens.SELECT_ATTACK);
                break;
        }


    }

    private void syncTeam(ListChangeListener.Change<? extends Monster> c, LinkedList<Monster> team, ListView<Monster> teamList) {
        ObservableList<Monster> selectedItems = teamList.getSelectionModel().getSelectedItems();


        while (c.next()) {
            for (Monster monster : c.getAddedSubList()) {
                addMonster(team, monster);
            }

            for (Monster monster : c.getRemoved()) {
                removeMonster(team, monster);
            }
        }

        if (selectedItems.size() > Constants.TEAM_SIZE) {
            Monster oldest = team.poll();
            int indexToDeselect = teamList.getItems().indexOf(oldest);
            Platform.runLater(() -> teamList.getSelectionModel().clearSelection(indexToDeselect));
        }
    }

    private void syncSelected(LinkedList<Monster> team, ListView<Monster> teamList) {
        for (Monster monster : team) {
            Platform.runLater(() -> teamList.getSelectionModel().select(monster));
        }
    }

    public void initDrag(SpriteListView<Monster> listView, LinkedList<Monster> team) {
        listView.setDragEnabled(true);
        listView.getItems().clear();
        listView.getItems().addAll(team);
    }
}