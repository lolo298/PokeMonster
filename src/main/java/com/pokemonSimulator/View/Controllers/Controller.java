package com.pokemonSimulator.View.Controllers;

import com.pokemonSimulator.View.MainController;

public class Controller {
    protected static MainController mainController;

    public static void setMainController(MainController mainController) {
        Controller.mainController = mainController;
    }
}