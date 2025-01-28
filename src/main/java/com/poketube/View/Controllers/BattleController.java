package com.poketube.View.Controllers;

import com.poketube.Game.Game;
import com.poketube.Game.Monsters.Attack;
import com.poketube.Game.Monsters.BattleMon;
import com.poketube.Game.Poketube;
import com.poketube.Utils.Logger;
import com.poketube.View.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Arrays;

public class BattleController implements IController {
    private MainController mainController;
    private Game game;

    @FXML
    private Label activeMonLabel;
    @FXML
    private ImageView activeMonImg;
    @FXML
    private Label activeHpLabel;
    @FXML
    private Label enemyMonlabel;
    @FXML
    private ImageView enemyMonImg;
    @FXML
    private Label enemyHpLabel;

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
    private ListView<String> switchMonList;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void initialize() {
        System.out.println("BattleController initialized");
        this.game = Poketube.getInstance().startGame();

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
        activeHpLabel.setText(String.format("HP: %d/%d", activeMon.getHealth().getValue(), activeMon.getMaxHealth().getValue()));

        try {
            activeMonImg.setImage(activeMon.getSprite().getSecond());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        enemyMonlabel.setText(enemyMon.getName().getValue());
        enemyHpLabel.setText(String.format("HP: %d/%d", enemyMon.getHealth().getValue(), enemyMon.getMaxHealth().getValue()));

        try {
            enemyMonImg.setImage(enemyMon.getSprite().getFirst());
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

            System.out.println("Setting attack " + i + " to " + attack.getName().getValue());
            assert act != null && actTooltip != null;
            act.setText(attack.getName().getValue());
            actTooltip.setText(String.format("%d/%d", attack.getPp().getValue(), attack.getBasePp().getValue()));
            actTooltip.setShowDelay(Duration.millis(500));

        }


    }
}
