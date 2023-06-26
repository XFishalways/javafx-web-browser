package com.browser.controller;

import com.browser.Application.Main;
import com.browser.history.HistoryManagement;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.browser.controller.MainController.tabPane;
import com.browser.history.*;

import com.browser.view.*;
import org.controlsfx.control.textfield.TextFields;

public class TabController implements Initializable {

    public Label download;
    public JFXProgressBar progressbar;
    @FXML
    private Label back;

    @FXML
    private Label bookmark;

    @FXML
    private BorderPane borderpane;

    @FXML
    private Label forward;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private Label homepage;

    @FXML
    private GridPane navigationBar;

    @FXML
    private Label reload;

    @FXML
    private Label search;

    @FXML
    private TextField searchField;

    @FXML
    private Label setting;


    public WebView browser = new WebView();
    public WebEngine webEngine = browser.getEngine();
    private WebHistory history = webEngine.getHistory();

    public Worker<Void> worker;
    private final Ham ham = new Ham();

    static WebEngine engine;

    public void setWebEngine(WebEngine webEngine) {
        engine = webEngine;
    }

    public static WebEngine getWebEngine() {
        return engine;
    }

    public void setBookmark(Label bookmark) {
        this.bookmark = bookmark;
    }

    public Label getBookmark() {
        return bookmark;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        setWebEngine(webEngine);

        searchField.setText("https://www.baidu.com");
        pageRender(searchField.getText());

        back.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "arrow-left.svg")))));
        back.setDisable(true);

        forward.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "arrow-right.svg")))));
        forward.setDisable(true);

        reload.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "refresh.svg")))));

        search.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "search.svg")))));
        search.setId("search");

        download.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "downloads.svg")))));

        bookmark.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "bookmarks.svg")))));

        // Worker load the page
        worker = webEngine.getLoadWorker();
        worker.stateProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Loading state: " + newValue.toString());
            if (newValue == State.SUCCEEDED) {
                System.out.println("Finish!");

                // The current url title set in current tab
                tabPane.getSelectionModel().getSelectedItem().setText(webEngine.getTitle());

                org.w3c.dom.Document doc = webEngine.getDocument();

            }

        });

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("location of engine: " + newValue);
            // TODO 下载入口！！！！！

        });

        progressbar.progressProperty().bind(worker.progressProperty());


        ham.getHamburger(hamburger, borderpane, tabPane);

//        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
//
//            pageRender(searchField.getText());
//
//        });

        // Search Field Listener

        searchField.setOnKeyPressed(event -> {

            Pattern p = Pattern.compile("[a-z]*[ ]*[A-Z]*[ ]*[0-9]*[ ]");
            Matcher m = p.matcher(searchField.getText());
            boolean b = m.matches();

            if (event.getCode() == KeyCode.ENTER) {

                if (b) {
                    pageRender("https://www.baidu.com/s?wd=" + searchField.getText()); // method
                    // call
                } else {
                    pageRender(searchField.getText());
                }
            }

            ObservableList<String> domainNames = FXCollections.observableArrayList();
            domainNames = com.browser.history.HistoryManagement.getDomainNames(domainNames);
            String[] array = new String[domainNames.size()];

            for (int a = 0; a < domainNames.size(); a++) {
                array[a] = domainNames.get(a);
            }

            TextFields.bindAutoCompletion(searchField, array);

        });


        back.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            try {

                System.out.println("Max size :" + history.getEntries().size());
                System.out.println("Current index backward: " + history.getCurrentIndex());
                history.go(-1);

            } catch (IndexOutOfBoundsException e1) {
                System.out.println(e1);
            }
        });

        forward.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            try {
                System.out.println("Max size :" + history.getEntries().size());
                System.out.println("Current index forward: " + history.getCurrentIndex());
                history.go(1);

            } catch (IndexOutOfBoundsException e1) {
                System.out.println(e1);
            }
        });

        reload.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            webEngine.reload();
        });

        bookmark.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

        });

        download.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

        });
    }

    private void pageRender(String url) {
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {

            if (newState == State.SUCCEEDED) {

                searchField.setText(webEngine.getLocation());
                Main.getStage().setTitle("CCN Browser");

                if (history.getCurrentIndex() == 0) {
                    back.setDisable(true);
                    forward.setDisable(true);
                    if(history.getEntries().size()>1){
                        forward.setDisable(false);
                    }
                }

                if (history.getCurrentIndex() > 0) {
                    back.setDisable(false);
                    forward.setDisable(false);
                }

                if ((history.getCurrentIndex()+1) == history.getEntries().size()) {
                    forward.setDisable(true);
                }

                URL domain = null;
                if (!(webEngine.getLocation().equals("about:blank")))
                    try {
                        domain = new URL(webEngine.getLocation());
                    } catch (MalformedURLException e) {
                        System.err.println("Invalid domain.");
                    }

                String username  = Main.getUsername();

                assert domain != null;
                HistoryManagement.insertUrl(username, webEngine.getLocation(), domain.getHost(), webEngine.getTitle());
            }
        });

        webEngine.load(url);
        borderpane.setCenter(browser);
    }
}
