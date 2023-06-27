package com.teamb13.teamcreater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Launch extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launch.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1124, 725);
        stage.setTitle("Team Create");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        try {
            Files.delete(Path.of("temp.csv"));
        } catch (Exception ignore) { }
    }

    public static void main(String[] args) {
        launch();
    }
}
