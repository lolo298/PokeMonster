package com.poketube.View;

//public class Screens {
//    public static final String MAIN = "MainView.fxml";
//    public static final String SELECT_TEAM = "team-selection.fxml";
//    public static final String ORDER_TEAM = "team-order.fxml";
//
//}

public enum Screens {
    MAIN("MainView.fxml"),
    SELECT_TEAM("team-selection.fxml"),
    ORDER_TEAM("team-order.fxml");

    private final String path;

    Screens(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path;
    }
}