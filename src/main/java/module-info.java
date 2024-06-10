// Followed this file almost exactly:
// https://github.com/openjfx/samples/blob/master/IDE/IntelliJ/Modular/Maven/hellofx/src/main/java/module-info.java
module fifteenpuzzleapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens skean.sam.cs342.fifteenpuzzleapp to javafx.fxml;
    exports skean.sam.cs342.fifteenpuzzleapp;
}