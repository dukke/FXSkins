package com.pixelduke.samples.control.skin;

import com.pixelduke.control.skin.FXSkins;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScrollPaneSample extends Application {

    private static final String RESOURCE = "ScrollPane Sample.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");

        Parent root = FXMLLoader.load(getClass().getResource(RESOURCE));

        Scene scene = new Scene(root);
        stage.setTitle("ScrollPane Sample");

        scene.getStylesheets().add(FXSkins.getStylesheetURL());
        scene.getStylesheets().add(TextFieldSample.class.getResource("default-skin.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}