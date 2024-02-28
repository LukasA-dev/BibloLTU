package com.example.bibloltu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class RunSystem extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunSystem.class.getResource("BibloLtuScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1083, 579);
        stage.initStyle(StageStyle.DECORATED );
        stage.setResizable(false);
        stage.setTitle("BibloLTU!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}