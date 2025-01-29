package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Game;
import com.pokemonSimulator.Game.Monsters.Attack;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.Errors.InvalidSprite;
import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.View.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.net.URISyntaxException;
import java.util.Arrays;

public class BattleController implements IController {
    private MainController mainController;
    private Game game;

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
    private TextField debugWidth;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void initialize() {
        System.out.println("BattleController initialized");
        this.game = PokemonSimulator.getInstance().startGame();

        var formatter = new TextFormatter<>(new NumberStringConverter());
        debugWidth.setTextFormatter(formatter);



//        activeMonImg.layoutYProperty().bind(formatter.valueProperty());


        try {
//            battleMonPane1.layoutXProperty().bind(battleBackgroundMon1.layoutXProperty().add(50));
//            battleMonPane1.layoutYProperty().bind(battleBackgroundMon1.layoutYProperty().add(50));

//            battleMonPane2.layoutXProperty().add(825);
//            battleMonPane2.layoutYProperty().bind(battleBackgroundMon2.layoutYProperty().multiply(2));




        } catch (InvalidSprite e) {
            Logger.warn(e.toString());
        }

        loadBattleView();

        //Init actions events


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
        game.setActivePlayerAttack(attack);

        Logger.warn("Player " + game.getPlayerTurn());
        if (game.getPlayerTurn() == 2) {
            game.battle();
        }
        game.nextPlayer();
        loadBattleView();
    }

    private void loadBattleView() {
        BattleMon activeMon = game.getActiveMon();
        BattleMon enemyMon = game.getEnemyMon();

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
        System.out.println(Arrays.toString(attacks));
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

            System.out.println("Setting attack " + i + " to " + attack.getName().getValue());
            assert act != null && actTooltip != null;
            act.setText(attack.getName().getValue());
            actTooltip.setText(String.format("%d/%d", attack.getPp().getValue(), attack.getBasePp().getValue()));
            actTooltip.setShowDelay(Duration.millis(500));

        }


    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double normalize(double value, double originalMin, double originalMax, double newMin, double newMax) {
        double normalized = (value - originalMin) / (originalMax - originalMin);
        return newMin + normalized * (newMax - newMin);
    }
}
