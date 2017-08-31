/*
 * Universal Password Manager
 * Copyright (C) 2005-2013 Adrian Smith
 *
 * This file is part of Universal Password Manager.
 *
 * Universal Password Manager is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Universal Password Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Universal Password Manager; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com._17od.upm.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;


public class OpenDatabaseFromURLDialog extends Application {

    private static final long serialVersionUID = 1L;

    public TextField urlTextField;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    private boolean okClicked = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Open Database From URL");

        BorderPane container = new BorderPane();

        // Create a pane with an empty border for spacing
        Border emptyBorder = Border.EMPTY;
        Pane emptyBorderPanel = new Pane();
        emptyBorderPanel.setLayoutX(5);
        emptyBorderPanel.setLayoutY(5);
        emptyBorderPanel.setBorder(emptyBorder);
        //container.getChildren().add(emptyBorderPanel);

        // Create a pane with an title etched border
//        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//        Border etchedTitleBorder = BorderFactory.createTitledBorder(etchedBorder, ' ' + Translator.translate("remoteLocation") + ' ');
        GridPane mainPanel = new GridPane();
//        mainPanel.setBorder(etchedTitleBorder);
        emptyBorderPanel.getChildren().add(mainPanel);

        GridBagConstraints c = new GridBagConstraints();

        // The URL Label row
        Label urlLabel = new Label("URL");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 0, 0);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(urlLabel, 0, 0);

        // The Remote URL input field row
        urlTextField = new TextField();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(urlTextField, 1,0);

        // The username label field
        Label usernameLabel = new Label("Username");
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 0, 0);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameLabel, 2, 0);

        // The username inpur field
        usernameTextField = new TextField();
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        mainPanel.add(usernameTextField, 3, 0);

        // The password label field
        Label passwordLabel = new Label("Password");
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 0, 0);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordLabel, 4, 0);

        // The username inpur field
        passwordTextField = new PasswordField();
        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        mainPanel.add(passwordTextField, 5, 0);

        // Some spacing
        VBox verticalSpace = new VBox(1);
        verticalSpace.setSpacing(5);
        c.gridy = 6;
        c.weighty = 1;
        mainPanel.getChildren().add(verticalSpace);

        // The buttons row

        Button okButton = new Button("ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                okClicked = true;
            }
        });

        Button cancelButton = new Button("cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        HBox button = new HBox(10);
        button.setAlignment(Pos.BOTTOM_CENTER);
        button.getChildren().addAll(okButton, cancelButton);

        container.setTop(emptyBorderPanel);
        container.setBottom(button);

        Scene scene = new Scene(container, 600, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }


    public TextField getUrlTextField() {
        return urlTextField;
    }


    public TextField getUsernameTextField() {
        return usernameTextField;
    }


    public boolean getOkClicked() {
        return okClicked;
    }
}
