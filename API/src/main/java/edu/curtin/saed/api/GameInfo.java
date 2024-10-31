package edu.curtin.saed.api;

import java.util.List;

public interface GameInfo {
    int[] getPlayerLocation(); // Get player's current location
    void setPlayerLocation(int row, int col);
    List<String> getPlayerInventory(); // Get player's inventory
    int[] getGridSize(); // Get grid dimensions
    Object getSquareContents(int row, int col); // Get contents of a grid square
    boolean isSquareVisible(int row, int col); // Check if a square is visible
    void setSquareVisible(int row, int col, boolean visible); // Set square visibility
    void addObstacleAdjacent(int row, int col);
    void clearSquare(int row, int col); // Clears the player icon at the specified position
    void addItemToInventory(String itemName);
    void showMessage(String message);


}
