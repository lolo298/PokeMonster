module com.poketube {
    requires javafx.controls;
    requires javafx.fxml;

    requires jdk.compiler;

    opens com.poketube to javafx.fxml;
    opens com.poketube.View to javafx.fxml;
    exports com.poketube;
    exports com.poketube.View;
    exports com.poketube.View.Controllers;
    opens com.poketube.View.Controllers to javafx.fxml;
}