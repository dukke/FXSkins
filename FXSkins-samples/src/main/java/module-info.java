module com.pixelduke.samples.fxskins {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.graphics;

    requires com.pixelduke.fxskins;

    exports com.pixelduke.samples.control.skin to javafx.graphics;
    opens com.pixelduke.samples.control.skin to javafx.fxml;
}