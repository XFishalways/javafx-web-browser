package com.browser.view;

import com.browser.test.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Homepage extends Application {

    public static String FXML = "/fxml/";
    public static String CSS = "/css/";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(FXML + "Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Web Browser");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
