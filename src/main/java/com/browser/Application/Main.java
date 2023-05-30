package com.browser.Application;

import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import com.browser.controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Segp-Group 3
 */
public class Main extends Application {

    /**
     * Holds a reference to the resource folder of all fxml files
     */
    public static String FXML = "/fxml/";
    public static String CSS = "/css/";
    public static String IMAGES = "/img/";

    MainController object = new MainController();
    static Stage stage;

    public static Scene sceneCopy;

    private static StackPane pane = new StackPane();

    private static String username;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXML+"MainFXML.fxml"));
        Parent root = loader.load();

        pane.getChildren().add(root);
        Scene scene = new Scene(pane);


        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.P && event.isControlDown()) {
                MainController mainController = new MainController();
                try {
                    mainController.creatNewTab(mainController.getTabPane(), mainController.getAddNewTab());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS + "stylesheet.css")).toExternalForm());
        stage.setTitle("CCN Browser");
        stage.setScene(scene);
        stage.show();

        setStage(stage);
        setScene(scene);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("输入用户名");
        dialog.setHeaderText(null);
        dialog.setContentText("请输入用户名：");
        dialog.showAndWait();
        username = dialog.getResult();
    }

    public void setPane(StackPane pane) {
        this.pane = pane;
    }

    public static StackPane getPane() {
        return pane;
    }

    public static String getUsername() {
        return username;
    }

    private void setScene(Scene scene) {
        sceneCopy = scene;
    }

    public static Scene getScene() {
        return sceneCopy;
    }


    private void setStage(Stage stage) {
        this.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    private static JFXDialogLayout content = new JFXDialogLayout();

    public static void setDialouge(JFXButton applyButton , String heading , String text , Node ob) {

        JFXButton button = applyButton;

        content.setHeading(new Text(heading));
        content.setBody(new Text(text));

        JFXDialog dialogue = new JFXDialog(pane, content, JFXDialog.DialogTransition.CENTER);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (e6) -> {
            dialogue.close();
        });

        content.setActions(ob, button);
        dialogue.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}

