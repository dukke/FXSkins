module com.pixelduke.samples.fxskins {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.graphics;

    requires org.controlsfx.controls;

    requires com.pixelduke.fxskins;

    exports com.pixelduke.samples.control.skin to javafx.graphics;
    exports com.pixelduke.samples.control.skin.controllers to javafx.fxml;
    opens com.pixelduke.samples.control.skin.controllers to javafx.fxml;
    opens com.pixelduke.samples.control.skin to javafx.fxml;
}