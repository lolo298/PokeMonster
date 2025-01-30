package com.pokemonSimulator.Utils.Values.enums;

public enum Screens {
    MAIN("MainView.fxml"),
    SELECT_TEAM("team-selection.fxml"),
    SELECT_ATTACK("select-attack.fxml"),
    BATTLE("battle.fxml"),
    END("end.fxml");

    private final String path;

    Screens(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}