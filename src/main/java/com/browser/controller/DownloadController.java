package com.browser.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import static com.browser.controller.MainController.downloader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DownloadController implements Initializable {
    @FXML
    private VBox container;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            refreshProgress(); // 刷新进度条
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play(); // 启动定时器s

        ListMission();
    }

    private void ListMission() {
        int tot = downloader.getTotal();

        List<Integer> IdList = downloader.getIdList();

        for (int i = 0; i < tot; i++) {

            int id = IdList.get(i);
            DownloadMission tmpMission = downloader.getMission(id);

            HBox row = new HBox(10);

            Label downloadID = new Label(Integer.toString(id));
            Label fileName = new Label(tmpMission.getSaveName());
            Label currentSpeed = new Label(tmpMission.getReadableSpeed());
            Label fileSize = new Label(tmpMission.getReadableSize());
            ProgressBar progressBar = new ProgressBar(0);
            Button startButton = new Button("Start");
            Button pauseButton = new Button("Pause");
            Button cancelButton = new Button("Cancel");

            startButton.setOnAction(event -> {
                downloader.start();
            });

            pauseButton.setOnAction(event -> {
                downloader.pause(id);
            });

            cancelButton.setOnAction(event -> {
                downloader.cancel(id);
            });

            row.getChildren().addAll(downloadID, fileName, progressBar, startButton, pauseButton, cancelButton);
            container.getChildren().add(row);
        }
    }

    private void refreshProgress() {
        int tot = downloader.getTotal();

        List<Integer> IdList = downloader.getIdList();

        for (int i = 0; i < tot; i++) {
            int id = IdList.get(i);
            DownloadMission tmpMission = downloader.getMission(id);

            ProgressBar progressBar = (ProgressBar) ((HBox)container.getChildren().get(i)).getChildren().get(i);
            progressBar.setProgress((double) tmpMission.getDownloadedSize()/ (double) tmpMission.getFileSize());
        }
    }
}
