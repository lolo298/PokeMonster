package com.pokemonSimulator.View;

import com.pokemonSimulator.Utils.Values.enums.Screens;
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
        Scene scene = new Scene(fxmlLoader.load(), 1664, 936);
//        stage.setMaximized(true);
        stage.setResizable(false);

        stage.setTitle("PokemonSimulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void startWindow() {
        launch();
    }
}