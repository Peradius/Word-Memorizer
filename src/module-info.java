module Projekty {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens sample;
    opens sample.Data;
    opens sample.GUI.Controllers;

    exports sample.GUI.Controllers to javafx.fxml;
    exports sample.Data to javafx.fxml;
}