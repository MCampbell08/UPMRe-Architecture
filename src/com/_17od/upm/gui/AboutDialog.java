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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import static com.sun.glass.ui.Cursor.setVisible;

public class AboutDialog extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        String version = com._17od.upm.gui.AboutDialog.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "<version unknown>";
        }

        BorderPane pane = new BorderPane();
        primaryStage.setTitle("About UPM");

        TextArea textArea = new TextArea();
        StringBuffer aboutText = new StringBuffer();
        aboutText.append("Universal Password Manager\n");
        aboutText.append(version);
        aboutText.append("\n\n");
        aboutText.append("Copyright \u00a9 2005-2013 Adrian Smith & Contributors\n\n");
        aboutText.append("adrian@17od.com\n");
        aboutText.append("http://upm.sourceforge.net");
        textArea.setText(aboutText.toString());
        textArea.setLayoutX(Component.CENTER_ALIGNMENT);
        textArea.setEditable(false);
        textArea.setFont(new Font("Tahoma", 12));

        Button okButton = new Button("Okay");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                setVisible(false);
                primaryStage.close();
            }
        });

        HBox button = new HBox(10);
        button.setAlignment(Pos.BOTTOM_CENTER);
        button.getChildren().add(okButton);

        pane.setPadding(new Insets(5));
        pane.setCenter(textArea);
        pane.setBottom(button);

        Scene scene = new Scene(pane, 300, 200);

        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();
    }
}