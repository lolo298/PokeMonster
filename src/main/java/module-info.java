module com.pokemonSimulator {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports com.pokemonSimulator;
    exports com.pokemonSimulator.View;
    exports com.pokemonSimulator.View.Controllers;
    exports com.pokemonSimulator.View.Controls;
    opens com.pokemonSimulator to javafx.fxml;
    opens com.pokemonSimulator.View to javafx.fxml;
    opens com.pokemonSimulator.View.Controllers to javafx.fxml;
    opens com.pokemonSimulator.View.Controls to javafx.fxml;
}