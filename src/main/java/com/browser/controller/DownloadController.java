package com.browser.controller;

import com.browser.downloader.Downloader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.browser.controller.MainController.downloader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DownloadController implements Initializable {
    @FXML
    private VBox container;

    private Timeline timeline;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            refreshProgress(); // 刷新进度条
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play(); // 启动定时器s

//        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
//            ListMission(); // 刷新页面
//        }));
//        timeline2.setCycleCount(Timeline.INDEFINITE);
//        timeline2.play(); // 启动定时器s

        ListMission();
    }

    private void ListMission() {
        int tot = downloader.getTotal();

        List<Downloader> MissionList = downloader.getMissionList();

        for (int i = 0; i < tot; i++) {

            Downloader tmpMission = MissionList.get(i);

            HBox row = new HBox(10);

            //Label downloadID = new Label(Integer.toString(id));
            Label fileName = new Label(tmpMission.getFileName());
            //Label currentSpeed = new Label(tmpMission.getReadableSpeed());
            Label fileSize = new Label(Long.toString( tmpMission.getFileSize()));
            fileName.setMinWidth(200);
            fileSize.setMinWidth(100);
            ProgressBar progressBar = new ProgressBar(0);
            progressBar.setMinWidth(400);
            Button startButton = new Button("Start");
            Button pauseButton = new Button("Pause");
            Button cancelButton = new Button("Cancel");

//            startButton.setOnAction(event -> {
//                downloader.start();
//            });
//
//            pauseButton.setOnAction(event -> {
//                downloader.pause(id);
//            });
//
//            cancelButton.setOnAction(event -> {
//                downloader.cancel(id);
//            });

            row.getChildren().addAll(fileName, fileSize, progressBar, startButton, pauseButton, cancelButton);
            container.getChildren().add(row);
        }
    }

    private void refreshProgress() {
        int tot = downloader.getTotal();

        List<Downloader> MissionList = downloader.getMissionList();

        for (int i = 0; i < tot; i++) {
            Downloader tmpMission = MissionList.get(i);

            ProgressBar progressBar = (ProgressBar) ((HBox)container.getChildren().get(i)).getChildren().get(2);
            progressBar.setProgress(tmpMission.getProgress());
        }
    }
}
