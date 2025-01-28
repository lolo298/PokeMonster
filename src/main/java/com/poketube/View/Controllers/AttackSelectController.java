package com.poketube.View.Controllers;

import com.poketube.Game.Monsters.Attack;
import com.poketube.Game.Monsters.BattleMon;
import com.poketube.Game.Poketube;
import com.poketube.Game.Types.Types;
import com.poketube.Utils.Logger;
import com.poketube.View.MainController;
import com.poketube.View.Screens;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AttackSelectController implements IController {
    public static final int ATTACK_PER_MON = 4;
    private MainController mainController;
    private int Step = 0;

    @FXML
    private Button startButton;
    @FXML
    private ListView<BattleMon> teamList;
    @FXML
    private ListView<Attack> attackList;

    private HashMap<BattleMon, Attack[]> teamAttacks = new HashMap<>();

    private List<Attack> availableAttacks;

    public void setMainController(MainController mainController) {

        System.out.println("setMainController: " + mainController);
        this.mainController = mainController;
    }

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
        Poketube app = Poketube.getInstance();

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
            Logger.warn("syncAttacks: " + selectedAttacks);
            var selectedMon = teamList.getSelectionModel().getSelectedItem();
            if (selectedMon == null) {
                return;
            }

            if (selectedAttacks.size() > ATTACK_PER_MON) {
                var wasAdded = c.getAddedSubList().get(0);
                System.out.println("wasAdded: " + wasAdded);
                int index = attackList.getItems().indexOf(wasAdded);
                System.out.println("index: " + index);
                Platform.runLater(() -> {
                    attackList.getSelectionModel().clearSelection(index);
                });
            }

            Logger.log("Saving attacks for " + selectedMon);

            Attack[] attacks = teamAttacks.get(selectedMon);

            for (int i = 0; i < attacks.length; i++) {
                if (i < selectedAttacks.size()) {
                    attacks[i] = selectedAttacks.get(i);
                    System.out.println("attacks[" + i + "]: " + attacks[i]);
                } else {
                    attacks[i] = null;
                }
            }

            teamAttacks.put(selectedMon, attacks);
            System.out.println("teamAttacks: " + Arrays.toString(teamAttacks.get(selectedMon)));
        }
    }

    private void loadAttacks() {
        attackList.getSelectionModel().getSelectedItems().removeListener(this.attackChange);
        var selectedMon = teamList.getSelectionModel().getSelectedItem();
        if (selectedMon == null) {
            return;
        }

        Types type = selectedMon.getType().getType();

        List<Attack> compatibleAttacks = this.availableAttacks.stream().filter(attack -> attack.getType().getType() == type || attack.getType().getType() == Types.NORMAL).toList();
        System.out.println("compatibleAttacks for " + type + ": " + compatibleAttacks);
        attackList.getItems().clear();
        attackList.getItems().addAll(compatibleAttacks);


        Attack[] attacks = teamAttacks.get(selectedMon);
        System.out.println("attacks: " + Arrays.toString(attacks));
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

        List<BattleMon> monsters = Poketube.getInstance().getTeam2();
        for (BattleMon mon : monsters) {
            teamAttacks.put(mon, new Attack[4]);
        }

        teamList.getItems().addAll(monsters);

        teamList.getSelectionModel().getSelectedItems().addListener(this.teamChange);
        attackList.getSelectionModel().getSelectedItems().addListener(this.attackChange);

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
