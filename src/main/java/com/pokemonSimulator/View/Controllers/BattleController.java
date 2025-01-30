package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Actions.SwitchMon;
import com.pokemonSimulator.Game.Game;
import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.View.MainController;
import com.pokemonSimulator.View.Screens;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Arrays;

public class BattleController extends Controller {
    private Game game;

    @FXML
    private Label infoLabel;

    @FXML
    private StackPane battlePane;
    @FXML
    private ImageView battleBackground;
    @FXML
    private ImageView battleBackgroundMon1;
    @FXML
    private ImageView battleBackgroundMon2;
    @FXML
    private AnchorPane battleMonPane1;
    @FXML
    private AnchorPane battleMonPane2;


    @FXML
    private Label activeMonLabel;
    @FXML
    private ImageView activeMonImg;
    @FXML
    private ProgressBar activeHpBar;
    @FXML
    private Label enemyMonLabel;
    @FXML
    private ImageView enemyMonImg;
    @FXML
    private ProgressBar enemyHpBar;

    @FXML
    private Button act1;
    @FXML
    private Tooltip act1Tooltip;
    @FXML
    private Button act2;
    @FXML
    private Tooltip act2Tooltip;
    @FXML
    private Button act3;
    @FXML
    private Tooltip act3Tooltip;
    @FXML
    private Button act4;
    @FXML
    private Tooltip act4Tooltip;

    @FXML
    private ListView<String> useItemsList;
    @FXML
    private ListView<BattleMon> switchMonList;
    @FXML
    private Button switchButton;

    private int isSwitching = 0;

    @FXML
    private void onSwitch() {
        BattleMon selectedMon = switchMonList.getSelectionModel().getSelectedItem();

        if (selectedMon == null || selectedMon == game.getActiveMon() || selectedMon.isFainted()) {
            return;
        }

        SwitchMon switchMon = new SwitchMon(selectedMon);

        if (isSwitching != 0) {
            game.switchMon(isSwitching, switchMon);

            blockActions(false);

            isSwitching = 0;
        } else {
            game.setActivePlayerAction(switchMon);
            if (game.getPlayerTurn() == 2) {
                battle();
            }
        }

        game.nextPlayer();
        loadBattleView();
    }

    @FXML
    void initialize() {
        this.game = PokemonSimulator.getInstance().startGame();

        loadBattleView();
    }

    @FXML
    private void onActSelect(ActionEvent e) {
        Button clickedAct = (Button) e.getSource();

        BattleMon activeMon = game.getActiveMon();
        Attack[] attacks = activeMon.getAttacks();

        Attack attack = null;

        if (clickedAct == act1) {
            attack = attacks[0];
        } else if (clickedAct == act2) {
            attack = attacks[1];
        } else if (clickedAct == act3) {
            attack = attacks[2];
        } else if (clickedAct == act4) {
            attack = attacks[3];
        }

        assert attack != null;
        game.setActivePlayerAction(attack);

        if (game.getPlayerTurn() == 2) {
            battle();
        }
        if (isSwitching == 0) {
            game.nextPlayer();
            loadBattleView();
        }
    }

    private void loadBattleView() {
        BattleMon activeMon = game.getActiveMon();
        BattleMon enemyMon = game.getEnemyMon();

        System.out.println(activeMon);
        System.out.println(enemyMon);

        activeMonLabel.setText(activeMon.getName().getValue());
        double health = clamp(activeMon.getHealth().getValue(), 0, activeMon.getMaxHealth().getValue());
        activeHpBar.setProgress(normalize(health, 0, activeMon.getMaxHealth().getValue(), 0, 1));

        try {
            activeMonImg.setImage(activeMon.getSprites().getSecond());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        enemyMonLabel.setText(enemyMon.getName().getValue());
        health = clamp(enemyMon.getHealth().getValue(), 0, enemyMon.getMaxHealth().getValue());
        enemyHpBar.setProgress(normalize(health, 0, enemyMon.getMaxHealth().getValue(), 0, 1));

        try {
            enemyMonImg.setImage(enemyMon.getSprites().getFirst());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Attack[] attacks = activeMon.getAttacks();
        for (int i = 0; i < 4; i++) {
            Attack attack = attacks[i];
            Button act = null;
            Tooltip actTooltip = null;
            switch (i) {
                case 0: {
                    act = act1;
                    actTooltip = act1Tooltip;
                    break;
                }
                case 1: {
                    act = act2;
                    actTooltip = act2Tooltip;
                    break;
                }
                case 2: {
                    act = act3;
                    actTooltip = act3Tooltip;
                    break;
                }
                case 3: {
                    act = act4;
                    actTooltip = act4Tooltip;
                    break;
                }
            }

            switchMonList.getItems().clear();
            switchMonList.getItems().addAll(game.getActiveTeam());

            assert act != null && actTooltip != null;
            act.setText(attack.getName().getValue());
            actTooltip.setText(String.format("%d/%d", attack.getPp().getValue(), attack.getBasePp().getValue()));
            actTooltip.setShowDelay(Duration.millis(500));
            infoLabel.setText("Player " + game.getPlayerTurn() + "'s turn");

        }
    }

    private void battle() {
        game.battle();

        Logger.warn(game.getTeam1().toString());
        Logger.warn(game.getTeam2().toString());

        Logger.warn(game.getPlayer1Mon().toString());
        Logger.warn(game.getPlayer2Mon().toString());


        if (game.isOver()) {
            int winner = game.getWinner();

            blockActions(true);
            switchButton.setDisable(true);

            mainController.switchView(Screens.END);



            return;
        }


        //check fainted mons
        if (game.getPlayer1Mon().isFainted()) {
            game.setTurn(1);
            loadBattleView();

            activeMonImg.setImage(null);

            switchMonList.refresh();

            blockActions(true);
            infoLabel.setText("Player 1's " + game.getPlayer1Mon().getName() + " fainted");
            isSwitching = 1;
        } else if (game.getPlayer2Mon().isFainted()) {
            game.setTurn(2);
            loadBattleView();
            switchMonList.refresh();

            activeMonImg.setImage(null);

            blockActions(true);
            infoLabel.setText("Player 2's " + game.getPlayer2Mon().getName() + " fainted");
            isSwitching = 2;
        }
    }

    private void blockActions(boolean block) {
        act1.setDisable(block);
        act2.setDisable(block);
        act3.setDisable(block);
        act4.setDisable(block);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double normalize(double value, double originalMin, double originalMax, double newMin, double newMax) {
        double normalized = (value - originalMin) / (originalMax - originalMin);
        return newMin + normalized * (newMax - newMin);
    }
}
