package com.browser.view;

import com.browser.Application.Main;

import com.browser.bookmarks.TableViewSample;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class MenuView {

    private Tab tab = new Tab();

    private JFXTabPane fxTabpane = new JFXTabPane();
    private Tab historyTab = new Tab("History");
    private Tab downloadsTab = new Tab("Downloads");
    private Tab bookmarksTab = new Tab("Bookmarks");
    private Tab settingTab = new Tab("Setting");
    private JFXDrawersStack drawersStack;
    private JFXDrawer rightDrawer;
    private ObservableList<Tab> tabs;
    SingleSelectionModel<Tab> selectedTab;
    SingleSelectionModel<Tab> fxSelectedTab ;
    public void setMenuViewListener(JFXButton history, JFXButton downloads, JFXButton bookmarks, JFXButton setting,
                                    TabPane tabPane, JFXDrawersStack drawersStack, JFXDrawer rightDrawer) {

        tabs = tabPane.getTabs();
        selectedTab  = tabPane.getSelectionModel();

        fxSelectedTab = fxTabpane.getSelectionModel();

        try {
            fxTabpane.setTabMinWidth(150);
            fxTabpane.setTabMinHeight(30);
            fxTabpane.setId("settingTabPane");

            historyTab.setText("History");
            historyTab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "History.fxml"))));

            downloadsTab.setText("Downloads");
            downloadsTab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "Downloads.fxml"))));

            bookmarksTab.setText("Bookmarks");
            bookmarksTab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "bookmarks.fxml"))));

            settingTab.setText("Setting");
            settingTab.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Main.FXML + "Setting.fxml"))));

        } catch (IOException e1) {
            System.out.println("Setting tabs fxml files not loading");
        }

        this.drawersStack = drawersStack;
        this.rightDrawer = rightDrawer;


        /*
         * Here we developed a tab and its borderpane for setting we made
         * setting class that design the layout of setting then a single tab for
         * all menus set a Tab and only in one tab all menu can be seen Is this
         * cool? or not give me constructive feedback! Setting is class that
         * will only desinge the layout of setting tab and we are just calling
         * its method getSettingView and give two arguments that is tab and
         * setting pane
         */
        history.setOnAction((e)->{

            onClickHideHamburger();

            addAndSelectNewTab(tabs, selectedTab,fxSelectedTab, 0);

            //	fxSelectedTab.select(0);

            tab.setText("History");

        });


        downloads.setOnAction((e) -> {

            onClickHideHamburger();

            addAndSelectNewTab(tabs, selectedTab, fxSelectedTab , 1);

            //	fxSelectedTab.select(1);

            tab.setText("Downloads");

        });

        bookmarks.setOnAction(event -> {

            onClickHideHamburger();

            addAndSelectNewTab(tabs, selectedTab, fxSelectedTab, 2);

//                fxSelectedTab.select(2);

            tab.setText("Bookmarks");

        });

        setting.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            onClickHideHamburger();

            addAndSelectNewTab(tabs, selectedTab,fxSelectedTab, 3);

            //	fxSelectedTab.select(3);

            tab.setText("Setting");

        });

        tab.setContent(fxTabpane);
        fxTabpane.getTabs().addAll(historyTab, downloadsTab, bookmarksTab, settingTab);

        fxTabpane.getSelectionModel().selectedItemProperty().addListener((ov, t, newSelectedTab) -> {

            if (newSelectedTab == historyTab) {
                tab.setText("History");
            }
            if (newSelectedTab == bookmarksTab) {
                tab.setText("Bookmarks");
            }
            if (newSelectedTab == downloadsTab) {
                tab.setText("Downloads");
            }
            if (newSelectedTab == settingTab) {
                tab.setText("Setting");
            }
        });
    }


    private void addAndSelectNewTab(ObservableList<Tab> tabs, SingleSelectionModel<Tab> selectedTab, SingleSelectionModel<Tab> fxSelectedTab
            , int selectedTabIndex) {
        Platform.runLater(() -> {
            for(int i = 0; i < tabs.size(); i++){

                String openTabName = tabs.get(i).getText();

                if(openTabName.equals("History") 		||
                        openTabName.equals("Bookmarks") ||
                        openTabName.equals("Downloads") ||
                        openTabName.equals("Setting"))
                {
                    System.out.println("Tab index:"+ selectedTabIndex);
                    selectedTab.select(i);
                    return;
                }
            }
            fxSelectedTab.select(selectedTabIndex);
            tabs.add(tabs.size() - 1, tab);
            selectedTab.select(tab);

            try {
                getBookMarkView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void getBookMarkView() throws FileNotFoundException {
        TableViewSample tableViewSample = new TableViewSample();
        bookmarksTab = tableViewSample.getBookmarkView(bookmarksTab);
    }

    public void onClickHideHamburger() {

        drawersStack.toggle(rightDrawer);
    }
}
