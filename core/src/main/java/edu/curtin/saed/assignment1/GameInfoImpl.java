package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.GameInfo;
import java.util.ArrayList;
import java.util.List;

public class GameInfoImpl implements GameInfo {
    private Player player;
    private GridArea gridArea;
    private Game game;

    public GameInfoImpl(Player player, GridArea gridArea, Game game) {
        this.player = player;
        this.gridArea = gridArea;
        this.game = game;
    }

    @Override
    public int[] getPlayerLocation() {
        return new int[]{player.getRow(), player.getCol()};
    }

    @Override
    public void setPlayerLocation(int row, int col) {
        player.move(row, col);
        gridArea.updatePlayerPosition(player.getRow(), player.getCol(), row, col);
    }

    @Override
    public List<String> getPlayerInventory() {
        return player.getInventory().stream().map(Item::getName).toList();
    }

    @Override
    public int[] getGridSize() {
        return new int[]{gridArea.getRows(), gridArea.getCols()};
    }

    @Override
    public Object getSquareContents(int row, int col) {
        if (gridArea.isGoal(row, col)) {
            return "Goal";
        } else if (gridArea.hasItem(row, col)) {
            return gridArea.getCell(row, col).getItemName();
        } else if (gridArea.isObstacle(row, col)) {
            return "Obstacle";
        }
        return "Empty";
    }

    @Override
    public boolean isSquareVisible(int row, int col) {
        return gridArea.isSquareVisible(row, col);
    }

    @Override
    public void setSquareVisible(int row, int col, boolean visible) {
        gridArea.setSquareVisible(row, col, visible);
    }

    @Override
    public void addObstacleAdjacent(int row, int col) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < gridArea.getRows() && newCol >= 0 && newCol < gridArea.getCols()) {
                List<Location> obstacleLocations = new ArrayList<>();
                obstacleLocations.add(new Location(newRow, newCol));
                List<String> requiredItems = new ArrayList<>();

                Obstacle newObstacle = new Obstacle(obstacleLocations, requiredItems);
                gridArea.addObstacle(newRow, newCol, newObstacle);
            }
        }
    }

    @Override
    public void clearSquare(int row, int col) {
        gridArea.updateCell(row, col, "default.png");
    }

    @Override
    public void addItemToInventory(String itemName) {
        List<Location> emptyLocations = new ArrayList<>(); // Empty list for direct inventory
        String defaultMessage = "Special Item Acquired";   // Default message if needed
        Item newItem = new Item(itemName, emptyLocations, defaultMessage);
        player.collectItem(newItem); // Adds the item to the player's inventory
    }

    @Override
    public void showMessage(String message) {
        game.updateOutputLog(message);
    }
}
