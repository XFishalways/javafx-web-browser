package com.browser.controller;

import com.browser.Application.Main;
import com.browser.bookmarks.Add;
import com.browser.bookmarks.TableViewSample;
import com.browser.history.HistoryManagement;

import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
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
    public Label past;
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

    ObservableList<WebHistory.Entry> entries = history.getEntries();

    private Add add = new Add();
    private TableViewSample tableViewSample = new TableViewSample();

    private final TableView<TableViewSample.Bookmark> table = new TableView<>();
    private final ObservableList<TableViewSample.Bookmark> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();
    int num = 0;

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

    private boolean isDownloadLink(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String type = conn.getContentType();
        String disposition = conn.getHeaderField("Content-Disposition");
        if (disposition != null && disposition.toLowerCase().contains("attachment;")){
            return true;
        }
        else if (type.toLowerCase().contains("application")){
            return true;
        }
        else{
            return false;
        }
        //return url.endsWith(".zip") || url.endsWith(".rar") || url.endsWith(".exe");
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

        homepage.setGraphic(new ImageView(new Image(Objects.requireNonNull(Objects.requireNonNull(getClass()).getResourceAsStream(Main.IMAGES + "home.png")))));

        past.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Main.IMAGES + "history.png")))));

        // loader
        worker = webEngine.getLoadWorker();
        worker.stateProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Loading state: " + newValue.toString());
            if (newValue == State.SUCCEEDED) {
                System.out.println("Finish!");
                String currentURL = webEngine.getLocation();

                boolean flag = false;
                try{
                    flag = isDownloadLink(currentURL);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if (flag) { // 调用下载
                    try {
                        downloader.addDownloadTask(currentURL);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
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
            String currentURL = webEngine.getLocation();
            boolean flag = false;
            try{
                flag = isDownloadLink(currentURL);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            if (flag) {
                // TODO 调用下载
                try {
                    downloader.addDownloadTask(currentURL);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            pageRender(searchField.getText());
            System.out.println("clicked");

        });

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

            Stage stage = new Stage();
            try {
                tableViewSample.read_from_file();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(new Group());
            stage.setTitle("BookmarkManager");
            stage.setWidth(650);
            stage.setHeight(550);

            final Label label = new Label("Bookmarks");
            label.setFont(new Font("Arial", 20));

            table.setEditable(true);

            TableColumn<TableViewSample.Bookmark, String> titleCol = new TableColumn<>("Title");
            titleCol.setMinWidth(100);
            titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
            titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
            titleCol.setOnEditCommit(
                    (TableColumn.CellEditEvent<TableViewSample.Bookmark, String> t) -> {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setTitle(t.getNewValue());
                    });
            TableColumn<TableViewSample.Bookmark, String> typeCol = new TableColumn<>("type");
            typeCol.setMinWidth(100);
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
            typeCol.setOnEditCommit(
                    (TableColumn.CellEditEvent<TableViewSample.Bookmark, String> t) -> {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setType(t.getNewValue());
                    });
            TableColumn<TableViewSample.Bookmark, String> urlCol = new TableColumn<>("url");
            urlCol.setMinWidth(100);
            urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
            urlCol.setCellFactory(TextFieldTableCell.forTableColumn());
            urlCol.setOnEditCommit(
                    (TableColumn.CellEditEvent<TableViewSample.Bookmark, String> t) -> {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setUrl(t.getNewValue());
                    });
            TableColumn<TableViewSample.Bookmark, String> tipCol = new TableColumn<>("Tip");
            tipCol.setMinWidth(200);
            tipCol.setCellValueFactory(new PropertyValueFactory<>("tip"));
            tipCol.setCellFactory(TextFieldTableCell.forTableColumn());
            tipCol.setOnEditCommit(
                    (TableColumn.CellEditEvent<TableViewSample.Bookmark, String> t) -> {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setTip(t.getNewValue());
                    });

            table.setItems(data);
            table.getColumns().addAll(titleCol,typeCol, urlCol, tipCol);

            final TextField addTitle = new TextField();
            addTitle.setPromptText("Title");
            addTitle.setMaxWidth(titleCol.getPrefWidth());
            final TextField addType = new TextField();
            addType.setMaxWidth(typeCol.getPrefWidth());
            addType.setPromptText("type");
            final TextField addUrl = new TextField();
            addUrl.setMaxWidth(urlCol.getPrefWidth());
            addUrl.setPromptText("url");
            final TextField addTip = new TextField();
            addTip.setMaxWidth(tipCol.getPrefWidth());
            addTip.setPromptText("Tip");

            final Button addButton = new Button("Add");
            addButton.setOnAction((ActionEvent e) -> {

                if (!(Objects.equals(addTip.getText(), "") || Objects.equals(addTitle.getText(), "") || Objects.equals(addUrl.getText(), "") || Objects.equals(addType.getText(), ""))) {
                    num++;
                    data.add(new TableViewSample.Bookmark(
                            addTitle.getText(),
                            addType.getText(),
                            addUrl.getText(),
                            addTip.getText()));
                    addTitle.clear();
                    addType.clear();
                    addUrl.clear();
                    addTip.clear();
                }
            });
            final Button DeleteButton = new Button("Delete");
            DeleteButton.setOnAction((ActionEvent e) -> {
                num--;
                table.getItems().remove(table.selectionModelProperty().getValue().getSelectedIndex());
            });
            hb.getChildren().addAll(addTitle, addType, addUrl, addTip, addButton, DeleteButton);
            hb.setSpacing(3);

            final VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(label, table, hb);

            ((Group) scene.getRoot()).getChildren().addAll(vbox);

            stage.setScene(scene);
            stage.show();
        });

        past.setOnMouseClicked(event -> {
            TableView<WebHistory.Entry> tableView = new TableView<>();
            ObservableList<WebHistory.Entry> data = FXCollections.observableArrayList();

            TableColumn<WebHistory.Entry, String> titleColumn = new TableColumn<>("标题");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            TableColumn<WebHistory.Entry, String> urlColumn = new TableColumn<>("URL");
            urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

            TableColumn<WebHistory.Entry, String> visitedColumn = new TableColumn<>("最近访问");
            visitedColumn.setCellValueFactory(new PropertyValueFactory<>("lastVisitedDate"));

            // 添加新的历史记录条目
            data.addAll(entries);

            tableView.getColumns().addAll(titleColumn, urlColumn, visitedColumn);
            tableView.setItems(data);

            VBox layout = new VBox(tableView);
            Scene scene = new Scene(layout);

            Stage stage = new Stage();
            stage.setTitle("历史记录");
            stage.setScene(scene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();

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
