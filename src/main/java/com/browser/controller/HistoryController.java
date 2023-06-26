package com.browser.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HistoryController {
    public Label selectedDeleteEntries;
    public JFXButton btn;
    public JFXTextField search;
    public JFXButton deleteSingle;
    public JFXButton search1;
    public PieChart historyPieChart;
    public TreeView treeView;
    public VBox paneForChart;
    public BorderPane borderPaneHistory;
    public JFXTreeTableView table;

    public void deleteHistory(MouseEvent mouseEvent) {
    }

    public void SearchDataInTable(MouseEvent mouseEvent) {
    }
}
