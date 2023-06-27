package com.browser.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawer.DrawerDirection;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;

import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Ham {

    public MenuView menuView = new MenuView();
    public JFXDrawersStack drawersStack = new JFXDrawersStack();
    public JFXDrawer rightDrawer = new JFXDrawer();
    Label history = new Label();
    Label downloads = new Label();
    Label bookmarks = new Label();
    Label setting = new Label();

    public JFXHamburger getHamburger(JFXHamburger hamburger , BorderPane borderpane , TabPane settingTabPane) {

        history.setMinSize(40, 40);
        history.setGraphic(new ImageView(new Image(com.browser.Application.Main.IMAGES + "history.png")));
        history.setTooltip(new Tooltip("History"));

        downloads.setMinSize(40, 40);
        downloads.setGraphic(new ImageView(new Image(com.browser.Application.Main.IMAGES + "downloads.png")));
        downloads.setTooltip(new Tooltip("Downloads"));

        bookmarks.setMinSize(40, 40);
        bookmarks.setGraphic(new ImageView(new Image(com.browser.Application.Main.IMAGES + "bookmarks.png")));
        bookmarks.setTooltip(new Tooltip("Bookmarks"));

        setting.setMinSize(40, 40);
        setting.setGraphic(new ImageView(new Image(com.browser.Application.Main.IMAGES + "settings.png")));
        setting.setTooltip(new Tooltip("Setting"));


        VBox vbox = new VBox();
        vbox.getChildren().addAll(history, downloads, bookmarks, setting);
        vbox.setSpacing(25);
        vbox.setId("rightDrawerVbox");

        rightDrawer.setDirection(DrawerDirection.RIGHT);
        rightDrawer.setDefaultDrawerSize(80);
        rightDrawer.setSidePane(vbox);
        rightDrawer.setOpacity(0.5);

        // Hamburger

        borderpane.setRight(drawersStack);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            showHamburgerPane();
        });

        menuView.setMenuViewListener(history, downloads, bookmarks, setting, settingTabPane, drawersStack, rightDrawer);
        // setting.getStyleClass().addAll("animated-option-button","animated-option-sub-button2");
        setHistory(history);
        return hamburger;
    }

    public void showHamburgerPane() {
        drawersStack.toggle(rightDrawer);
    }

    public void hideHamburgerPane() {

        drawersStack.toggle(rightDrawer, false);
    }

    public void setHistory(Label history) {
        this.history = history;
    }

    public Label getHistory() {
        return history;
    }

    public void setDownloads(Label downloads) {
        this.downloads = downloads;
    }
    public Label getDownloads() {
        return downloads;
    }
}