package com.browser.controller;

import com.browser.downloader.MainDownloader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadController implements Initializable {
    @FXML
    private VBox container;
    private int count = 0;

    private MainDownloader downloader = new MainDownloader();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void addMission(String url,String FilePath,String FileName) {
        count++;
        HBox row = new HBox(10);

        try{
            downloader.addDownloadTask(url, FilePath, FileName);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        Label fileName = new Label("File " + count);
        ProgressBar progressBar = new ProgressBar(0);
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button cancelButton = new Button("Cancel");

        row.getChildren().addAll(fileName, progressBar, startButton, pauseButton, cancelButton);
        container.getChildren().add(row);
    }
}
