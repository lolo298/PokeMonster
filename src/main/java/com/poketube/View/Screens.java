package com.poketube.View;

public enum Screens {
    MAIN("MainView.fxml"),
    SELECT_TEAM("team-selection.fxml"),
    SELECT_ATTACK("select-attack.fxml"),
    BATTLE("battle.fxml");

    private final String path;

    Screens(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}