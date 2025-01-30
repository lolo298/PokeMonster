package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Actions.SwitchMon;
import com.pokemonSimulator.Game.Game;
import com.pokemonSimulator.Game.Actions.Attack;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Values.enums.Screens;
import com.pokemonSimulator.Utils.Values.enums.TerrainSpriteTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
    private ImageView statusActiveImg;
    @FXML
    private Label enemyMonLabel;
    @FXML
    private ImageView enemyMonImg;
    @FXML
    private ProgressBar enemyHpBar;
    @FXML
    private ImageView statusEnemyImg;

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
    private Button struggleButton;

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

            game.setTurn(1);

            isSwitching = 0;
        } else {
            game.setActivePlayerAction(switchMon);

            if (game.getPlayerTurn() == 2) {
                battle();
            }
            game.nextPlayer();
        }

        loadBattleView();
    }

    @FXML
    void initialize() {
        this.game = PokemonSimulator.getInstance().startGame();

        act1.visibleProperty().bind(act1.managedProperty());
        act2.visibleProperty().bind(act2.managedProperty());
        act3.visibleProperty().bind(act3.managedProperty());
        act4.visibleProperty().bind(act4.managedProperty());
        struggleButton.visibleProperty().bind(struggleButton.managedProperty());

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

        battleBackground.setImage(game.getTerrainState().getSprite(TerrainSpriteTypes.BG));
        battleBackgroundMon1.setImage(game.getTerrainState().getSprite(TerrainSpriteTypes.BATTLE_FRONT));
        battleBackgroundMon2.setImage(game.getTerrainState().getSprite(TerrainSpriteTypes.BATTLE_BACK));

        activeMonLabel.setText(activeMon.getName().getValue());
        int health = clamp(activeMon.getHealth().getValue(), 0, activeMon.getMaxHealth().getValue());
        activeHpBar.setProgress(normalize(health, 0, activeMon.getMaxHealth().getValue(), 0, 1));


        activeMonImg.setImage(activeMon.getSprites().getSecond());
        statusActiveImg.setImage(activeMon.getStatus().getSprite());

        enemyMonLabel.setText(enemyMon.getName().getValue());
        health = clamp(enemyMon.getHealth().getValue(), 0, enemyMon.getMaxHealth().getValue());
        enemyHpBar.setProgress(normalize(health, 0, enemyMon.getMaxHealth().getValue(), 0, 1));

        enemyMonImg.setImage(enemyMon.getSprites().getFirst());
        statusEnemyImg.setImage(enemyMon.getStatus().getSprite());

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

            act.setDisable(attack.getPp().getValue() == 0);
        }

        checkStruggle();


        infoLabel.setText("Player " + game.getPlayerTurn() + "'s turn");
    }

    private void checkStruggle() {
        if (Arrays.stream(game.getActiveMon().getAttacks()).allMatch(attack -> attack.getPp().getValue() == 0)) {
            act1.setManaged(false);
            act2.setManaged(false);
            act3.setManaged(false);
            act4.setManaged(false);

            struggleButton.setManaged(true);
        } else {
            act1.setManaged(true);
            act2.setManaged(true);
            act3.setManaged(true);
            act4.setManaged(true);

            struggleButton.setManaged(false);
        }
    }

    private void battle() {
        game.battle();

        if (game.isOver()) {
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

    private <T extends Number> T clamp(T value, T min, T max) {
        if (value.doubleValue() < min.doubleValue()) {
            return min;
        } else if (value.doubleValue() > max.doubleValue()) {
            return max;
        }
        return value;
    }

    private double normalize(double value, double originalMin, double originalMax, double newMin, double newMax) {
        double normalized = (value - originalMin) / (originalMax - originalMin);
        return newMin + normalized * (newMax - newMin);
    }
}
