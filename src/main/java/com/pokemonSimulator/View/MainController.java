package com.pokemonSimulator.View;

import com.pokemonSimulator.Utils.Logger;
import com.pokemonSimulator.Utils.Values.enums.Screens;
import com.pokemonSimulator.View.Controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane rootPane;

    @FXML
    void initialize() {
        Controller.setMainController(this);
        switchView(Screens.SELECT_TEAM);
    }

    public void switchView(Screens fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile.toString()));
            Parent newView = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(newView);
        } catch (IOException e) {
            Logger.error("Failed to load view: " + e.getMessage());
            var stack = e.getStackTrace();
            for (int i = 0; i < 15; i++) {
                Logger.error(stack[i].toString());
            }
        }
    }
}
