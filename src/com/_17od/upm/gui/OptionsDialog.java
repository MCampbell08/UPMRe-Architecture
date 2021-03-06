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

import java.awt.*;
import java.io.File;
import java.util.Locale;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import com._17od.upm.util.Preferences;
import com._17od.upm.util.Translator;
import com._17od.upm.util.Util;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class OptionsDialog extends EscapeDialog {

	private static final long serialVersionUID = 1L;

	Stage primaryStage;
	private TextField dbToLoadOnStartup;
	private CheckBox enableProxyCheckbox;
	private CheckBox hideAccountPasswordCheckbox;
	private CheckBox inclEscCharstoPassCheckbox;
	private CheckBox storeWindowPosCheckbox;
	private CheckBox appAlwaysonTopCheckbox;
	private Label accountPasswordLengthLabel;
	private TextField accountPasswordLength;
	private TextField httpProxyHost;
	private TextField httpProxyPort;
	private TextField httpProxyUsername;
	private PasswordField httpProxyPassword;
	private CheckBox hidePasswordCheckbox;
	private CheckBox databaseAutoLockCheckbox;
	private TextField databaseAutoLockTime;
	private CheckBox acceptSelfSignedCertsCheckbox;
	private Label proxyLabel;
	private Label proxyPortLabel;
	private Label proxyUsernameLabel;
	private Label proxyPasswordLabel;
	private ComboBox localeComboBox;
	private boolean okClicked = false;
	//private Frame parentFrame;
	private boolean languageChanged;
	private char defaultEchoChar;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("options");
		BorderPane container = new BorderPane();
		container.setPadding(new Insets(10,10,10,10));

		// Create a pane with an empty border for spacing
		Border emptyBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		GridPane emptyBorderPanel = new GridPane();
		emptyBorderPanel.setPadding(new Insets(10,10,10,10));
		emptyBorderPanel.setBorder(emptyBorder);
		container.getChildren().add(emptyBorderPanel);

		// ******************
		// *** The DB TO Load On Startup Section
		// ******************
		// Create a pane with an title etched border
		BorderStroke etchedBorder = new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT);
		Border etchedTitleBorder = new Border(etchedBorder);
		// ' + Translator.translate("general") + ' ');

		GridPane mainPanel = new GridPane();
		mainPanel.setBorder(etchedTitleBorder);
		mainPanel.setPadding(new Insets(10,10,10,10));
		emptyBorderPanel.add(mainPanel,0,0);

		//GridBagConstraints c = new GridBagConstraints();

		// The "Database to Load on Startup" row
		Label urlLabel = new Label("dbToLoadOnStartup");
//        c.gridx = 0;
//        c.gridy = 0;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.insets = new Insets(0, 5, 3, 0);
//        c.weightx = 1;
//        c.weighty = 0;
//        c.gridwidth = 2;
//        c.fill = GridBagConstraints.NONE;
		mainPanel.add(urlLabel, 0, 0, 2, 1);

		// The "Database to Load on Startup" input field row
		dbToLoadOnStartup = new TextField(Preferences.get(Preferences.ApplicationOptions.DB_TO_LOAD_ON_STARTUP));
		dbToLoadOnStartup.setMinSize(25, 5);
//      dbToLoadOnStartup.setHorizontalAlignment(TextField.LEFT);
		dbToLoadOnStartup.setAlignment(Pos.CENTER_LEFT);
//        c.gridx = 0;
//        c.gridy = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.insets = new Insets(0, 5, 5, 5);
//        c.weightx = 1;
//        c.weighty = 0;
//        c.gridwidth = 1;
//        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(dbToLoadOnStartup,0, 1);

		Button dbToLoadOnStartupButton = new Button("...");
		dbToLoadOnStartupButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				getDBToLoadOnStartup();
			}
		});
////        c.gridx = 1;
////        c.gridy = 1;
////        c.anchor = GridBagConstraints.LINE_END;
////        c.insets = new Insets(0, 0, 5, 5);
////        c.weightx = 0;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(dbToLoadOnStartupButton, 1, 1);

//		// The "Language" label row
		Label localeLabel = new Label("Language");
//		//localeLabel.setText(Translator.translate("language"));
////        c.gridx = 0;
////        c.gridy = 2;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 3, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 2;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(localeLabel, 0 , 2, 2, 1);

//		// The "Locale" field row
		localeComboBox = new ComboBox();
		localeComboBox.getItems().addAll(getSupportedLocaleNames());
		for (int i = 0; i < localeComboBox.getItems().size(); i++) {
			// If the locale language is blank then set it to the English
			// language
			// I'm not sure why this happens. Maybe it's because the default
			// locale
			// is English???
			String currentLanguage = Translator.getCurrentLocale().getLanguage();
			if (currentLanguage.equals("")) {
				currentLanguage = "en";
			}

			if (currentLanguage.equals(Translator.SUPPORTED_LOCALES[i].getLanguage())) {
				localeComboBox.getSelectionModel().select(i);
				break;
			}
		}
////        c.gridx = 0;
////        c.gridy = 3;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 8, 5);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 2;
////        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(localeComboBox, 0, 3, 2, 1);

//		// The "Hide account password" row
		Boolean hideAccountPassword = new Boolean(
				Preferences.get(Preferences.ApplicationOptions.ACCOUNT_HIDE_PASSWORD, "true"));
		hideAccountPasswordCheckbox = new CheckBox("hideAccountPassword" + hideAccountPassword.booleanValue());
////        c.gridx = 0;
////        c.gridy = 4;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(hideAccountPasswordCheckbox, 0, 4);

//		// The "Database auto lock" row
		Boolean databaseAutoLock = new Boolean(
				Preferences.get(Preferences.ApplicationOptions.DATABASE_AUTO_LOCK, "false"));
		databaseAutoLockCheckbox = new CheckBox("databaseAutoLock" +
				databaseAutoLock.booleanValue());
////        c.gridx = 0;
////        c.gridy = 5;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(databaseAutoLockCheckbox, 0, 5);
		databaseAutoLockCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				databaseAutoLockTime.setDisable(databaseAutoLockCheckbox.isSelected());
			}
		});


		// The "Database auto lock" field row
		databaseAutoLockTime = new TextField(Preferences.get(Preferences.ApplicationOptions.DATABASE_AUTO_LOCK_TIME));
		databaseAutoLockTime.setMinSize(5, 5);
////        c.gridx = 1;
////        c.gridy = 5;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(databaseAutoLockTime, 1, 5);
		databaseAutoLockTime.setDisable(!databaseAutoLockCheckbox.isSelected());

		// The "Generated password length" row
		accountPasswordLengthLabel = new Label("generatedPasswodLength");
////        c.gridx = 0;
////        c.gridy = 6;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(accountPasswordLengthLabel, 0, 6);

		accountPasswordLength = new TextField(
				Preferences.get(Preferences.ApplicationOptions.ACCOUNT_PASSWORD_LENGTH));
////        c.gridx = 1;
////        c.gridy = 6;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(accountPasswordLength, 1, 6);

		// The "Include Escape Characters to Generated Passwords" row
		Boolean inclEscCharstoPass = new Boolean(
				Preferences.get(Preferences.ApplicationOptions.INCLUDE_ESCAPE_CHARACTERS, "true"));
		inclEscCharstoPassCheckbox = new CheckBox(("includePunctuationCharacters") +
				inclEscCharstoPass.booleanValue());
////        c.gridx = 0;
////        c.gridy = 7;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(inclEscCharstoPassCheckbox, 0, 7);

		// The "Store Window position" row
		Boolean storeWindowPos = Boolean
				.valueOf(Preferences.get(Preferences.ApplicationOptions.REMEMBER_WINDOW_POSITION, "false"));
		storeWindowPosCheckbox = new CheckBox("storeWindowPosition" +
				storeWindowPos.booleanValue());
////        c.gridx = 0;
////        c.gridy = 8;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(storeWindowPosCheckbox, 0, 8);

		// The "Application always on top" row
		Boolean appAlwaysonTop = new Boolean(
				Preferences.get(Preferences.ApplicationOptions.MAINWINDOW_ALWAYS_ON_TOP, "false"));
		appAlwaysonTopCheckbox = new CheckBox("applicationAlwaysonTop" +
				appAlwaysonTop.booleanValue());
////        c.gridx = 0;
////        c.gridy = 9;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(appAlwaysonTopCheckbox, 0, 9);

		// Some spacing
		mainPanel.add(new Separator(), 0, 10, 2, 1);

		// ******************
		// *** The HTTPS Section
		// ******************
		// Create a pane with an title etched border
		//Border httpsEtchedTitleBorder = BorderFactory.createTitledBorder(etchedBorder, " HTTPS ");
		Border httpsEtchedTitleBorder = new Border(etchedBorder);
		final GridPane httpsPanel = new GridPane();
		httpsPanel.setBorder(httpsEtchedTitleBorder);
		httpsPanel.setPadding(new Insets(10,10,10,10));
		//emptyBorderPanel.add(httpsPanel, 0, 1);

		// The "Accept Self Sigend Certificates" checkbox row
		Boolean acceptSelfSignedCerts = new Boolean(
				Preferences.get(Preferences.ApplicationOptions.HTTPS_ACCEPT_SELFSIGNED_CERTS, "false"));
		acceptSelfSignedCertsCheckbox = new CheckBox("acceptSelfSignedCerts" +
				acceptSelfSignedCerts.booleanValue());
//        c.gridx = 0;
//        c.gridy = 0;
//        c.anchor = GridBagConstraints.LINE_START;
//        c.insets = new Insets(0, 2, 5, 0);
//        c.weightx = 1;
//        c.weighty = 0;
//        c.gridwidth = 1;
//        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(acceptSelfSignedCertsCheckbox, 0, 11);

		mainPanel.add(new Separator(), 0, 12, 2, 1);

//		// ******************
//		// *** The Proxy Section
//		// ******************
//		// Create a pane with an title etched border
//		Border proxyEtchedTitleBorder = BorderFactory.createTitledBorder(etchedBorder,
//				' ' + Translator.translate("httpProxy") + ' ');
		Border proxyEtchedTitleBorder = Border.EMPTY;
		final GridPane proxyPanel = new GridPane();
		proxyPanel.setBorder(proxyEtchedTitleBorder);
		//emptyBorderPanel.getChildren().add(proxyPanel);

		// The "Enable Proxy" row
		Boolean proxyEnabled = new Boolean(Preferences.get(Preferences.ApplicationOptions.HTTP_PROXY_ENABLED));
		enableProxyCheckbox = new CheckBox("enableProxy" + false);
		enableProxyCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (enableProxyCheckbox.isSelected()) {
					enableProxyComponents(true);
				} else {
					enableProxyComponents(false);
				}
			}
		});

////        c.gridx = 0;
////        c.gridy = 0;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 2, 5, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(enableProxyCheckbox, 0, 14);

		// The "HTTP Proxy" label row
		proxyLabel = new Label("httpProxy");
////        c.gridx = 0;
////        c.gridy = 1;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 3, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 2;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(proxyLabel, 0, 16);

		// The "HTTP Proxy Port" label
		proxyPortLabel = new Label("port");
////        c.gridx = 1;
////        c.gridy = 1;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 3, 5);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(proxyPortLabel, 1, 16);

		// The "HTTP Proxy" field row
		httpProxyHost = new TextField(Preferences.get(Preferences.ApplicationOptions.HTTP_PROXY_HOST));
////		c.gridx = 0;
////		c.gridy = 2;
////		c.anchor = GridBagConstraints.LINE_START;
////		c.insets = new Insets(0, 5, 5, 0);
////		c.weightx = 1;
////		c.weighty = 0;
////		c.gridwidth = 1;
////		c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(httpProxyHost, 0, 17);

		httpProxyPort = new TextField(Preferences.get(Preferences.ApplicationOptions.HTTP_PROXY_PORT));
////        c.gridx = 1;
////        c.gridy = 2;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 5, 5);
////        c.weightx = 0;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(httpProxyPort, 1, 17);

		// The "HTTP Proxy Username" label row
		proxyUsernameLabel = new Label("httpProxyUsername");
////        c.gridx = 0;
////        c.gridy = 3;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 3, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 2;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(proxyUsernameLabel, 0, 18);

		// The "HTTP Proxy Username" field row
		httpProxyUsername = new TextField(Preferences.get(Preferences.ApplicationOptions.HTTP_PROXY_USERNAME));
////        c.gridx = 0;
////        c.gridy = 4;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 5, 5);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 2;
////        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(httpProxyUsername, 0, 19, 2, 1);

		// The "HTTP Proxy Password" label row
		proxyPasswordLabel = new Label("httpProxyPassword");
////        c.gridx = 0;
////        c.gridy = 5;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 3, 0);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 2;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(proxyPasswordLabel, 0, 20);

		// The "HTTP Proxy Password" field row
		String encodedPassword = /*Preferences.get(Preferences.ApplicationOptions.HTTP_PROXY_PASSWORD)*/ null;
		String decodedPassword = null;
		if (encodedPassword != null) {
			decodedPassword = new String(Base64.decodeBase64(encodedPassword.getBytes()));
		}
		httpProxyPassword = new PasswordField();
		httpProxyPassword.setText(decodedPassword);
////        c.gridx = 0;
////        c.gridy = 6;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 5, 5);
////        c.weightx = 1;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(httpProxyPassword, 0, 21);

		hidePasswordCheckbox = new CheckBox(Translator.translate("hide"));
//		defaultEchoChar = httpProxyPassword.getEchoChar();
//		hidePasswordCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				if (hidePasswordCheckbox.isSelected()) {
//					httpProxyPassword.setEchoChar(defaultEchoChar);
//				} else {
//					httpProxyPassword.setEchoChar((char) 0);
//				}
//			}
//		});

////        c.gridx = 1;
////        c.gridy = 6;
////        c.anchor = GridBagConstraints.LINE_START;
////        c.insets = new Insets(0, 5, 5, 0);
////        c.weightx = 0;
////        c.weighty = 0;
////        c.gridwidth = 1;
////        c.fill = GridBagConstraints.NONE;
		mainPanel.add(hidePasswordCheckbox, 1, 21);

		// Some spacing
		//emptyBorderPanel.add(new Separator(), 1, 0);

		// The buttons row
		HBox buttonPanel = new HBox();
		mainPanel.add(buttonPanel, 0, 22, 2, 1);
		Button okButton = new Button("ok");
		// Link Enter key to okButton
		//getRootPane().setDefaultButton(okButton);
		okButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				okButtonAction();
			}
		});
		buttonPanel.getChildren().add(okButton);

		Button cancelButton = new Button("cancel");
		cancelButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				primaryStage.close();
				event.consume();
			}
		});
		buttonPanel.getChildren().add(cancelButton);

		Preferences.save();
		enableProxyComponents(proxyEnabled.booleanValue());
		this.primaryStage = primaryStage;
		container.setCenter(mainPanel);
		Scene scene = new Scene(container);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void enableProxyComponents(boolean enable) {
		httpProxyHost.setDisable(!enable);
		httpProxyPort.setDisable(!enable);
		httpProxyUsername.setDisable(!enable);
		httpProxyPassword.setDisable(!enable);
		proxyLabel.setDisable(!enable);
		proxyPortLabel.setDisable(!enable);
		proxyUsernameLabel.setDisable(!enable);
		proxyPasswordLabel.setDisable(!enable);
		hidePasswordCheckbox.setDisable(!enable);
	}

	public boolean okClicked() {
		return okClicked;
	}

	private void okButtonAction() {
		try {
			if (databaseAutoLockCheckbox.isSelected()) {
				if (databaseAutoLockTime.getText() == null || databaseAutoLockTime.getText().trim().equals("")
						|| !Util.isNumeric(databaseAutoLockTime.getText())) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(Translator.translate("invalidValueForDatabaseAutoLockTime"));
					alert.setContentText(Translator.translate("problem"));
					databaseAutoLockTime.setFocusTraversable(true);
					return;
				}
			}

			if (accountPasswordLength.getText() == null || accountPasswordLength.getText().trim().equals("")
					|| !Util.isNumeric(accountPasswordLength.getText())) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("invalidValueForAccountPasswordLength");
				alert.setContentText("problem");
				alert.showAndWait();
				databaseAutoLockTime.setFocusTraversable(true);
				return;
			}

			Preferences.set(Preferences.ApplicationOptions.DB_TO_LOAD_ON_STARTUP, dbToLoadOnStartup.getText());
			Preferences.set(Preferences.ApplicationOptions.ACCOUNT_HIDE_PASSWORD,
					String.valueOf(hideAccountPasswordCheckbox.isSelected()));
			Preferences.set(Preferences.ApplicationOptions.INCLUDE_ESCAPE_CHARACTERS,
					String.valueOf(inclEscCharstoPassCheckbox.isSelected()));
			Preferences.set(Preferences.ApplicationOptions.REMEMBER_WINDOW_POSITION,
					String.valueOf(storeWindowPosCheckbox.isSelected()));
			Preferences.set(Preferences.ApplicationOptions.MAINWINDOW_ALWAYS_ON_TOP,
					String.valueOf(appAlwaysonTopCheckbox.isSelected()));
			Preferences.set(Preferences.ApplicationOptions.DATABASE_AUTO_LOCK,
					String.valueOf(databaseAutoLockCheckbox.isSelected()));
			Preferences.set(Preferences.ApplicationOptions.DATABASE_AUTO_LOCK_TIME, databaseAutoLockTime.getText());
			Preferences.set(Preferences.ApplicationOptions.ACCOUNT_PASSWORD_LENGTH, accountPasswordLength.getText());
			Preferences.set(Preferences.ApplicationOptions.HTTPS_ACCEPT_SELFSIGNED_CERTS,
					String.valueOf(acceptSelfSignedCertsCheckbox.isSelected()));
			Preferences.set(Preferences.ApplicationOptions.HTTP_PROXY_HOST, httpProxyHost.getText());
			Preferences.set(Preferences.ApplicationOptions.HTTP_PROXY_PORT, httpProxyPort.getText());
			Preferences.set(Preferences.ApplicationOptions.HTTP_PROXY_USERNAME, httpProxyUsername.getText());
			String encodedPassword = new String(
					Base64.encodeBase64(new String(httpProxyPassword.getText()).getBytes()));
			Preferences.set(Preferences.ApplicationOptions.HTTP_PROXY_PASSWORD, encodedPassword);
			Preferences.set(Preferences.ApplicationOptions.HTTP_PROXY_ENABLED,
					String.valueOf(enableProxyCheckbox.isSelected()));

			MainWindow.setAppAlwaysonTop(appAlwaysonTopCheckbox.isSelected());

			// Save the new language and set a flag if it has changed
			String beforeLocale = Preferences.get(Preferences.ApplicationOptions.LOCALE);
			Locale selectedLocale = Translator.SUPPORTED_LOCALES[localeComboBox.getSelectionModel().getSelectedIndex()];
			String afterLocale = selectedLocale.getLanguage();
			if (!afterLocale.equals(beforeLocale)) {
				Preferences.set(Preferences.ApplicationOptions.LOCALE, selectedLocale.getLanguage());
				Translator.loadBundle(selectedLocale);
				languageChanged = true;
			}

			Preferences.save();
//			setVisible(false);
			dispose();
			okClicked = true;
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			//alert.setTitle(e.getStackTrace());
			alert.setContentText("error");
			alert.showAndWait();
		}
	}

	private void getDBToLoadOnStartup() {
		FileChooser fc = new FileChooser();
		fc.setTitle("dbToOpenOnStartup");
		File databaseFile = fc.showOpenDialog(primaryStage);
		dbToLoadOnStartup.setText(databaseFile.getAbsoluteFile().toString());
	}

	private Object[] getSupportedLocaleNames() {
		Object[] names = new Object[Translator.SUPPORTED_LOCALES.length];

		for (int i = 0; i < Translator.SUPPORTED_LOCALES.length; i++) {
			names[i] = Translator.SUPPORTED_LOCALES[i].getDisplayLanguage(Translator.getCurrentLocale()) + " ("
					+ Translator.SUPPORTED_LOCALES[i].getDisplayLanguage(Translator.SUPPORTED_LOCALES[i]) + ')';
		}

		return names;
	}

	public boolean hasLanguageChanged() {
		return languageChanged;
	}
}