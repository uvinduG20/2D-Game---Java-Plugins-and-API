package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.ItemAcquiredListener;
import edu.curtin.saed.api.PlayerMoveListener;
import edu.curtin.saed.api.PluginButtonListener;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Game {
    private static final int CELL_SIZE = 64;
    private static final int TOP_BAR_HEIGHT = 50;

    private GridArea gridArea;
    private Player player;
    private int rows, cols;
    private Stage stage;
    private TextFlow outputLog;
    private VBox inventoryDisplay;
    private ScrollPane scrollPane;
    private ResourceBundle bundle;
    private LocalDate gameDate;
    private int movesCount = 0;
    private boolean gameEnded = false;
    private PlayerMoveListener playerMoveListener;
    private ItemAcquiredListener itemAcquiredListener;
    private GameInfoImpl gameInfo;
    private ScriptManager scriptManager;
    private HBox topBar;
    private boolean localeFocusActive = false;

    public Game(Stage stage, GameConfig config) {
        this.stage = stage;
        this.rows = config.rows;
        this.cols = config.cols;
        this.gridArea = new GridArea(rows, cols);
        this.player = new Player(config.startRow, config.startCol);

        this.outputLog = new TextFlow();
        this.inventoryDisplay = new VBox();

        scrollPane = new ScrollPane(outputLog);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        scrollPane.setStyle("-fx-background-color: black; -fx-padding: 10;");

        this.gameDate = LocalDate.now();

        // Initialize GameInfoImpl, PlayerMoveListener, and ItemAcquiredListener
        gameInfo = new GameInfoImpl(player, gridArea, this);
        PlayerMoveListenerImpl moveListenerImpl = new PlayerMoveListenerImpl(gameInfo);
        this.playerMoveListener = moveListenerImpl;
        this.itemAcquiredListener = itemName -> {
            updateOutputLog("Item acquired: " + itemName);
            scriptManager.runScriptsOnItemAcquisition(itemName);
        };

        topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #333;");
        setupTopBar();

        // Initialize PluginManager and ScriptManager
        PluginManager pluginManager = new PluginManager(gameInfo);
        scriptManager = new ScriptManager(gameInfo);
        scriptManager.loadScripts(config.scripts);

        List<PluginButtonListener> pluginButtons = pluginManager.loadPlugins(config.plugins);

        // Add each plugin button to the topBar
        for (PluginButtonListener buttonListener : pluginButtons) {
            Button pluginButton = new Button(buttonListener.getButtonLabel());
            pluginButton.setOnAction(e -> {
                buttonListener.onButtonPressed();
                updatePlayerPositionAfterTeleport();
                gridArea.requestFocus();
            });
            topBar.getChildren().add(pluginButton);
        }

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(gridArea);

        initializeGame(config.goalRow, config.goalCol, config.items, config.obstacles);
    }

    public void initializeLocale() {
        setGameLocale(Locale.forLanguageTag("en-US"));
    }

    private void setupTopBar() {
        TextField localeInput = new TextField();
        localeInput.setPromptText("Enter locale (e.g., en-US)");
        localeInput.setOnMouseClicked(event -> localeFocusActive = true);

        Button changeLocaleButton = new Button("Change Locale");
        changeLocaleButton.setOnAction(event -> {
            String newLocaleTag = localeInput.getText().trim();
            if (!newLocaleTag.isEmpty()) {
                Locale newLocale = Locale.forLanguageTag(newLocaleTag);
                setGameLocale(newLocale);
                localeInput.clear();
                localeFocusActive = false;
                gridArea.requestFocus();
            }
        });
        topBar.getChildren().addAll(localeInput, changeLocaleButton);
    }

    private void setGameLocale(Locale locale) {
        try {
            bundle = ResourceBundle.getBundle("i18n.messages", locale);
            updateOutputLog("Locale set to " + locale.toLanguageTag());
        } catch (MissingResourceException e) {
            bundle = ResourceBundle.getBundle("i18n.messages", Locale.ENGLISH);
            updateOutputLog("Locale not supported, defaulting to English.");
        }
    }

    private void updatePlayerPositionAfterTeleport() {
        int[] newLocation = gameInfo.getPlayerLocation();
        gridArea.updatePlayerPosition(player.getRow(), player.getCol(), newLocation[0], newLocation[1]);
        player.move(newLocation[0], newLocation[1]);
    }

    public void start() {
        initializeLocale();  // Ensure locale is set before accessing bundle

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(gridArea);

        Label inventoryTitle = new Label(bundle.getString("inventory.title"));
        inventoryTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #ffd700;");
        inventoryDisplay.getChildren().clear();
        inventoryDisplay.getChildren().add(inventoryTitle);
        inventoryDisplay.setPadding(new Insets(10));
        inventoryDisplay.setStyle("-fx-background-color: #333; -fx-padding: 10;");

        VBox rightBox = new VBox(inventoryDisplay, new Label(bundle.getString("output.log.title")), scrollPane);
        rightBox.setPadding(new Insets(10));
        rightBox.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 16;");
        rightBox.setPrefWidth(350);

        root.setRight(rightBox);

        int sceneWidth = (cols * CELL_SIZE) + 350;
        int sceneHeight = (rows * CELL_SIZE) + TOP_BAR_HEIGHT;
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.setTitle(bundle.getString("game.title"));
        stage.show();

        gridArea.requestFocus();

        updateOutputLog(bundle.getString("game.start.message"));
        displayGameDate();
        updateInventoryDisplay();

        scene.setOnKeyPressed(event -> {
            if (!gameEnded) {
                KeyCode code = event.getCode();
                switch (code) {
                    case UP -> movePlayer("UP");
                    case DOWN -> movePlayer("DOWN");
                    case LEFT -> movePlayer("LEFT");
                    case RIGHT -> movePlayer("RIGHT");
                    default -> updateOutputLog(bundle.getString("invalid.move.key"));
                }
            }
        });

        gridArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!localeFocusActive && !isNowFocused) {
                gridArea.requestFocus();
            }
        });
    }

    private void initializeGame(int goalRow, int goalCol, List<Item> items, List<Obstacle> obstacles) {
        gridArea.setGoal(goalRow, goalCol);

        for (Item item : items) {
            for (Location location : item.getLocations()) {
                gridArea.addItem(location.getRow(), location.getCol(), item);
                gameInfo.setSquareVisible(location.getRow(), location.getCol(), true);
            }
        }

        for (Obstacle obstacle : obstacles) {
            for (Location location : obstacle.getLocations()) {
                gridArea.addObstacle(location.getRow(), location.getCol(), obstacle);
            }
        }

        gridArea.showInitialVisibleCells(player.getRow(), player.getCol());
        gridArea.updateCell(player.getRow(), player.getCol(), "player.png");
    }

    private void movePlayer(String direction) {
        if (gameEnded) {
            return;
        }

        int oldRow = player.getRow();
        int oldCol = player.getCol();
        int newRow = oldRow;
        int newCol = oldCol;

        switch (direction) {
            case "UP" -> newRow--;
            case "DOWN" -> newRow++;
            case "LEFT" -> newCol--;
            case "RIGHT" -> newCol++;
            default -> {
                updateOutputLog(bundle.getString("invalid.move.direction"));
                return;
            }
        }

        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
            if (gridArea.isObstacle(newRow, newCol)) {
                List<String> requiredItems = gridArea.getRequiredItems(newRow, newCol);
                if (requiredItems != null && requiredItems.stream().anyMatch(reqItem -> player.getInventory().stream().noneMatch(item -> item.equals(reqItem)))) {
                    updateOutputLog(bundle.getString("obstacle.requirements") + requiredItems);
                    return;
                } else {
                    gridArea.clearObstacle(newRow, newCol);
                }
            }

            gridArea.updatePlayerPosition(oldRow, oldCol, newRow, newCol);
            player.move(newRow, newCol);
            movesCount++;
            gameDate = gameDate.plusDays(1);
            displayGameDate();
            gridArea.showVisibleCellsAround(player.getRow(), player.getCol());

            if (playerMoveListener != null) {
                playerMoveListener.onPlayerMove(direction);
            }

            if (gridArea.isGoal(newRow, newCol)) {
                endGame();
            } else if (gridArea.hasItem(newRow, newCol)) {
                Item item = gridArea.pickupItem(newRow, newCol);
                player.collectItem(item);
                if (itemAcquiredListener != null) {
                    itemAcquiredListener.onItemAcquired(item.getName());
                }
                updateInventoryDisplay();
                updateOutputLog(bundle.getString("item.collected") + item.getName() + " - " + item.getMessage());
            }
        } else {
            updateOutputLog(bundle.getString("move.out.of.bounds"));
        }
    }

    private void updateInventoryDisplay() {
        inventoryDisplay.getChildren().clear();
        Label inventoryTitle = new Label(bundle.getString("inventory.title"));
        inventoryTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #ffd700;");
        inventoryDisplay.getChildren().add(inventoryTitle);

        for (String itemName : gameInfo.getPlayerInventory()) {
            Label itemLabel = new Label(itemName);
            itemLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            inventoryDisplay.getChildren().add(itemLabel);
        }
    }

    public void updateOutputLog(String message) {
        Text messageText = new Text(message + "\n");
        messageText.setStyle("-fx-fill: #000000; -fx-font-size: 14;");
        outputLog.getChildren().add(messageText);
        scrollPane.setVvalue(1.0);
    }

    private void displayGameDate() {
        String formattedDate = gameDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(bundle.getLocale()));
        updateOutputLog(bundle.getString("current.date") + ": " + formattedDate);
    }

    private void endGame() {
        updateOutputLog(bundle.getString("game.win.message"));
        updateOutputLog(bundle.getString("total.days") + ": " + movesCount);
        gameEnded = true;
    }

    public HBox getTopBar() {
        return topBar;
    }
}
