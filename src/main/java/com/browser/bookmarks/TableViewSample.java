package com.browser.bookmarks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class TableViewSample {

    private final TableView<URLdetails> table = new TableView<>();
    private final ObservableList<URLdetails> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();

    private TreeTableView<String> treeView  = new TreeTableView<>();
    int num = 0;

    public TableViewSample() throws FileNotFoundException {
        read_from_file();

        table.setEditable(true);

        TableColumn<URLdetails, String> titleCol = new TableColumn<>("Title");
        titleCol.setMinWidth(100);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(
                (CellEditEvent<URLdetails, String> t) -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setTitle(t.getNewValue()));
        TableColumn<URLdetails, String> typeCol = new TableColumn<>("type");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit(
                (CellEditEvent<URLdetails, String> t) -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setType(t.getNewValue()));
        TableColumn<URLdetails, String> urlCol = new TableColumn<>("url");
        urlCol.setMinWidth(100);
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlCol.setCellFactory(TextFieldTableCell.forTableColumn());
        urlCol.setOnEditCommit(
                (CellEditEvent<URLdetails, String> t) -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setUrl(t.getNewValue()));
        TableColumn<URLdetails, String> tipCol = new TableColumn<>("Tip");
        tipCol.setMinWidth(200);
        tipCol.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tipCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tipCol.setOnEditCommit(
                (CellEditEvent<URLdetails, String> t) -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setTip(t.getNewValue()));

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
                data.add(new URLdetails(
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

    }
    public void stop() throws IOException {
        write_to_file();
    }
    public void read_from_file() throws FileNotFoundException {
        File file = new File("D:\\JavaProjects\\browser\\src\\main\\resources\\cache\\test.txt");
        if(!file.exists()) {
            System.out.println("文件未找到");
            System.exit(0);
        }
        Scanner s = new Scanner(file);
        num = s.nextInt();
        for(int i=0;i<num;i++)
            data.add(new URLdetails(s.next(),s.next(),s.next(),s.next()));
    }

    public void write_to_file() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:\\JavaProjects\\browser\\src\\main\\resources\\cache\\test.txt"));
        URLdetails savedData;
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
        bufferedWriter.close();
    }

    public Tab getBookmarkView(Tab bookmarkTab) {
        BorderPane borderPaneBookmark = new BorderPane();
        treeView.setMinWidth(150);
        treeView.setMaxWidth(150);
        borderPaneBookmark.setLeft(treeView);
        borderPaneBookmark.setCenter(table);
        bookmarkTab.setContent(borderPaneBookmark);
        return bookmarkTab;
    }
}