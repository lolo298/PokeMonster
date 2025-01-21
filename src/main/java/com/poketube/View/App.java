package com.poketube.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        var main = App.class.getResource(Screens.MAIN.toString());
        FXMLLoader fxmlLoader = new FXMLLoader(main);
        Scene scene = new Scene(fxmlLoader.load(), 640, 240);
        stage.setTitle("Poketube");
        stage.setScene(scene);
        stage.show();
    }

    public static void startWindow() {
        launch();
    }
}