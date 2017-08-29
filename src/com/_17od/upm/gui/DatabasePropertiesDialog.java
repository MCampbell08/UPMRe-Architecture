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

import com._17od.upm.database.PasswordDatabase;
import com._17od.upm.transport.Transport;
import com._17od.upm.transport.TransportException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabasePropertiesDialog extends Application {

    private static final long serialVersionUID = 1L;

    private boolean databaseNeedsSaving = false;

    final PasswordDatabase db;

    ArrayList acntName;

    public DatabasePropertiesDialog(ArrayList accountNames, final PasswordDatabase database) {
        this.acntName = accountNames;
        this.db = database;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Database Properties");

        Pane mainPane = new Pane();

        Border pane = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        Pane emptyBorderPanel = new Pane();
        emptyBorderPanel.setBorder(pane);
        mainPane.getChildren().add(emptyBorderPanel);

        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        BorderPane mainPanel = new BorderPane(new GridPane());
        mainPanel.setBorder(border);
        emptyBorderPanel.getChildren().add(mainPanel);

        GridPane c = new GridPane();

        // Remote URL Label Row
        Label urlLabel = new Label("URL");
//        c.gridx = 0;
//        c.gridy = 0;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.insets = new Insets(0, 3, 0, 0);
//        c.weightx = 1;
//        c.weighty = 0;
//        c.gridwidth = 1;
//        c.fill = GridBagConstraints.NONE
        mainPanel.getChildren().add(urlLabel);

        // The Remote URL input field row
        final TextField urlTextField = new TextField(db.getDbOptions().getRemoteLocation());
        mainPanel.getChildren().add(urlTextField);

        //The Authentication Credentials label row
        Label authLabel = new Label("Authentication Credentials");
        mainPanel.getChildren().add(authLabel);

        //The Authentication Credentials input field row
        String[] sAccountNames = new String[acntName.size() + 1];
        sAccountNames[0] = "";
        System.arraycopy(acntName.toArray(), 0, sAccountNames, 1, acntName.size());
        Arrays.sort(sAccountNames);
        List<String> list = Arrays.asList(sAccountNames);
        ObservableList<String> names = FXCollections.observableArrayList(list);
        final ComboBox auth = new ComboBox(names);
        auth.setValue(db.getDbOptions().getAuthDBEntry());
        mainPanel.getChildren().add(auth);

        // The Buttons Row
        Pane buttonPanel = new Pane(new FlowPane());
        emptyBorderPanel.getChildren().add(buttonPanel);
        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveDatabaseOptions(primaryStage, urlTextField.getText().trim(), (String) auth.getValue(), db);
            }
        });
        buttonPanel.getChildren().add(okButton);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                databaseNeedsSaving = false;
                primaryStage.close();
            }
        });
        buttonPanel.getChildren().add(cancelButton);

        Scene scene = new Scene(mainPane, 300, 200);

        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private URL validateURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            // If we got here the the URL is invalid
        }
        return url;
    }

    private void saveDatabaseOptions(Stage primaryStage, String remoteLocation, String authEntry, PasswordDatabase database) {
        boolean canCloseWindow = false;

        // If either the url or authentication entry to use have changed then update
        // the flag to indicate that the database needs to be saved
        if (!database.getDbOptions().getRemoteLocation().equals(remoteLocation) ||
                !database.getDbOptions().getAuthDBEntry().equals(authEntry)) {
            databaseNeedsSaving = true;
        } else {
            // If the db doesn't need to be saved then we can close this window
            canCloseWindow = true;
        }

        if (!remoteLocation.equals("")) {

            // Check the validity of the URL given by the user
            URL url = validateURL(remoteLocation);
            if (url != null) {

                // Only allow supported protocols
                if (Transport.isASupportedProtocol(url.getProtocol())) {

                    // If the remote location has changed then upload the database
                    if (!database.getDbOptions().getRemoteLocation().equals(remoteLocation)) {
                        try {
                            Transport transport = Transport.getTransportForURL(url);
                            if (!authEntry.equals("")) {
                                String userId = database.getAccount(authEntry).getUserId();
                                String password = database.getAccount(authEntry).getPassword();
                                transport.put(remoteLocation, database.getDatabaseFile(), userId, password);
                            } else {
                                transport.put(remoteLocation, database.getDatabaseFile());
                            }
                            canCloseWindow = true;
                        } catch (TransportException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Transport Error");
                            alert.setHeaderText(null);
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                    } else {
                        canCloseWindow = true;
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Protocol");
                    alert.setHeaderText(null);
                    alert.setContentText("Unsupported Protocol");
                    alert.showAndWait();
                }

            } else {
                // If we got here the the URL is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid URL");
                alert.setHeaderText(null);
                alert.setContentText("Given URL is Invalid");
                alert.showAndWait();
            }

        } else {
            // If we were given a blank URL then the user doesn't want to maintain a remote location so we can safetly exit
            canCloseWindow = true;
        }

        if (canCloseWindow) {
            try {
                if (databaseNeedsSaving) {
                    database.getDbOptions().setAuthDBEntry(authEntry);
                    database.getDbOptions().setRemoteLocation(remoteLocation);
                }
                primaryStage.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Problem Saving DB");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public boolean getDatabaseNeedsSaving() {
        return databaseNeedsSaving;
    }
}