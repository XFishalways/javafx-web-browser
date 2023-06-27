package com.browser.controller;

import com.browser.Application.Main;
import com.browser.history.HistoryManagement;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.browser.controller.MainController.tabPane;
import static com.browser.controller.MainController.downloader;
import com.browser.history.*;

import com.browser.view.*;
import org.controlsfx.control.textfield.TextFields;

public class TabController implements Initializable {
    @FXML
    public Label download;
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

    BooleanProperty backAvailable = new SimpleBooleanProperty(false);
    BooleanProperty forwardAvailable = new SimpleBooleanProperty(false);

//    Bindings.bindBidirectional(backAvailable, history.currentIndexProperty(), index -> index.intValue() > 0);
//    Bindings.bindBidirectional(forwardAvailable, history.currentIndexProperty(), index -> index.intValue() < history.getEntries().size() - 1);
//    backButton.setDisable(!backAvailable.get());
//    forwardButton.setDisable(!forwardAvailable.get());

    public Worker<Void> worker;
    private final Ham ham = new Ham();

    static WebEngine engine;
    WebHistory webHistory = webEngine.getHistory();

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

    private boolean isDownloadLink(String url) {

        return url.endsWith(".zip") || url.endsWith(".rar") || url.endsWith(".exe");
    }

    private int currentIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        setWebEngine(webEngine);

        searchField.setText("https://www.baidu.com");
        pageRender(searchField.getText());

        searchField.setEditable(true);

        back.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "arrow-left.png")))));
        back.setDisable(true);

        forward.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "arrow-right.png")))));
        forward.setDisable(true);

        reload.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "refresh.png")))));

        search.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "search.png")))));
        search.setId("search");

        download.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "downloads.png")))));

        bookmark.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "bookmarks.png")))));

        setting.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "settings.png")))));

        homepage.setGraphic(new ImageView(new Image(Objects.requireNonNull(Objects.requireNonNull(getClass()).getResourceAsStream(Main.IMAGES + "home.png")))));

        // loader
        worker = webEngine.getLoadWorker();
        worker.stateProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Loading state: " + newValue.toString());
            if (newValue == State.SUCCEEDED) {
                System.out.println("Finish!");

                String currentURL = webEngine.getLocation();
                if (isDownloadLink(currentURL)) {
                    // TODO 调用下载
                } else {
                    // 正常打开网页
                    tabPane.getSelectionModel().getSelectedItem().setText(webEngine.getTitle());
                    searchField.setText(currentURL);
                }
            }
        });

        history.currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentIndex = newValue.intValue();

//            back.setVisible(currentIndex > 0);
            back.setDisable(currentIndex <= 0);
//            forward.setVisible(currentIndex < history.getEntries().size() - 1);
            forward.setDisable(currentIndex >= history.getEntries().size() - 1);
        });

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("location of engine: " + newValue);
            // TODO 下载入口！！！！！

        });

        ham.getHamburger(hamburger, borderpane, tabPane);

        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            pageRender(searchField.getText());
            System.out.println("clicked");

        });

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


        back.setOnMouseClicked(event -> {
            try {
                System.out.println("Max size :" + history.getEntries().size());
                System.out.println("Current index backward: " + history.getCurrentIndex());
                history.go(-1);

            } catch (IndexOutOfBoundsException e1) {
                System.out.println(e1);
            }
        });

        forward.setOnMouseClicked(event -> {
            try {
                System.out.println("Max size :" + history.getEntries().size());
                System.out.println("Current index forward: " + history.getCurrentIndex());
                history.go(1);

            } catch (IndexOutOfBoundsException e1) {
                System.out.println(e1);
            }
        });

        reload.setOnMouseClicked(event -> {
            webEngine.reload();
        });

        bookmark.setOnMouseClicked(event -> {

        });

        download.setOnMouseClicked(event -> {
            try {
                Tab tab = new Tab("Download Page");
                tab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "Downloads.fxml"))));
                tab.getStyleClass().addAll("tab-pane");

                ObservableList<Tab> tabs = tabPane.getTabs();

                Platform.runLater(() -> {

                    tabs.add(tabs.size() - 1, tab);

                    SingleSelectionModel<Tab> selectedTab = tabPane.getSelectionModel();
                    selectedTab.select(tab);
                });
            }
            catch (IOException e){
                System.err.println(e.toString());
            }

        });

        homepage.setOnMouseClicked(event -> {

            searchField.setText("https://www.baidu.com");
            pageRender(searchField.getText());
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

                assert domain != null;
                HistoryManagement.insertUrl(webEngine.getLocation(), domain.getHost(), webEngine.getTitle());
            }
        });

        webEngine.load(url);
        borderpane.setCenter(browser);
    }

}
