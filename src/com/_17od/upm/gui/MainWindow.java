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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.UrlValidator;
import com._17od.upm.crypto.InvalidPasswordException;
import com._17od.upm.database.AccountInformation;
import com._17od.upm.database.ProblemReadingDatabaseFile;
import com._17od.upm.platformspecific.PlatformSpecificCode;
import com._17od.upm.util.Preferences;
import com._17od.upm.util.Translator;
import com._17od.upm.util.Util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the main application entry class
 */
public class MainWindow extends Application implements EventHandler {

	private static final long serialVersionUID = 1L;
	private static final String applicationName = "Universal Password Manager";

	public static final String NEW_DATABASE_TXT = "newDatabaseMenuItem";
	public static final String OPEN_DATABASE_TXT = "openDatabaseMenuItem";
	public static final String OPEN_DATABASE_FROM_URL_TXT = "openDatabaseFromURLMenuItem";
	public static final String SYNC_DATABASE_TXT = "syncWithRemoteDatabaseMenuItem";
	public static final String CHANGE_MASTER_PASSWORD_TXT = "changeMasterPasswordMenuItem";
	public static final String DATABASE_PROPERTIES_TXT = "databasePropertiesMenuItem";
	public static final String ADD_ACCOUNT_TXT = "addAccountMenuItem";
	public static final String EDIT_ACCOUNT_TXT = "editAccountMenuItem";
	public static final String DELETE_ACCOUNT_TXT = "deleteAccountMenuItem";
	public static final String VIEW_ACCOUNT_TXT = "viewAccountMenuItem";
	public static final String COPY_USERNAME_TXT = "copyUsernameMenuItem";
	public static final String COPY_PASSWORD_TXT = "copyPasswordMenuItem";
	public static final String LAUNCH_URL_TXT = "launchURLMenuItem";
	public static final String OPTIONS_TXT = "optionsMenuItem";
	public static final String ABOUT_TXT = "aboutMenuItem";
	public static final String RESET_SEARCH_TXT = "resetSearchMenuItem";
	public static final String EXIT_TXT = "exitMenuItem";
	public static final String EXPORT_TXT = "exportMenuItem";
	public static final String IMPORT_TXT = "importMenuItem";
	public static final String LOCK_TIMER_TXT = "lock";

	private Button addAccountButton;
	private Button editAccountButton;
	private Button deleteAccountButton;
	private Button copyUsernameButton;
	private Button copyPasswordButton;
	private Button launchURLButton;
	private Button optionsButton;
	private Button syncDatabaseButton;
	private TextField searchField;
	private Button resetSearchButton;
	private Label searchIcon;

	private Menu databaseMenu;
	private MenuItem newDatabaseMenuItem;
	private MenuItem openDatabaseMenuItem;
	private MenuItem openDatabaseFromURLMenuItem;
	private MenuItem syncWithRemoteDatabaseMenuItem;
	private MenuItem changeMasterPasswordMenuItem;
	private MenuItem databasePropertiesMenuItem;
	private MenuItem exitMenuItem;
	private Menu helpMenu;
	private MenuItem aboutMenuItem;
	private Menu accountMenu;
	private MenuItem addAccountMenuItem;
	private MenuItem editAccountMenuItem;
	private MenuItem deleteAccountMenuItem;
	private MenuItem viewAccountMenuItem;
	private MenuItem copyUsernameMenuItem;
	private MenuItem copyPasswordMenuItem;
	private MenuItem launchURLMenuItem;
	private MenuItem exportMenuItem;
	private MenuItem importMenuItem;

	private ListView accountsListview;
	private Label statusBar = new Label(" ");
	private Pane databaseFileChangedPanel;
	public static MainWindow AppWindow;

	private DatabaseActions dbActions;
	public Stage primary_stage;

	private String windowTitle = "";
	private static int window_X = 0;
	private static int window_Y = 0;
	private static int window_Height = 0;
	private static int window_Width = 0;

//	public MainWindow(String title) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
//			IOException,
//			ProblemReadingDatabaseFile {
//		super();
//		windowTitle = title;
//
//
//
//		PlatformSpecificCode.getInstance().initialiseApplication(this);
//
//
//
//		// Set up the content pane.
//
//		// Add listener to store current position and size on closing
//
//
//		// Display the window.
////		pack();
////		setLocationRelativeTo(null);
//
//
////	 Give the search field focus
////	 I'm using requestFocusInWindow() rathar than requestFocus()
////	 because the javadocs recommend i
////	 ----------------- //searchField.requestFocusInWindow(); // -------------------- //
//
//	}

	public static void setAppAlwaysonTop(boolean val) {
		//AppWindow.setAlwaysOnTop(val);
		// AppWindow.revalidate();

	}

	public static void main(String[] args) {
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//
//				try {
//					// Use the System look and feel
//					Preferences.load();
//					Translator.initialise();
//					Double jvmVersion = new Double(System.getProperty("java.specification.version"));
//					if (jvmVersion.doubleValue() < 1.4) {
//						Alert alert = new Alert(Alert.AlertType.ERROR);
//						alert.setTitle(Translator.translate("problem"));
//						alert.setHeaderText(null);
//						alert.setContentText(Translator.translate("requireJava14"));
//						alert.showAndWait();
//					} else {
//						AppWindow = new MainWindow(applicationName);
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		windowTitle = "UPM";
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../util/images/upm.gif")));
		primaryStage.setTitle(windowTitle);

		window_X = (int)primaryStage.getX();
		window_Y = (int)primaryStage.getY();
		window_Width = (int)primaryStage.getWidth();
		window_Height = (int)primaryStage.getHeight();

		primaryStage.setOnCloseRequest(new EventHandler<javafx.stage.WindowEvent>() {
			@Override
			public void handle(javafx.stage.WindowEvent event) {
				event.consume();
//				storeWindowBounds();
//				try {
//					Preferences.save();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				System.out.println("Closing Application");
				primaryStage.close();
			}
		});
		MenuBar menuBar = createMenuBar();
		GridPane newPane = addComponentsToPane();
		VBox box = new VBox();
		box.getChildren().addAll(/*menuBar,*/ newPane);
		Scene scene = new Scene(box);

		//Boolean appAlwaysOnTop = new Boolean(Preferences.get(Preferences.ApplicationOptions.MAINWINDOW_ALWAYS_ON_TOP, "false"));
		//primaryStage.setAlwaysOnTop(appAlwaysOnTop.booleanValue());

		primaryStage.setAlwaysOnTop(true);
		primaryStage.setScene(scene);
		primary_stage = primaryStage;
		primaryStage.show();

		dbActions = new DatabaseActions(this);
		addComponentsToPane();

//		boolean restore = Preferences.get(Preferences.ApplicationOptions.REMEMBER_WINDOW_POSITION, "false")
//				.equals("true");
//		if (restore) {
//			restoreWindowBounds();
//		}

//		try {
//			// Load the startup database if it's configured
//			String db = Preferences.get(Preferences.ApplicationOptions.DB_TO_LOAD_ON_STARTUP);
//			if (db != null && !db.equals("")) {
//				File dbFile = new File(db);
//				if (!dbFile.exists()) {
//					dbActions.errorHandler(new Exception(Translator.translate("dbDoesNotExist", db)));
//				} else {
//					dbActions.openDatabase(db);
//				}
//			}
//		} catch (Exception e) {
//			dbActions.errorHandler(e);
//		}

		PlatformSpecificCode.getInstance().initialiseApplication(this);
	}

	private GridPane addComponentsToPane() {

		// Ensure the layout manager is a BorderLayout
//        if (!(getContentPane().getLayout() instanceof GridBagLayout)) {
//            getContentPane().setLayout(new GridBagLayout());
//        }

		GridPane pane = new GridPane();
		pane.setPadding(new Insets(0,10,0,10));

		//GridBagConstraints c = new GridBagConstraints();

		// The toolbar Row
//		c.gridx = 0;
//		c.gridy = 0;
//		c.anchor = GridBagConstraints.FIRST_LINE_START;
//		c.insets = new Insets(0, 0, 0, 0);
//		c.weightx = 0;
//		c.weighty = 0;
//		c.gridwidth = 3;
//		c.fill = GridBagConstraints.HORIZONTAL;
		ToolBar toolbar = createToolBar();
		toolbar.setOrientation(Orientation.HORIZONTAL);
		pane.add(toolbar, 0, 0);

		// Keep the frame background color consistent
		pane.setBackground(toolbar.getBackground());

		// The seperator Row
//		c.gridx = 0;
//		c.gridy = 1;
//		c.anchor = GridBagConstraints.LINE_START;
//		c.insets = new Insets(0, 0, 0, 0);
//		c.weightx = 1;
//		c.weighty = 0;
//		c.gridwidth = 3;
//		c.fill = GridBagConstraints.HORIZONTAL;
		pane.getChildren().add(new Separator());

		// The search field row
		Image backgroundImage10 = new Image(getClass().getResourceAsStream("../util/images/search.gif"));
		searchIcon = new Label();
		searchIcon.setGraphic(new ImageView(backgroundImage10));
		//searchIcon.setDisabledIcon(Util.loadImage("search_d.gif"));
		searchIcon.setDisable(false);
//		c.gridx = 0;
//		c.gridy = 2;
//		c.anchor = GridBagConstraints.LINE_START;
//		c.insets = new Insets(5, 1, 5, 1);
//		c.weightx = 0;
//		c.weighty = 0;
//		c.gridwidth = 1;
//		c.fill = GridBagConstraints.NONE;
		pane.add(searchIcon, 2, 1);

		searchField = new TextField();
		searchField.setDisable(true);
		searchField.setMinSize(searchField.getPrefWidth(), searchField.getPrefHeight());
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			dbActions.filter();
		});
		searchField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if (event.getEventType() == javafx.scene.input.KeyEvent.KEY_RELEASED) {
					dbActions.resetSearch();
				} else if (event.getEventType() == javafx.scene.input.KeyEvent.KEY_PRESSED) {
					// If the user hits the enter key in the search field and
					// there's only one item
					// in the listview then open that item (this code assumes
					// that the one item in
					// the listview has already been selected. this is done
					// automatically in the
					// DatabaseActions.filter() method)
					if (accountsListview.getItems().size() == 1) {
						viewAccountMenuItem.fire();
					}
				}
			}
		});
//		c.gridx = 1;
//		c.gridy = 2;
//		c.anchor = GridBagConstraints.LINE_START;
//		c.insets = new Insets(5, 1, 5, 1);
//		c.weightx = 0;
//		c.weighty = 0;
//		c.gridwidth = 1;
//		c.fill = GridBagConstraints.NONE;
		pane.getChildren().add(searchField);


		Image backgroundImage9 = new Image(getClass().getResourceAsStream("../util/images/stop.gif"));
		//resetSearchButton = new Button(Util.loadImage("stop.gif"));
		resetSearchButton = new Button();
		resetSearchButton.setGraphic(new ImageView(backgroundImage9));
		//resetSearchButton.setDisabledIcon(Util.loadImage("stop_d.gif"));
		resetSearchButton.setDisable(true);
		resetSearchButton.setTooltip(new Tooltip(RESET_SEARCH_TXT));
		resetSearchButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				resetSearchButton.setText(RESET_SEARCH_TXT);
			}
		});
		resetSearchButton.setOnAction(this);
//		Border other = new Border(new BorderStroke(javafx.scene.paint.Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
//		databaseFileChangedPanel.setBorder(other);
//		resetSearchButton.setBorder(other);
		resetSearchButton.setDisable(true);
//		c.gridx = 2;
//		c.gridy = 2;
//		c.anchor = GridBagConstraints.LINE_START;
//		c.insets = new Insets(5, 1, 5, 1);
//		c.weightx = 1;
//		c.weighty = 0;
//		c.gridwidth = 1;
//		c.fill = GridBagConstraints.NONE;
		pane.getChildren().add(resetSearchButton);

		// The accounts listview row
		accountsListview = new ListView();
		accountsListview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		accountsListview.getSelectionModel().selectFirst();
		//accountsListview.setVisibleRowCount(10);
		//accountsListview.setSelectionModel(new SortedListModel());
		ScrollPane accountsScrollList = new ScrollPane();
//		accountsListview.setFocusModel(new EventHandler<>() {
//			@Override
//			public void handle(Event event) {
//				// If the listview gets focus, there is one ore more items in
//				// the listview and there is nothing
//				// already selected, then select the first item in the list
//				if (accountsListview.getItems().size() > 0 && accountsListview.getEditingIndex() == -1) {
//					accountsListview.getSelectionModel().selectIndices(0, 0);
//				}
//			}
//		});
		accountsListview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				dbActions.setButtonState();
			}

//			public void valueChanged(ChangeListener e) {
//				dbActions.setButtonState();
//			}
		});
		accountsListview.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				if (event.getClickCount() == 2) {
					viewAccountMenuItem.fire();
				}
			}
		});
		accountsListview.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					viewAccountMenuItem.fire();
				}
			}
		});
		// Create a shortcut to delete account functionality with DEL(delete)
		// key

		accountsListview.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if (event.getCode() == KeyCode.DELETE) {
					try {
						dbActions.reloadDatabaseBefore(new DeleteAccountAction());
					} catch (InvalidPasswordException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ProblemReadingDatabaseFile e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});


//		c.gridx = 0;
//		c.gridy = 3;
//		c.anchor = GridBagConstraints.CENTER;
//		c.insets = new Insets(0, 1, 1, 1);
//		c.weightx = 1;
//		c.weighty = 1;
//		c.gridwidth = 3;
//		c.fill = GridBagConstraints.BOTH;
		pane.getChildren().add(accountsScrollList);

		// The "File Changed" panel
//		c.gridx = 0;
//		c.gridy = 4;
//		c.anchor = GridBagConstraints.CENTER;
//		c.insets = new Insets(0, 1, 0, 1);
//		c.ipadx = 3;
//		c.ipady = 3;
//		c.weightx = 0;
//		c.weighty = 0;
//		c.gridwidth = 3;
//		c.fill = GridBagConstraints.BOTH;
		databaseFileChangedPanel = new Pane();
		//databaseFileChangedPanel.setLayoutX(new ContentDisplay(databaseFileChangedPanel, ContentDisplay.LEFT));
		databaseFileChangedPanel.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.web("#F9AC3C"), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
		Border border = new Border(new BorderStroke(javafx.scene.paint.Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		databaseFileChangedPanel.setBorder(border);
		Label fileChangedLabel = new Label("Database file changed");
		fileChangedLabel.setAlignment(Pos.CENTER_LEFT);
		databaseFileChangedPanel.getChildren().add(fileChangedLabel);
		//databaseFileChangedPanel.getChildren().add(Box);
		Button reloadButton = new Button("Reload");
		reloadButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				try {
					dbActions.reloadDatabaseFromDisk();
				} catch (Exception ex)        {
					dbActions.errorHandler(ex);
				}
			}
		});

		databaseFileChangedPanel.getChildren().add(reloadButton);
		databaseFileChangedPanel.setVisible(false);
		pane.getChildren().add(databaseFileChangedPanel);

		// Add the statusbar
//		c.gridx = 0;
//		c.gridy = 5;
//		c.anchor = GridBagConstraints.CENTER;
//		c.insets = new Insets(0, 1, 1, 1);
//		c.weightx = 1;
//		c.weighty = 0;
//		c.gridwidth = 3;
//		c.fill = GridBagConstraints.HORIZONTAL;
		pane.getChildren().add(statusBar);
		return pane;
	}

	public void setFileChangedPanelVisible(boolean visible) {
		databaseFileChangedPanel.setVisible(visible);
	}

	private ToolBar createToolBar() {

		ToolBar toolbar = new ToolBar();
//		toolbar.setFloatable(false);
//		toolbar.setRollover(true);

		// The "Add Account" button
		Image backgroundImage = new Image(getClass().getResourceAsStream("../util/images/add_account.gif"));
		addAccountButton = new Button();
		addAccountButton.setGraphic(new ImageView(backgroundImage));
		addAccountButton.setTooltip(new Tooltip(ADD_ACCOUNT_TXT));
		//addAccountButton.setDisabledIcon(Util.loadImage("add_account_d.gif"));
		addAccountButton.setOnAction(this);
		addAccountButton.setDisable(true);
		addAccountButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				addAccountMenuItem.setText(ADD_ACCOUNT_TXT);
			}
		});

		toolbar.getItems().add(addAccountButton);

		// The "Edit Account" button
		Image backgroundImage2 = new Image(getClass().getResourceAsStream("../util/images/edit_account.gif"));
		//editAccountButton = new Button(Util.loadImage("edit_account.gif"));
		editAccountButton = new Button();
		editAccountButton.setGraphic(new ImageView(backgroundImage2));
		editAccountButton.setTooltip(new Tooltip(EDIT_ACCOUNT_TXT));
		//editAccountButton.setDisabledIcon(Util.loadImage("edit_account_d.gif"));
		editAccountButton.setOnAction(this);
		editAccountButton.setDisable(true);
		editAccountButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				editAccountMenuItem.setText(EDIT_ACCOUNT_TXT);
			}
		});
		toolbar.getItems().add(editAccountButton);

		// The "Delete Account" button
		Image backgroundImage3 = new Image(getClass().getResourceAsStream("../util/images/delete_account.gif"));
		//deleteAccountButton = new Button(Util.loadImage("delete_account.gif"));
		deleteAccountButton = new Button();
		deleteAccountButton.setGraphic(new ImageView(backgroundImage3));
		deleteAccountButton.setTooltip(new Tooltip(DELETE_ACCOUNT_TXT));
		//deleteAccountButton.setDisabledIcon(Util.loadImage("delete_account_d.gif"));
		deleteAccountButton.setOnAction(this);
		deleteAccountButton.setDisable(true);
		deleteAccountButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				deleteAccountMenuItem.setText(DELETE_ACCOUNT_TXT);
			}
		});
		toolbar.getItems().add(deleteAccountButton);

		toolbar.getItems().add(new Separator());

		// The "Copy Username" button
		Image backgroundImage4 = new Image(getClass().getResourceAsStream("../util/images/copy_username.gif"));
		//copyUsernameButton = new Button(Util.loadImage("copy_username.gif"));
		copyUsernameButton = new Button();
		copyUsernameButton.setGraphic(new ImageView(backgroundImage4));
		copyUsernameButton.setTooltip(new Tooltip(COPY_USERNAME_TXT));
		//copyUsernameButton.setDisabledIcon(Util.loadImage("copy_username_d.gif"));
		copyUsernameButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyUsernameToClipboard();
			}
		});
		copyUsernameButton.setDisable(true);
		toolbar.getItems().add(copyUsernameButton);

		// The "Copy Password" button
		Image backgroundImage5 = new Image(getClass().getResourceAsStream("../util/images/copy_password.gif"));
		//copyPasswordButton = new Button(Util.loadImage("copy_password.gif"));
		copyPasswordButton = new Button();
		copyPasswordButton.setGraphic(new ImageView(backgroundImage5));
		copyPasswordButton.setTooltip(new Tooltip(COPY_PASSWORD_TXT));
		//copyPasswordButton.setDisabledIcon(Util.loadImage("copy_password_d.gif"));
		copyPasswordButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				copyPasswordToClipboard();
			}
		});
		copyPasswordButton.setDisable(true);
		toolbar.getItems().add(copyPasswordButton);

		// The "Launch URL" button
		Image backgroundImage6 = new Image(getClass().getResourceAsStream("../util/images/launch_URL.gif"));
		//launchURLButton = new Button(Util.loadImage("launch_URL.gif"));
		launchURLButton = new Button();
		launchURLButton.setGraphic(new ImageView(backgroundImage6));
		launchURLButton.setTooltip(new Tooltip(LAUNCH_URL_TXT));
		//launchURLButton.setDisabledIcon(Util.loadImage("launch_URL_d.gif"));
		launchURLButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				AccountInformation accInfo = dbActions.getSelectedAccount();
				String uRl = accInfo.getUrl();

				// Check if the selected url is null or emty and inform the user
				// via JoptioPane message
				if ((uRl == null) || (uRl.length() == 0)) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("EmptyUrlJoptionpaneMsg");
					alert.setHeaderText(null);
					alert.setContentText("UrlErrorJoptionpaneTitle");
					alert.showAndWait();

					// Check if the selected url is a valid formated url(via
					// urlIsValid() method) and inform the user via JoptioPane
					// message
				} else if (!(urlIsValid(uRl))) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("InvalidUrlJoptionpaneMsg");
					alert.setHeaderText(null);
					alert.setContentText("UrlErrorJoptionpaneTitle");
					alert.showAndWait();
					// Call the method LaunchSelectedURL() using the selected
					// url as input
				} else {
					LaunchSelectedURL(uRl);

				}
			}
		});
		launchURLButton.setDisable(true);
		toolbar.getItems().add(launchURLButton);

		toolbar.getItems().add(new Separator());

		// The "Option" button
		Image backgroundImage7 = new Image(getClass().getResourceAsStream("../util/images/options.gif"));
		//optionsButton = new Button(Util.loadImage("options.gif"));
		optionsButton = new Button();
		optionsButton.setGraphic(new ImageView(backgroundImage7));
		optionsButton.setTooltip(new Tooltip(OPTIONS_TXT));
		//optionsButton.setDisabledIcon(Util.loadImage("options_d.gif"));
		optionsButton.setOnAction(this);
		optionsButton.setDisable(false);
		optionsButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				optionsButton.setText(OPTIONS_TXT);
			}
		});
		toolbar.getItems().add(optionsButton);

		toolbar.getItems().add(new Separator());

		// The Sync database button
		Image backgroundImage8 = new Image(getClass().getResourceAsStream("../util/images/sync.png"));
		//syncDatabaseButton = new Button(Util.loadImage("sync.png"));
		syncDatabaseButton = new Button();
		syncDatabaseButton.setGraphic(new ImageView(backgroundImage8));
		syncDatabaseButton.setTooltip(new Tooltip(SYNC_DATABASE_TXT));
		//syncDatabaseButton.setDisabledIcon(Util.loadImage("sync_d.png"));
		syncDatabaseButton.setOnAction(this);
		syncDatabaseButton.setDisable(true);
		syncDatabaseButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				syncWithRemoteDatabaseMenuItem.setText(SYNC_DATABASE_TXT);
			}
		});
		toolbar.getItems().add(syncDatabaseButton);

		return toolbar;
	}

	private MenuBar createMenuBar() {

		MenuBar menuBar = new MenuBar();
		databaseMenu = new Menu("Database Menu");//Translator.translate("databaseMenu"));
		//databaseMenu = new Menu();
		//databaseMenu.setText(Translator.translate("databaseMenu"));
		databaseMenu.setAccelerator(new KeyCodeCombination(KeyCode.D));
		menuBar.getMenus().add(databaseMenu);

		newDatabaseMenuItem = new MenuItem(NEW_DATABASE_TXT);
		newDatabaseMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.N));
		databaseMenu.getItems().add(newDatabaseMenuItem);
		newDatabaseMenuItem.setOnAction(this);

		openDatabaseMenuItem = new MenuItem(OPEN_DATABASE_TXT);
		openDatabaseMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.O));
		databaseMenu.getItems().add(openDatabaseMenuItem);
		openDatabaseMenuItem.setOnAction(this);

		openDatabaseFromURLMenuItem = new MenuItem(OPEN_DATABASE_FROM_URL_TXT);
		openDatabaseFromURLMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.L));
		databaseMenu.getItems().add(openDatabaseFromURLMenuItem);
		openDatabaseFromURLMenuItem.setOnAction(this);

//		databaseMenu.addSeparator();

		syncWithRemoteDatabaseMenuItem = new MenuItem(SYNC_DATABASE_TXT);
		syncWithRemoteDatabaseMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.S));
		databaseMenu.getItems().add(syncWithRemoteDatabaseMenuItem);
		syncWithRemoteDatabaseMenuItem.setOnAction(this);
		syncWithRemoteDatabaseMenuItem.setDisable(true);

		changeMasterPasswordMenuItem = new MenuItem(CHANGE_MASTER_PASSWORD_TXT);
		changeMasterPasswordMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.G));
		databaseMenu.getItems().add(changeMasterPasswordMenuItem);
		changeMasterPasswordMenuItem.setOnAction(this);
		changeMasterPasswordMenuItem.setDisable(true);

		databasePropertiesMenuItem = new MenuItem(DATABASE_PROPERTIES_TXT);
		databasePropertiesMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.S));
		databaseMenu.getItems().add(databasePropertiesMenuItem);
		databasePropertiesMenuItem.setOnAction(this);
		databasePropertiesMenuItem.setDisable(true);

//		databaseMenu.addSeparator();

		exportMenuItem = new MenuItem(EXPORT_TXT);
		databaseMenu.getItems().add(exportMenuItem);
		exportMenuItem.setOnAction(this);
		exportMenuItem.setDisable(false);

		importMenuItem = new MenuItem(IMPORT_TXT);
		databaseMenu.getItems().add(importMenuItem);
		importMenuItem.setOnAction(this);
		importMenuItem.setDisable(true);

		accountMenu = new Menu("accountMenu");
		accountMenu.setAccelerator(new KeyCodeCombination(KeyCode.A));
		menuBar.getMenus().add(accountMenu);

		addAccountMenuItem = new MenuItem(ADD_ACCOUNT_TXT);
		addAccountMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.A));
		accountMenu.getItems().add(addAccountMenuItem);
		addAccountMenuItem.setOnAction(this);
		addAccountMenuItem.setDisable(true);

		editAccountMenuItem = new MenuItem(EDIT_ACCOUNT_TXT);
		editAccountMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.E));
		accountMenu.getItems().add(editAccountMenuItem);
		editAccountMenuItem.setOnAction(this);
		editAccountMenuItem.setDisable(true);

		deleteAccountMenuItem = new MenuItem(DELETE_ACCOUNT_TXT);
		deleteAccountMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.D));
		accountMenu.getItems().add(deleteAccountMenuItem);
		deleteAccountMenuItem.setOnAction(this);
		deleteAccountMenuItem.setDisable(true);

		viewAccountMenuItem = new MenuItem(VIEW_ACCOUNT_TXT);
		viewAccountMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.V));
		accountMenu.getItems().add(viewAccountMenuItem);
		viewAccountMenuItem.setOnAction(this);
		viewAccountMenuItem.setDisable(true);

		copyUsernameMenuItem = new MenuItem(COPY_USERNAME_TXT);
		copyUsernameMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.U));
		accountMenu.getItems().add(copyUsernameMenuItem);
		copyUsernameMenuItem.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			public void handle(javafx.event.ActionEvent event) {
				copyUsernameToClipboard();
			}
		});
		copyUsernameMenuItem.setDisable(true);

		copyPasswordMenuItem = new MenuItem(COPY_PASSWORD_TXT);
		copyPasswordMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.P));
		accountMenu.getItems().add(copyPasswordMenuItem);
		copyPasswordMenuItem.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			public void handle(javafx.event.ActionEvent event) {
				copyPasswordToClipboard();
			}
		});
		copyPasswordMenuItem.setDisable(true);

		launchURLMenuItem = new MenuItem(LAUNCH_URL_TXT);
		launchURLMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.B));
		accountMenu.getItems().add(launchURLMenuItem);

		launchURLMenuItem.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				AccountInformation accountInformation = dbActions.getSelectedAccount();
				String url = accountInformation.getUrl();

				if((url == null) || (url.length() == 0)){
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("UrlErrorJoptionpaneTitle");
					alert.setContentText("EmptyUrlJoptionpaneMsg");
					alert.showAndWait();
				}
				else if (!(urlIsValid(url))) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("UrlErrorJoptionpaneTitle");
					alert.setContentText("InvalidUrlJoptionpaneMsg");
					alert.showAndWait();

					// Call the method LaunchSelectedURL() using the selected
					// url as input
				} else {
					LaunchSelectedURL(url);

				}
			}
		});

		launchURLMenuItem.setDisable(true);

		exitMenuItem = new MenuItem(EXIT_TXT);
		exitMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.X));
		exitMenuItem.setOnAction(this);

		aboutMenuItem = new MenuItem(ABOUT_TXT);
		aboutMenuItem.setOnAction(this);

		// Because the MAC version of UPM will have a program item in the menu
		// bar then these items
		// only need to be added on non-mac platforms
		if (!PlatformSpecificCode.isMAC()) {
//			databaseMenu.addSeparator();
			databaseMenu.getItems().add(exitMenuItem);

			helpMenu = new Menu("helpMenu");
			helpMenu.setAccelerator(new KeyCodeCombination(KeyCode.H));
			menuBar.getMenus().add(helpMenu);

			helpMenu.getItems().add(aboutMenuItem);
		}

		return menuBar;

	}

	public ListView getAccountsListview() {
		return accountsListview;
	}

	private void copyUsernameToClipboard() {
		AccountInformation accInfo = dbActions.getSelectedAccount();
		copyToClipboard(new String(accInfo.getUserId()));
	}

	private void copyPasswordToClipboard() {
		AccountInformation accInfo = dbActions.getSelectedAccount();
		copyToClipboard(new String(accInfo.getPassword()));
	}

	private void copyToClipboard(String s) {
		StringSelection stringSelection = new StringSelection(s);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
	}

	// Use com.apache.commons.validator library in order to check the
	// validity(proper formating, e.x http://www.url.com) of the given url
	private boolean urlIsValid(String urL) {

		UrlValidator urlValidator = new UrlValidator();
		if (urlValidator.isValid(urL)) {
			return true;
		} else {
			return false;
		}

	}

	// Method that get(as input) the selected Account URL and open this URL via
	// the default browser of our platform

	private void LaunchSelectedURL(String url) {

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			try {
				desktop.browse(new URI(url));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			// Linux and Mac specific code in order to launch url
		} else {
			Runtime runtime = Runtime.getRuntime();

			try {
				runtime.exec("xdg-open " + url);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Writes current window position and size to the preferences
	 */
	private void storeWindowBounds() {
		Preferences.set(Preferences.ApplicationOptions.XLOC, Integer.toString(window_X));
		Preferences.set(Preferences.ApplicationOptions.YLOC, Integer.toString(window_Y));
		Preferences.set(Preferences.ApplicationOptions.WWIDTH, Integer.toString(window_Width));
		Preferences.set(Preferences.ApplicationOptions.WHEIGHT, Integer.toString(window_Height));
	}

	/**
	 * Restores the window position and size to those found in the preferences
	 * Checks if the window can still be displayed, if not, revert to default
	 * position
	 */
	private void restoreWindowBounds() {
		int x = Preferences.getInt(Preferences.ApplicationOptions.XLOC, window_X);
		int y = Preferences.getInt(Preferences.ApplicationOptions.YLOC, window_Y);
		// check if this position can still be displayed to avoid problems
		// for people who dragged the window on a screen that is no longer
		// connected.
		if (getGraphicsConfigurationContaining(x, y) == null) {
			x = window_X;
			y = window_Y;
		}
		int width = Preferences.getInt(Preferences.ApplicationOptions.WWIDTH, window_Width);
		int height = Preferences.getInt(Preferences.ApplicationOptions.WHEIGHT, window_Height);

		//this.setBounds(x, y, width, height);
	}

	/**
	 * Utility function for restoreWindowBounds
	 */
	private GraphicsConfiguration getGraphicsConfigurationContaining(int x, int y) {
		ArrayList configs = new ArrayList();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		for (int i = 0; i < devices.length; i++) {
			GraphicsConfiguration[] gconfigs = devices[i].getConfigurations();
			configs.addAll(Arrays.asList(gconfigs));
		}
		for (int i = 0; i < configs.size(); i++) {
			GraphicsConfiguration config = ((GraphicsConfiguration) configs.get(i));
			Rectangle bounds = config.getBounds();
			if (bounds.contains(x, y)) {
				return config;
			}
		}
		return null;
	}

	/**
	 * Convenience method to iterate over all graphics configurations.
	 */
	private static ArrayList getConfigs() {
		ArrayList result = new ArrayList();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		for (int i = 0; i < devices.length; i++) {
			GraphicsConfiguration[] configs = devices[i].getConfigurations();
			result.addAll(Arrays.asList(configs));
		}
		return result;
	}

	public Button getCopyPasswordButton() {
		return copyPasswordButton;
	}

	public Button getLaunchURLButton() {
		return launchURLButton;
	}

	public Button getCopyUsernameButton() {
		return copyUsernameButton;
	}

	public Button getEditAccountButton() {
		return editAccountButton;
	}

	public Button getAddAccountButton() {
		return addAccountButton;
	}

	public Button getOptionsButton() {
		return optionsButton;
	}

	public Button getDeleteAccountButton() {
		return deleteAccountButton;
	}

	public TextField getSearchField() {
		return searchField;
	}

	public Label getSearchIcon() {
		return searchIcon;
	}

	public Button getResetSearchButton() {
		return resetSearchButton;
	}

	public MenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	public MenuItem getExitMenuItem() {
		return exitMenuItem;
	}

	public MenuItem getCopyPasswordMenuItem() {
		return copyPasswordMenuItem;
	}

	public MenuItem getLaunchURLMenuItem() {
		return launchURLMenuItem;
	}

	public MenuItem getCopyUsernameMenuItem() {
		return copyUsernameMenuItem;
	}

	public MenuItem getDeleteAccountMenuItem() {
		return deleteAccountMenuItem;
	}

	public MenuItem getViewAccountMenuItem() {
		return viewAccountMenuItem;
	}

	public MenuItem getEditAccountMenuItem() {
		return editAccountMenuItem;
	}

	public static String getApplicationName() {
		return applicationName;
	}

	public MenuItem getAddAccountMenuItem() {
		return addAccountMenuItem;
	}

	public MenuItem getChangeMasterPasswordMenuItem() {
		return changeMasterPasswordMenuItem;
	}

	public MenuItem getDatabasePropertiesMenuItem() {
		return databasePropertiesMenuItem;
	}

	@Override
	public void handle(Event event) {
		try{
			MenuItem item = (MenuItem) event.getSource();
			if(item.getText() == MainWindow.NEW_DATABASE_TXT){
				dbActions.newDatabase();
			} else if(item.getText() == MainWindow.OPEN_DATABASE_TXT){
				dbActions.openDatabase();
			} else if(item.getText() == MainWindow.OPEN_DATABASE_FROM_URL_TXT){
				dbActions.openDatabaseFromURL();
			} else if(item.getText() == MainWindow.SYNC_DATABASE_TXT){
				dbActions.syncWithRemoteDatabase();
			} else if(item.getText() == MainWindow.ADD_ACCOUNT_TXT){
				dbActions.reloadDatabaseBefore(new AddAccountAction());
			} else if(item.getText() == MainWindow.EDIT_ACCOUNT_TXT){
				String selectedAccName = (String) this.accountsListview.getSelectionModel().getSelectedItem();
				dbActions.reloadDatabaseBefore(new EditAccountAction(selectedAccName));
			} else if(item.getText() == MainWindow.DELETE_ACCOUNT_TXT){
				dbActions.reloadDatabaseBefore(new DeleteAccountAction());
			} else if(item.getText() == MainWindow.VIEW_ACCOUNT_TXT){
				//dbActions.viewAccount();
			} else if(item.getText() == MainWindow.OPTIONS_TXT){
				//dbActions.options();
			} else if(item.getText() == MainWindow.ABOUT_TXT){
				dbActions.showAbout();
			} else if(item.getText() == MainWindow.RESET_SEARCH_TXT){
				dbActions.resetSearch();
			} else if(item.getText() == MainWindow.CHANGE_MASTER_PASSWORD_TXT){
				dbActions.reloadDatabaseBefore(new ChangeMasterPasswordAction());
			} else if(item.getText() == MainWindow.DATABASE_PROPERTIES_TXT){
				dbActions.reloadDatabaseBefore(new ShowDatabasePropertiesAction());
			} else if(item.getText() == MainWindow.EXIT_TXT){
				dbActions.exitApplication();
			} else if(item.getText() == MainWindow.EXPORT_TXT){
				dbActions.export();
			} else if(item.getText() == MainWindow.IMPORT_TXT){
				dbActions.reloadDatabaseBefore(new ImportAccountsAction());
			}
		}catch(Exception e){

		}
	}
	public Button getSyncWithRemoteDatabaseButton() {
		return syncDatabaseButton;
	}

	public MenuItem getSyncWithRemoteDatabaseMenuItem() {
		return syncWithRemoteDatabaseMenuItem;
	}

	public MenuItem getExportMenuItem() {
		return exportMenuItem;
	}

	public MenuItem getImportMenuItem() {
		return importMenuItem;
	}

	public Label getStatusBar() {
		return statusBar;
	}

	public Pane getDatabaseFileChangedPanel() {
		return databaseFileChangedPanel;
	}

	/**
	 * Initialise all the menus, buttons, etc to take account of the language
	 * selected by the user
	 */
	public void initialiseControlsWithDefaultLanguage() {
		databaseMenu.setText("databaseMenu");
		newDatabaseMenuItem.setText(NEW_DATABASE_TXT);
		openDatabaseMenuItem.setText(OPEN_DATABASE_TXT);
		openDatabaseFromURLMenuItem.setText(OPEN_DATABASE_FROM_URL_TXT);
		syncWithRemoteDatabaseMenuItem.setText(SYNC_DATABASE_TXT);
		changeMasterPasswordMenuItem.setText(CHANGE_MASTER_PASSWORD_TXT);
		databasePropertiesMenuItem.setText(DATABASE_PROPERTIES_TXT);
		accountMenu.setText("accountMenu");
		addAccountMenuItem.setText(ADD_ACCOUNT_TXT);
		editAccountMenuItem.setText(EDIT_ACCOUNT_TXT);
		deleteAccountMenuItem.setText(DELETE_ACCOUNT_TXT);
		viewAccountMenuItem.setText(VIEW_ACCOUNT_TXT);
		copyUsernameMenuItem.setText(COPY_USERNAME_TXT);
		copyPasswordMenuItem.setText(COPY_PASSWORD_TXT);
		launchURLMenuItem.setText(LAUNCH_URL_TXT);
		exitMenuItem.setText(EXIT_TXT);
		aboutMenuItem.setText(ABOUT_TXT);
		exportMenuItem.setText(EXPORT_TXT);
		importMenuItem.setText(IMPORT_TXT);

		// Because the MAC version of UPM will have a program item in the menu
		// bar then these items
		// only need to be added on non-mac platforms
		if (!PlatformSpecificCode.isMAC()) {
			helpMenu.setText("helpMenu");
		}

		addAccountButton.setTooltip(new Tooltip(ADD_ACCOUNT_TXT));
		editAccountButton.setTooltip(new Tooltip(EDIT_ACCOUNT_TXT));
		deleteAccountButton.setTooltip(new Tooltip(DELETE_ACCOUNT_TXT));
		copyUsernameButton.setTooltip(new Tooltip(COPY_USERNAME_TXT));
		copyPasswordButton.setTooltip(new Tooltip(COPY_PASSWORD_TXT));
		launchURLButton.setTooltip(new Tooltip(LAUNCH_URL_TXT));
		optionsButton.setTooltip(new Tooltip(OPTIONS_TXT));
		syncDatabaseButton.setTooltip(new Tooltip(SYNC_DATABASE_TXT));
		resetSearchButton.setTooltip(new Tooltip(RESET_SEARCH_TXT));
	}

	public interface ChangeDatabaseAction {
		public void doAction();
	}

	private class EditAccountAction implements ChangeDatabaseAction {
		private String accountToEdit;

		public EditAccountAction(String accountToEdit) {
			this.accountToEdit = accountToEdit;
		}

		public void doAction() {
			try {
				//dbActions.editAccount(accountToEdit);
			} catch (Exception e) {
				dbActions.errorHandler(e);
			}
		}
	}

	private class ChangeMasterPasswordAction implements ChangeDatabaseAction {
		public void doAction() {
			try {
				dbActions.changeMasterPassword();
			} catch (Exception e) {
				dbActions.errorHandler(e);
			}
		}
	}

	private class DeleteAccountAction implements ChangeDatabaseAction {
		public void doAction() {
			try {
				dbActions.deleteAccount();
			} catch (Exception e) {
				dbActions.errorHandler(e);
			}
		}
	}

	private class AddAccountAction implements ChangeDatabaseAction {
		public void doAction() {
			try {
				//dbActions.addAccount();
			} catch (Exception e) {
				dbActions.errorHandler(e);
			}
		}
	}

	private class ShowDatabasePropertiesAction implements ChangeDatabaseAction {
		public void doAction() {
			try {
				//dbActions.showDatabaseProperties();
			} catch (Exception e) {
				dbActions.errorHandler(e);
			}
		}
	}

	private class ImportAccountsAction implements ChangeDatabaseAction {
		public void doAction() {
			try {
				dbActions.importAccounts();
			} catch (Exception e) {
				dbActions.errorHandler(e);
			}
		}
	}

}
