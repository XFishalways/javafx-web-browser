package com.browser.controller;

import com.browser.Application.Main;
import com.browser.view.Menu;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final String BLANK = "_blank";
    private static final String TARGET = "target";
    private static final String CLICK = "click";
    public static TabPane tabPane = new TabPane();
    public Menu menu = new Menu();
    public VBox drawerPane = new VBox();
    public BorderPane root;
    private static Tab firstTab = new Tab("New Tab");
    private static final Tab addNewTab = new Tab("+");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addNewTab.setClosable(false);
        addNewTab.setId("addNewTab");
        tabPane.setId("tab added");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        try {
            firstTab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "Tab.fxml"))));
            firstTab.setText("Baidu");
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        tabPane.getTabs().addAll(firstTab, addNewTab);
        root.setCenter(tabPane);

        setTabPaneView(tabPane, addNewTab);
        tabPaneChangeListener(tabPane);
    }

    public void tabPaneChangeListener(TabPane tabpane) {
        tabpane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab newSelectedTab) {
                menu.hideHamburgerPane();
            }
        });
    }

    public void setTabPaneView(TabPane tabpane, Tab addNewTab) {
        tabpane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab newSelectedTab) {

                if (tabPane.getTabs().size() == 1) {
                    Platform.exit();
                }

                com.browser.Application.Main.getStage().setTitle(tabPane.getSelectionModel().getSelectedItem().getText());

                if (newSelectedTab == addNewTab) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                creatNewTab(tabpane, addNewTab);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });
    }

    public void setFirstTab(Tab firstTab) {
        this.firstTab = firstTab;
    }

    public Tab getFirstTab() {
        return firstTab;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public Tab getAddNewTab() {
        return addNewTab;
    }

    public void creatNewTab(TabPane tabPane, Tab addNewTab) throws IOException {
        Tab tab = new Tab("New tab");
        tab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "Tab.fxml"))));
        tab.getStyleClass().addAll("tab-pane");

        ObservableList<Tab> tabs = tabPane.getTabs();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                tabs.add(tabs.size() - 1, tab);

                SingleSelectionModel<Tab> selectedTab = tabPane.getSelectionModel();
                selectedTab.select(tab);

            }
        });

    }

}
