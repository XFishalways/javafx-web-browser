package com.browser.controller;

import com.browser.Application.Main;
import com.browser.history.History;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.concurrent.Worker.State;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class TabController implements Initializable {

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
                History.insertUrl(username, webEngine.getLocation(), domain.getHost(), webEngine.getTitle());
            }
        });

        webEngine.load(url);
    }
}
