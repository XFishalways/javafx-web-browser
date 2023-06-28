package com.browser.bookmarks;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class TableViewSample extends Application{

    private final TableView<Bookmark> table = new TableView<>();
    private final ObservableList<Bookmark> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();
    int num = 0;

    public void start(Stage stage) throws FileNotFoundException {
        read_from_file();
        Scene scene = new Scene(new Group());
        stage.setTitle("BookmarkManager");
        stage.setWidth(650);
        stage.setHeight(550);

        final Label label = new Label("Bookmarks");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn<Bookmark, String> titleCol = new TableColumn<>("Title");
        titleCol.setMinWidth(100);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(
                (CellEditEvent<Bookmark, String> t) -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setTitle(t.getNewValue());
                });
        TableColumn<Bookmark, String> typeCol = new TableColumn<>("type");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit(
                (CellEditEvent<Bookmark, String> t) -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setType(t.getNewValue());
                });
        TableColumn<Bookmark, String> urlCol = new TableColumn<>("url");
        urlCol.setMinWidth(100);
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlCol.setCellFactory(TextFieldTableCell.forTableColumn());
        urlCol.setOnEditCommit(
                (CellEditEvent<Bookmark, String> t) -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setUrl(t.getNewValue());
                });
        TableColumn<Bookmark, String> tipCol = new TableColumn<>("Tip");
        tipCol.setMinWidth(200);
        tipCol.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tipCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tipCol.setOnEditCommit(
                (CellEditEvent<Bookmark, String> t) -> {
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
                data.add(new Bookmark(
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
    }
    public void stop() throws IOException {
        write_to_file();
    }

    /**
     * 将书签修改写入本地文件
     * @throws FileNotFoundException
     */
    public void read_from_file() throws FileNotFoundException {
        File file = new File("C:\\Users\\ThinkPad\\Desktop\\bookmark.txt");
        if(!file.exists()) {
            System.out.println("文件未找到");
            System.exit(0);
        }
        Scanner s = new Scanner(file);
        num = s.nextInt();
        for(int i=0;i<num;i++)
            data.add(new Bookmark(s.next(),s.next(),s.next(),s.next()));
    }

    public void write_to_file() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\ThinkPad\\Desktop\\bookmark.txt"));
        Bookmark savedData;
        List <List<String>> arrList = new ArrayList<>();
        bufferedWriter.write(String.valueOf(num));
        for(int i = 0; i < table.getItems().size(); i++) {
            savedData = table.getItems().get(i);
            arrList.add(new ArrayList<>());
            arrList.get(i).add(" "+savedData.getTitle());
            arrList.get(i).add(" "+savedData.getType());
            arrList.get(i).add(" "+savedData.getUrl());
            arrList.get(i).add(" "+savedData.getTip());
        }

        for (List<String> strings : arrList) {
            for (String string : strings) {
                bufferedWriter.write(string);
            }
        }
        bufferedWriter.close();  //关闭输入流
    }
    public static class Bookmark {

        private final SimpleStringProperty Title;
        private final SimpleStringProperty type;
        private final SimpleStringProperty url;
        private final SimpleStringProperty tip;

        public Bookmark(String title, String type, String url, String tip) {
            this.Title = new SimpleStringProperty(title);
            this.type = new SimpleStringProperty(type);
            this.url = new SimpleStringProperty(url);
            this.tip = new SimpleStringProperty(tip);
        }

        public String getTitle() {
            return Title.get();
        }

        public void setTitle(String title) {
            Title.set(title);
        }

        public String getType() {
            return type.get();
        }

        public void setType(String title) { type.set(title); }

        public String getUrl() {
            return url.get();
        }

        public void setUrl(String title) {
            url.set(title);
        }

        public String getTip() {
            return tip.get();
        }

        public void setTip(String title) {
            tip.set(title);
        }
    }

}