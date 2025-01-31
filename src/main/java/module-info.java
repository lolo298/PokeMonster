module com.pokemonSimulator {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports com.pokemonSimulator;
    exports com.pokemonSimulator.Utils.Values;
    exports com.pokemonSimulator.Utils.Errors;
    exports com.pokemonSimulator.Game.Monsters;
    exports com.pokemonSimulator.Game.Actions;
    exports com.pokemonSimulator.Game.Types;
    exports com.pokemonSimulator.Game.Items;
    exports com.pokemonSimulator.View;
    exports com.pokemonSimulator.View.Controllers;
    exports com.pokemonSimulator.View.Controls;

    opens com.pokemonSimulator to javafx.fxml;
    opens com.pokemonSimulator.View to javafx.fxml;
    opens com.pokemonSimulator.View.Controllers to javafx.fxml;
    opens com.pokemonSimulator.View.Controls to javafx.fxml;
    exports com.pokemonSimulator.Utils.Values.enums;
    opens com.pokemonSimulator.Utils.Values.enums to javafx.fxml;
    exports com.pokemonSimulator.Utils.Values.Interfaces;
    opens com.pokemonSimulator.Utils.Values.Interfaces to javafx.fxml;
}