package com._17od.upm.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class EscapeDialog extends Application {

    private static final long serialVersionUID = 1L;

    public EscapeDialog(Frame frame, String title, boolean modal) {
        super();
    }


    public EscapeDialog(Frame frame, boolean modal) {
        super();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane rootPane = new Pane();
        rootPane.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    primaryStage.show();
                    event.consume();
                }
            }
        });
    }
}