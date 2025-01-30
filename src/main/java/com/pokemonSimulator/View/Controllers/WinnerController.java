package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.Game.Game;
import com.pokemonSimulator.Game.Monsters.BattleMon;
import com.pokemonSimulator.Game.PokemonSimulator;
import com.pokemonSimulator.Utils.SpriteOrientation;
import com.pokemonSimulator.Utils.Values.SpritesType;
import com.pokemonSimulator.View.Screens;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class WinnerController extends Controller {

    @FXML
    private Button exitButton;
    @FXML
    private HBox teamBox;
    @FXML
    private Label winnerLabel;

    @FXML
    void initialize() {
        Game game = PokemonSimulator.game;

        winnerLabel.setText("Player " + game.getWinner() + " wins!");
        for (BattleMon mon : game.getWinnerTeam()) {
            ImageView img = new ImageView(mon.getSprite(SpriteOrientation.FRONT, SpritesType.ANIMATED));
            img.setFitHeight(100);
            img.setFitWidth(100);
            teamBox.getChildren().add(img);
        }

    }

    @FXML
    void onExit() {
        PokemonSimulator.reset();
        mainController.switchView(Screens.SELECT_TEAM);
    }
}
