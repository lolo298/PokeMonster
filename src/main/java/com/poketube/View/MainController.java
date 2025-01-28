package com.poketube.View;

import com.poketube.Utils.Logger;
import com.poketube.View.Controllers.IController;
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
        switchView(Screens.SELECT_TEAM);
    }

    public void switchView(Screens fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile.toString()));
            Parent newView = loader.load();
            IController controller = loader.getController();
            controller.setMainController(this);


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

    @FXML
    public void go() {
        switchView(Screens.TEST);
    }

}
