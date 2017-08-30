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

import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.UrlValidator;

import com._17od.upm.database.AccountInformation;
import com._17od.upm.util.Preferences;
import com._17od.upm.util.Translator;
import com._17od.upm.util.Util;

import static sun.plugin.javascript.navig.JSType.Image;

public class AccountDialog extends Application {

	private static final long serialVersionUID = 1L;
	private static final char[] ALLOWED_CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9' };
	// Extended CharArray list which include also 24 Escape characters for
	// stronger password generation
	private static final char[] EXTRA_ALLOWED_CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ',', ')', '_', '-',
			'+', '=', '|', '/', '<', '>', '.', '?', ';', ':' };

	/*
     * We will use the (4)four above CharArray lists(UPPERCASE_CHARS,
     * LOWERCASE_CHARS, NUMBER_CHARS, ESCAPE_CHARS) to ensure that the generated
     * passwords will be more complex. If the user has selected to include
     * escape characters to generated passwords and the length of the passwords
     * is 4 or above, then we will use some methods in order to generate
     * passwords that will have at least 1 lower case + 1 upper case + 1 number
     * + 1 escape character. On the other hand, if the user has not selected to
     * include escape characters to generated passwords and the length of the
     * passwords is at least 3, then we will use methods in order to generate
     * passwords that will have at least 1 lower case + 1 upper case + 1 number.
     */
	private static final char[] UPPERCASE_CHARS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'

	};

	private static final char[] LOWERCASE_CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'

	};

	private static final char[] NUMBER_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private static final char[] PUNCTUATION_CHARS = { '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ',', ')', '_',
			'-', '+', '=', '|', '/', '<', '>', '.', '?', ';', ':' };

	private AccountInformation pAccount;
	private TextField userId;
	private PasswordField password;
	private TextArea notes;
	private TextField url;
	private TextField accountName;
	private boolean okClicked = false;
	private ArrayList existingAccounts;
	private Stage parentWindow;
	private boolean accountChanged = false;
	private char defaultEchoChar;
	private Scene scene;
	private Pane pane;
	private boolean readOnly = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		pane = new AnchorPane();
		scene = new Scene(pane, primaryStage.getWidth(), primaryStage.getHeight());
		scene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				if(event.getClickCount() == 1){
					primaryStage.requestFocus();
				}
			}
		});
		boolean addingAccount = false;

		String title = null;
		if (readOnly) {
			title = Translator.translate("viewAccount");
		} else if (!readOnly && pAccount.getAccountName().trim().equals("")) {
			title = Translator.translate("addAccount");
			addingAccount = true;
		} else {
			title = Translator.translate("editAccount");
		}
		primaryStage.setTitle(title);
	}

	public AccountDialog(AccountInformation account, Stage parentWindow, boolean readOnly,
						 ArrayList existingAccounts) {
		super();

		this.readOnly = readOnly;
		//Request focus on Account JDialog when mouse clicked

		// Set the title based on weather we've been opened in readonly mode and
		// weather the
		// Account passed in is empty or not

		this.pAccount = account;
		this.existingAccounts = existingAccounts;
		this.parentWindow = parentWindow;

		//getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		GridPane container = new GridPane();

		// The AccountName Row
		Label accountLabel = new Label();
		accountLabel.setText(Translator.translate("account"));
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		container.getChildren().add(accountLabel);

		// This panel will hold the Account field and the copy and paste
		// buttons.
		GridPane accountPanel = new GridPane();

		accountName = new TextField(new String(pAccount.getAccountName()));
		if (readOnly) {
			accountName.setEditable(false);
		}
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		accountPanel.getChildren().add(accountName);
        accountName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                accountName.selectAll();
            }
        });

		Button acctCopyButton = new Button();
		Image acctCopy = new Image(getClass().getResourceAsStream("/util/copy-icon.png"));
		//acctCopyButton.setIcon(Util.loadImage("copy-icon.png"));
		acctCopyButton.setGraphic(new ImageView(acctCopy));
		acctCopyButton.setTooltip(new Tooltip("Copy"));
		acctCopyButton.setDisable(false);
		//acctCopyButton.setMargin(new Insets(0, 0, 0, 0));
		acctCopyButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyTextField(accountName);
			}
		});
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		accountPanel.getChildren().add(acctCopyButton);

		Button acctPasteButton = new Button();
		Image acctPaste = new Image(getClass().getResourceAsStream("/util/paste-icon.png"));
		//acctPasteButton.setIcon(Util.loadImage("paste-icon.png"));
		acctPasteButton.setGraphic(new ImageView(acctPaste));
		acctPasteButton.setTooltip(new Tooltip("Paste"));
		acctPasteButton.setDisable(readOnly);
		//acctPasteButton.setMargin(new Insets(0, 0, 0, 0));
		acctPasteButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				pasteToTextField(accountName);
			}
		});
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		accountPanel.getChildren().add(acctPasteButton);

		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.getChildren().add(accountPanel);

		// Userid Row
		Label useridLabel = new Label();
		useridLabel.setText(Translator.translate("userid"));
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		container.getChildren().add(useridLabel);

		// This panel will hold the User ID field and the copy and paste
		// buttons.
		GridPane idPanel = new GridPane();

		userId = new TextField(new String(pAccount.getUserId()));
		if (readOnly) {
			userId.setEditable(false);
		}
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		idPanel.getChildren().add(userId);
        userId.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                userId.selectAll();
            }
        });

		Button idCopyButton = new Button();
		Image copyBackground = new Image(getClass().getResourceAsStream("/util/copy-icon.png"));
		//idCopyButton.setIcon(Util.loadImage("copy-icon.png"));
		idCopyButton.setGraphic(new ImageView(copyBackground));
		idCopyButton.setTooltip(new Tooltip("Copy"));
		idCopyButton.setDisable(false);
		//idCopyButton.setMargin(new Insets(0, 0, 0, 0));
		idCopyButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyTextField(userId);
			}
		});
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		idPanel.getChildren().add(idCopyButton);

		Button idPasteButton = new Button();
		Image paste = new Image(getClass().getResourceAsStream("/util/paste-icon.png"));
		//idPasteButton.setIcon(Util.loadImage("paste-icon.png"));
		idPasteButton.setGraphic(new ImageView(paste));
		idPasteButton.setTooltip(new Tooltip("Paste"));
		idPasteButton.setDisable(readOnly);
		//.setMargin(new Insets(0, 0, 0, 0));
		idPasteButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				pasteToTextField(userId);
			}
		});
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		idPanel.getChildren().add(idPasteButton);

		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.getChildren().add(idPanel);

		// Password Row
		Label passwordLabel = new Label();
		passwordLabel.setText(Translator.translate("password"));
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(15, 10, 10, 10);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		container.getChildren().add(passwordLabel);

		// This panel will hold the password, generate password button, copy and
		// paste buttons, and hide password checkbox
		GridPane passwordPanel = new GridPane();

		password = new PasswordField();
		password.setText(new String(pAccount.getPassword()));
		// allow CTRL-C on the password field
		//password.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
		password.setEditable(!readOnly);
        password.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                password.selectAll();
            }
        });
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		passwordPanel.getChildren().add(password);

		Button generateRandomPasswordButton = new Button(Translator.translate("generate"));
		if (readOnly) {
			generateRandomPasswordButton.setDisable(true);
		}
		generateRandomPasswordButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				// Get the user's preference about including or not Escape
				// Characters to generated passwords
				Boolean includeEscapeChars = new Boolean(
						Preferences.get(Preferences.ApplicationOptions.INCLUDE_ESCAPE_CHARACTERS, "true"));
				int pwLength = Preferences.getInt(Preferences.ApplicationOptions.ACCOUNT_PASSWORD_LENGTH, 8);
				String Password;

				if ((includeEscapeChars.booleanValue()) && (pwLength > 3)) {
					// Verify that the generated password satisfies the criteria
					// for strong passwords(including Escape Characters)
					do {
						Password = GeneratePassword(pwLength, includeEscapeChars.booleanValue());
					} while (!(CheckPassStrong(Password, includeEscapeChars.booleanValue())));

				} else if (!(includeEscapeChars.booleanValue()) && (pwLength > 2)) {
					// Verify that the generated password satisfies the criteria
					// for strong passwords(excluding Escape Characters)
					do {
						Password = GeneratePassword(pwLength, includeEscapeChars.booleanValue());
					} while (!(CheckPassStrong(Password, includeEscapeChars.booleanValue())));

				} else {
					// Else a weak password of 3 or less chars will be produced
					Password = GeneratePassword(pwLength, includeEscapeChars.booleanValue());
				}
				password.setText(Password);
			}
		});
//		if (addingAccount) {
//			generateRandomPasswordButton.fire();
//		}
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		passwordPanel.getChildren().add(generateRandomPasswordButton);

		Button pwCopyButton = new Button();
		Image pwCopy = new Image(getClass().getResourceAsStream("/util/copy-icon.png"));
		//pwCopyButton.setIcon(Util.loadImage("copy-icon.png"));
		pwCopyButton.setGraphic(new ImageView(pwCopy));
		pwCopyButton.setTooltip(new Tooltip("Copy"));
		pwCopyButton.setDisable(false);
		//pwCopyButton.setMargin(new Insets(0, 0, 0, 0));
		pwCopyButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyTextField(password);
			}
		});
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		passwordPanel.getChildren().add(pwCopyButton);

		Button pwPasteButton = new Button();
		Image pwPaste = new Image(getClass().getResourceAsStream("/util/paste-icon.png"));
		//pwPasteButton.setIcon(Util.loadImage("paste-icon.png"));
		pwPasteButton.setGraphic(new ImageView(pwPaste));
		pwPasteButton.setTooltip(new Tooltip("Paste"));
		pwPasteButton.setDisable(readOnly);
		//pwPasteButton.setMargin(new Insets(0, 0, 0, 0));
		pwPasteButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				pasteToTextField(password);
			}
		});
		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		passwordPanel.getChildren().add(pwPasteButton);

		CheckBox hidePasswordCheckbox = new CheckBox(Translator.translate("hide"));
//		defaultEchoChar = password.getEchoChar();
//		hidePasswordCheckbox.setMargin(new Insets(5, 0, 5, 0));
//		hidePasswordCheckbox.addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent e) {
//				if (e.getStateChange() == ItemEvent.SELECTED) {
//					password.setEchoChar(defaultEchoChar);
//				} else {
//					password.setEchoChar((char) 0);
//				}
//			}
//		});

		Boolean hideAccountPassword = new Boolean(
				Preferences.get(Preferences.ApplicationOptions.ACCOUNT_HIDE_PASSWORD, "true"));
		hidePasswordCheckbox.setSelected(hideAccountPassword.booleanValue());

		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		passwordPanel.getChildren().add(hidePasswordCheckbox);

		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.getChildren().add(passwordPanel);

		// URL Row
		Label urlLabel = new Label();
		urlLabel.setText(Translator.translate("url"));
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		container.getChildren().add(urlLabel);

		// This panel will hold the URL field and the copy and paste buttons.
		GridPane urlPanel = new GridPane();

		url = new TextField(new String(pAccount.getUrl()));
		if (readOnly) {
			url.setEditable(false);
		}
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		urlPanel.getChildren().add(url);
        url.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                url.selectAll();
            }
        });

		final Button urlLaunchButton = new Button();
		Image urlLaunch = new Image(getClass().getResourceAsStream("/util/launch-url-sm.png"));
		//urlLaunchButton.setIcon(Util.loadImage("launch-url-sm.png"));
		urlLaunchButton.setGraphic(new ImageView(urlLaunch));
		urlLaunchButton.setTooltip(new Tooltip("Launch URL"));
		urlLaunchButton.setDisable(false);
		//urlLaunchButton.setMargin(new Insets(0, 0, 0, 0));
		urlLaunchButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				String urlText = url.getText();

				// Check if the selected url is null or emty and inform the user
				// via JoptioPane message
				if ((urlText == null) || (urlText.length() == 0)) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle(Translator.translate("EmptyUrlMsg"));
					alert.setContentText(Translator.translate("UrlErrorTitle"));
					alert.showAndWait();
					// Check if the selected url is a valid formated url(via
					// urlIsValid() method) and inform the user via JoptioPane
					// message
				} else if (!(urlIsValid(urlText))) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle(Translator.translate("InvalidUrlMsg"));
					alert.setContentText(Translator.translate("UrlErrorTitle"));
					alert.showAndWait();
					// Call the method LaunchSelectedURL() using the selected
					// url as input
				} else {
					LaunchSelectedURL(urlText);
				}
			}
		});
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		urlPanel.getChildren().add(urlLaunchButton);

		Button urlCopyButton = new Button();
		Image urlCopy = new Image(getClass().getResourceAsStream("/util/copy-icon.png"));
		//urlCopyButton.setIcon(Util.loadImage("copy-icon.png"));
		urlCopyButton.setGraphic(new ImageView(urlCopy));
		urlCopyButton.setTooltip(new Tooltip("Copy"));
		urlCopyButton.setDisable(false);
		//urlCopyButton.setMargin(new Insets(0, 0, 0, 0));
		urlCopyButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyTextField(url);
			}
		});
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		urlPanel.getChildren().add(urlCopyButton);

		Button urlPasteButton = new Button();
		Image urlPaste = new Image(getClass().getResourceAsStream("/util/paste-icon.png"));
		//urlPasteButton.setIcon(Util.loadImage("paste-icon.png"));
		urlPasteButton.setGraphic(new ImageView(urlPaste));
		urlPasteButton.setTooltip(new Tooltip("Paste"));
		urlPasteButton.setDisable(readOnly);
		//urlPasteButton.setMargin(new Insets(0, 0, 0, 0));
		urlPasteButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				pasteToTextField(url);
			}
		});
		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		urlPanel.getChildren().add(urlPasteButton);

		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.getChildren().add(urlPanel);

		// Notes Row
		Label notesLabel = new Label();
		notesLabel.setText(Translator.translate("notes"));
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		container.getChildren().add(notesLabel);

		// This panel will hold the Notes text area and the copy and paste
		// buttons.
		GridPane notesPanel = new GridPane();

		notes = new TextArea(new String(pAccount.getNotes()));
		if (readOnly) {
			notes.setEditable(false);
		}
		notes.isWrapText(); // Enable line wrapping.
		notes.setWrapText(true); // Line wrap at whitespace.
		ScrollPane notesScrollPane = new ScrollPane(notes);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		notesPanel.getChildren().add(notesScrollPane);

		Button notesCopyButton = new Button();
		Image notesCopy = new Image(getClass().getResourceAsStream("/util/copy-icon.png"));
		//notesCopyButton.setIcon(Util.loadImage("copy-icon.png"));
		notesCopyButton.setGraphic(new ImageView(notesCopy));
		notesCopyButton.setTooltip(new Tooltip("Copy"));
		notesCopyButton.setDisable(false);
		//notesCopyButton.setMargin(new Insets(0, 0, 0, 0));
		notesCopyButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyTextArea(notes);
			}
		});
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		notesPanel.getChildren().add(notesCopyButton);

		Button notesPasteButton = new Button();
		Image notesPaste = new Image(getClass().getResourceAsStream("/util/paste-icon.png"));
		//notesPasteButton.setIcon(Util.loadImage("paste-icon.png"));
		notesPasteButton.setGraphic(new ImageView(notesPaste));
		notesPasteButton.setTooltip(new Tooltip("Paste"));
		notesPasteButton.setDisable(readOnly);
		//notesPasteButton.setMargin(new Insets(0, 0, 0, 0));
		notesPasteButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				pasteToTextArea(notes);
			}
		});
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0, 0, 0, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		notesPanel.getChildren().add(notesPasteButton);

		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.getChildren().add(notesPanel);

		// Seperator Row
		Separator sep = new Separator();
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.getChildren().add(sep);

		// Button Row
		GridPane buttonPanel = new GridPane();
		Button okButton = new Button(Translator.translate("ok"));
		// Link Enter key to okButton
		//getRootPane().setDefaultButton(okButton);
		okButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				okButtonAction();
			}
		});
		buttonPanel.getChildren().add(okButton);
		if (!readOnly) {
			Button cancelButton = new Button(Translator.translate("cancel"));
			cancelButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
				@Override
				public void handle(javafx.event.ActionEvent event) {
					closeButtonAction();
				}
			});
			buttonPanel.getChildren().add(cancelButton);
		}
		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(5, 0, 5, 0);
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.NONE;
		container.getChildren().add(buttonPanel);
	} // End AccountDialog constructor

	public boolean okClicked() {
		return okClicked;
	} // End okClicked()

	public AccountInformation getAccount() {
		return pAccount;
	} // End getAccount()

	private void okButtonAction() {

		// Check if the account name has changed.
		if (!pAccount.getAccountName().equals(accountName.getText().trim())) {
			accountChanged = true;
		}

		// [1375397] Ensure that an account with the supplied name doesn't
		// already exist.
		// By checking 'accountNames' we're checking both visible and filtered
		// accounts
		//
		// Only check if an account with the same name exists if the account
		// name has actually changed
		if (accountChanged && existingAccounts.indexOf(accountName.getText().trim()) > -1) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(Translator.translate("accountAlreadyExistsWithName" + accountName.getText().trim()));
			alert.setContentText(Translator.translate("accountAlreadyExists"));
			alert.showAndWait();
		} else {
			// Check for changes
			if (!pAccount.getUserId().equals(userId.getText())) {
				accountChanged = true;
			}
			if (!pAccount.getPassword().equals(password.getText())) {
				accountChanged = true;
			}
			if (!pAccount.getUrl().equals(url.getText())) {
				accountChanged = true;
			}
			if (!pAccount.getNotes().equals(notes.getText())) {
				accountChanged = true;
			}

			pAccount.setAccountName(accountName.getText().trim());
			pAccount.setUserId(userId.getText());
			pAccount.setPassword(password.getText());
			pAccount.setUrl(url.getText());
			pAccount.setNotes(notes.getText());

			okClicked = true;
//          setVisible(false);
//		    dispose();
		}
	} // End okButtonAction()

	public boolean getAccountChanged() {
		return accountChanged;
	} // End getAccountChanged()

	private void closeButtonAction() {
		okClicked = false;
//		setVisible(false);
//		dispose();
	} // End closeButtonAction()

	/**
	 * This method takes as input the user's preferences about password length,
	 * including or excluding Escape Characters, and randomly generates a
	 * password. Then, the method returns the generated password as a String.
	 *
	 * @param PassLength
	 * @param InclEscChars
	 * @return passwordBuffer.toString()
	 */
	private static String GeneratePassword(int PassLength, boolean InclEscChars) {
		SecureRandom random = new SecureRandom();
		StringBuffer passwordBuffer = new StringBuffer();

		if (InclEscChars) {
			for (int i = 0; i < PassLength; i++) {
				passwordBuffer.append(EXTRA_ALLOWED_CHARS[random.nextInt(EXTRA_ALLOWED_CHARS.length)]);
			}
			return passwordBuffer.toString();

		} else {
			for (int i = 0; i < PassLength; i++) {
				passwordBuffer.append(ALLOWED_CHARS[random.nextInt(ALLOWED_CHARS.length)]);
			}
			return passwordBuffer.toString();
		}
	} // End GeneratePassword()

	/**
	 * This method returns true if the generated password satisfies the criteria
	 * of a strong password, including or excluding Escape Characters. If not,
	 * then returns false.
	 *
	 * @param Pass
	 * @param InclEscChars
	 * @return true or false, depending on strength criteria.
	 */
	private static boolean CheckPassStrong(String Pass, boolean InclEscChars) {
		if (InclEscChars) {
			if ((InclUpperCase(Pass)) && (InclLowerCase(Pass)) && (InclNumber(Pass)) && (InclEscape(Pass))) {
				return true;
			} else {
				return false;
			}
		} else {
			if ((InclUpperCase(Pass)) && (InclLowerCase(Pass)) && (InclNumber(Pass))) {
				return true;
			} else {
				return false;
			}
		}
	} // End CheckPassStrong()

	/**
	 * This method returns true if the generated password contains at least one
	 * Upper Case character. If not, then the method returns false.
	 *
	 * @param GeneratedPass
	 * @return true or false, depending on existence of one upper case letter.
	 */
	private static boolean InclUpperCase(String GeneratedPass) {
		char[] PassWordArray = GeneratedPass.toCharArray();
		boolean find = false;
		outerloop: for (int i = 0; i < PassWordArray.length; i++) {
			for (int j = 0; j < UPPERCASE_CHARS.length; j++) {
				if (PassWordArray[i] == UPPERCASE_CHARS[j]) {
					find = true;
					break outerloop;
				}
			}
		}
		if (find) {
			return true;
		} else {
			return false;
		}
	} // End InclUpperCase()

	/**
	 * This method returns true if the generated password contains at least one
	 * Lower Case character. If not, then the method returns false.
	 *
	 * @param GeneratedPass
	 * @return true or false, depending on existence of one lower case letter.
	 */
	private static boolean InclLowerCase(String GeneratedPass) {
		char[] PassWordArray = GeneratedPass.toCharArray();
		boolean find = false;
		outerloop: for (int i = 0; i < PassWordArray.length; i++) {
			for (int j = 0; j < LOWERCASE_CHARS.length; j++) {
				if (PassWordArray[i] == LOWERCASE_CHARS[j]) {
					find = true;
					break outerloop;
				}
			}
		}
		if (find) {
			return true;
		} else {
			return false;
		}
	} // End InclLowerCase()

	/**
	 * This method returns true if the generated password contains at least one
	 * Number. If not, then the method returns false.
	 *
	 * @param GeneratedPass
	 * @return true or false, depending on existence of one number.
	 */
	private static boolean InclNumber(String GeneratedPass) {
		char[] PassWordArray = GeneratedPass.toCharArray();
		boolean find = false;
		outerloop: for (int i = 0; i < PassWordArray.length; i++) {
			for (int j = 0; j < NUMBER_CHARS.length; j++) {
				if (PassWordArray[i] == NUMBER_CHARS[j]) {
					find = true;
					break outerloop;
				}
			}
		}
		if (find) {
			return true;
		} else {
			return false;
		}
	} // End InclNumber()

	/**
	 * This method returns true if the generated password contains at least one
	 * Escape character. If not, then the method returns false.
	 *
	 * @param GeneratedPass
	 * @return true or false, depending on existence of one escape character.
	 */
	private static boolean InclEscape(String GeneratedPass) {
		char[] PassWordArray = GeneratedPass.toCharArray();
		boolean find = false;
		outerloop: for (int i = 0; i < PassWordArray.length; i++) {
			for (int j = 0; j < PUNCTUATION_CHARS.length; j++) {
				if (PassWordArray[i] == PUNCTUATION_CHARS[j]) {
					find = true;
					break outerloop;
				}
			}
		}
		if (find) {
			return true;
		} else {
			return false;
		}
	} // End InclEscape()

	/**
	 * This method takes in a JTextField object and then copies the text of that
	 * text field to the system clipboard.
	 *
	 * @param textField
	 */
	public void copyTextField(TextField textField) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selected = new StringSelection(textField.getText());
		clipboard.setContents(selected, selected);
	}

	/**
	 * This method takes in a JTextArea object and then copies the selected text
	 * in that text area to the system clipboard.
	 *
	 * @param textArea
	 */
	public void copyTextArea(TextArea textArea) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selected = new StringSelection(textArea.getSelectedText());
		clipboard.setContents(selected, selected);
	}

	/**
	 * This method takes in a JTextField object and then sets the text of that
	 * text field to the contents of the system clipboard.
	 *
	 * @param textField
	 */
	public void pasteToTextField(TextField textField) {
		String text = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipText = clipboard.getContents(null);
		if ((clipText != null) && clipText.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				text = (String) clipText.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		textField.setText(text);
	}

	/**
	 * This method takes in a JTextArea object and then inserts the contents of
	 * the system clipboard into that text area at the cursor position.
	 *
	 * @param textArea
	 */
	public void pasteToTextArea(TextArea textArea) {
		String text = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipText = clipboard.getContents(null);
		if ((clipText != null) && clipText.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				text = (String) clipText.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		textArea.setText(text);
		textArea.getChildrenUnmodifiable().addAll(textArea);
		textArea.requestFocus();
	}

	/**
	 * Use com.apache.commons.validator library in order to check the validity
	 * (proper formating, e.x http://www.url.com) of the given URL.
	 *
	 * @param urL
	 * @return
	 */
	private boolean urlIsValid(String urL) {

		UrlValidator urlValidator = new UrlValidator();
		if (urlValidator.isValid(urL)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Method that get(as input) the selected Account URL and open this URL via
	 * the default browser of our platform.
	 *
	 * @param url
	 */
	private void LaunchSelectedURL(String url) {

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			try {
				desktop.browse(new URI(url));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else { // Linux and Mac specific code in order to launch url
			Runtime runtime = Runtime.getRuntime();

			try {
				runtime.exec("xdg-open " + url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}