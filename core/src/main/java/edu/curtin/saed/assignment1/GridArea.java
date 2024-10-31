package edu.curtin.saed.assignment1;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import java.util.List;

public class GridArea extends GridPane {
    private int rows;
    private int cols;
    private Cell[][] cells;
    private int playerRow;
    private int playerCol;

    public GridArea(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        initialize();
    }

    private void initialize() {
        // Initialize grid cells with a default hidden state
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
                Label cellLabel = new Label();
                cellLabel.setGraphic(new GridAreaIcon("black.png").getIcon()); // Default hidden state
                add(cellLabel, col, row);
            }
        }
    }

    public void showInitialVisibleCells(int startRow, int startCol) {
        playerRow = startRow;
        playerCol = startCol;
        revealCell(startRow, startCol);
        revealCell(startRow - 1, startCol);
        revealCell(startRow + 1, startCol);
        revealCell(startRow, startCol - 1);
        revealCell(startRow, startCol + 1);
        updateCell(startRow, startCol, "player.png"); // Ensure initial cell shows player
    }

    public void showVisibleCellsAround(int row, int col) {
        revealCell(row, col);
        revealCell(row - 1, col);
        revealCell(row + 1, col);
        revealCell(row, col - 1);
        revealCell(row, col + 1);
    }

    private void revealCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            Label cellLabel = (Label) getChildren().get(row * cols + col);
            Cell cell = cells[row][col];
            cell.setVisible(true); // Mark the cell as visible

            if (row == playerRow && col == playerCol) {
                cellLabel.setGraphic(new GridAreaIcon("player.png").getIcon());
            } else if (cell.isGoal()) {
                cellLabel.setGraphic(new GridAreaIcon("goal.png").getIcon());
            } else if (cell.hasItem()) {
                cellLabel.setGraphic(new GridAreaIcon("item.png").getIcon());
            } else if (cell.isObstacle()) {
                cellLabel.setGraphic(new GridAreaIcon("obstacle.png").getIcon());
            } else {
                cellLabel.setGraphic(new GridAreaIcon("default.png").getIcon());
            }
        }
    }

    public void updatePlayerPosition(int oldRow, int oldCol, int newRow, int newCol) {
        updateCell(oldRow, oldCol, "default.png");
        playerRow = newRow;
        playerCol = newCol;
        updateCell(newRow, newCol, "player.png");
    }

    public void updateCell(int row, int col, String imageName) {
        Label cellLabel = (Label) getChildren().get(row * cols + col);
        cellLabel.setGraphic(new GridAreaIcon(imageName).getIcon());
    }

    public void setGoal(int row, int col) {
        cells[row][col].setGoal(true);
    }

    public void addItem(int row, int col, Item item) {
        cells[row][col].setItem(true);
        cells[row][col].setItemName(item.getName());
        cells[row][col].setItemMessage(item.getMessage());
    }

    public void addObstacle(int row, int col, Obstacle obstacle) {
        cells[row][col].setObstacle(true);
        cells[row][col].setRequiredItems(obstacle.getRequiredItems());
    }

    public Item pickupItem(int row, int col) {
        if (cells[row][col].hasItem()) {
            String itemName = cells[row][col].getItemName();
            String itemMessage = cells[row][col].getItemMessage();
            cells[row][col].clearItem();
            updateCell(row, col, "player.png");
            return new Item(itemName, null, itemMessage);
        }
        return null;
    }

    public boolean isGoal(int row, int col) {
        return cells[row][col].isGoal();
    }

    public boolean hasItem(int row, int col) {
        return cells[row][col].hasItem();
    }

    public boolean isObstacle(int row, int col) {
        return cells[row][col].isObstacle();
    }

    public List<String> getRequiredItems(int row, int col) {
        return cells[row][col].getRequiredItems();
    }

    public void clearObstacle(int row, int col) {
        cells[row][col].clearObstacle();
        updateCell(row, col, "default.png");
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return cells[row][col];
        }
        return null;
    }

    // New Methods Added for GameInfoImpl

    // Returns the number of rows
    public int getRows() {
        return rows;
    }

    // Returns the number of columns
    public int getCols() {
        return cols;
    }

    // Check if a specific square is visible
    public boolean isSquareVisible(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return cells[row][col].isVisible();
        }
        return false;
    }

    // Set visibility for a specific square
    public void setSquareVisible(int row, int col, boolean visible) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cells[row][col].setVisible(visible);
        }
    }
}
