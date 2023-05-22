package com.browser.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = getClass().getResource("com/browser/test/test.fxml");
        Objects.requireNonNull(url, "Resource not found: test.fxml");
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("WebBrowser demo");
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

