package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Constants;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Values.enums.Types;
import com.pokemonSimulator.Utils.Values.enums.Screens;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AttackSelectController extends Controller {
    private int Step = 0;

    @FXML
    private Label mainLabel;
    @FXML
    private Button startButton;
    @FXML
    private ListView<BattleMon> teamList;
    @FXML
    private ListView<Attack> attackList;

    private HashMap<BattleMon, Attack[]> teamAttacks = new HashMap<>();

    private List<Attack> availableAttacks;

    private final ListChangeListener<BattleMon> teamChange = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends BattleMon> c) {
            loadAttacks();
        }
    };

    private final ListChangeListener<Attack> attackChange = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends Attack> c) {
            syncAttacks(c);
            checkButton();
        }
    };


    @FXML
    void initialize() {
        PokemonSimulator app = PokemonSimulator.getInstance();

        mainLabel.setText("Player 1: Select your team's attacks (Exactly " + Constants.ATTACK_PER_MON + " attacks per monster)");

        List<BattleMon> monsters = app.getTeam1();

        this.availableAttacks = app.getAttacks();

        for (BattleMon mon : monsters) {
            teamAttacks.put(mon, new Attack[4]);
        }

        teamList.getItems().addAll(monsters);
        teamList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        attackList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        teamList.getSelectionModel().getSelectedItems().addListener(this.teamChange);
        attackList.getSelectionModel().getSelectedItems().addListener(this.attackChange);
    }

    private <T> void syncAttacks(ListChangeListener.Change<? extends T> c) {
        while (c.next()) {


            var selectedAttacks = attackList.getSelectionModel().getSelectedItems();
            var selectedMon = teamList.getSelectionModel().getSelectedItem();
            if (selectedMon == null) {
                return;
            }

            if (selectedAttacks.size() > Constants.ATTACK_PER_MON) {
                var wasAdded = c.getAddedSubList().get(0);
                int index = attackList.getItems().indexOf(wasAdded);
                Platform.runLater(() -> {
                    attackList.getSelectionModel().clearSelection(index);
                });
            }


            Attack[] attacks = teamAttacks.get(selectedMon);

            for (int i = 0; i < attacks.length; i++) {
                if (i < selectedAttacks.size()) {
                    attacks[i] = selectedAttacks.get(i);
                } else {
                    attacks[i] = null;
                }
            }

            teamAttacks.put(selectedMon, attacks);
        }
    }

    private void loadAttacks() {
        attackList.getSelectionModel().getSelectedItems().removeListener(this.attackChange);
        var selectedMon = teamList.getSelectionModel().getSelectedItem();
        if (selectedMon == null) {
            attackList.getItems().clear();
            return;
        }

        Types type = selectedMon.getType().getType();

        List<Attack> compatibleAttacks = this.availableAttacks.stream().filter(attack -> attack.getType().getType() == type || attack.getType().getType() == Types.NORMAL).toList();
        attackList.getItems().clear();
        attackList.getItems().addAll(compatibleAttacks);


        Attack[] attacks = teamAttacks.get(selectedMon);
        for (Attack attack : attacks) {
            if (attack != null) {
                attackList.getSelectionModel().select(attack);
            }
        }
        attackList.getSelectionModel().getSelectedItems().addListener(this.attackChange);
    }

    @FXML
    public void startGame() {
        //save team attacks
        teamAttacks.forEach(BattleMon::setAttacks);

        //switch to second player
        teamList.getSelectionModel().getSelectedItems().removeListener(this.teamChange);
        attackList.getSelectionModel().getSelectedItems().removeListener(this.attackChange);

        teamList.getItems().clear();
        teamAttacks.clear();

        attackList.getItems().clear();

        List<BattleMon> monsters = PokemonSimulator.getInstance().getTeam2();
        for (BattleMon mon : monsters) {
            teamAttacks.put(mon, new Attack[4]);
        }

        teamList.getItems().addAll(monsters);

        teamList.getSelectionModel().getSelectedItems().addListener(this.teamChange);
        attackList.getSelectionModel().getSelectedItems().addListener(this.attackChange);

        mainLabel.setText("Player 2: Select your team's attacks (Exactly " + Constants.ATTACK_PER_MON + " attacks per monster)");

        this.startButton.setText("Start Battle");
        checkButton();


        if (Step == 1) {
            // goto battle screen
            mainController.switchView(Screens.BATTLE);
            return;
        }
        Step++;
    }

    private void checkButton() {
        startButton.setDisable(!teamAttacks.values().stream().allMatch(attacks -> Arrays.stream(attacks).allMatch(Objects::nonNull)));
    }
}
