package edu.curtin.saed.assignment1;

import java.util.List;

public class Cell {
    private boolean isGoal;
    private boolean hasItem;
    private boolean isObstacle;
    private String itemName;
    private String itemMessage;
    private List<String> requiredItems;
    private boolean isVisible; // New field to track cell visibility

    public Cell() {
        this.isGoal = false;
        this.hasItem = false;
        this.isObstacle = false;
        this.itemName = null;
        this.itemMessage = null;
        this.requiredItems = null;
        this.isVisible = false; // Cells start as not visible
    }

    // Goal-related methods
    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal(boolean isGoal) {
        this.isGoal = isGoal;
    }

    // Item-related methods
    public boolean hasItem() {
        return hasItem;
    }

    public void setItem(boolean hasItem) {
        this.hasItem = hasItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemMessage() {
        return itemMessage;
    }

    public void setItemMessage(String itemMessage) {
        this.itemMessage = itemMessage;
    }

    // Obstacle-related methods
    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    public List<String> getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(List<String> requiredItems) {
        this.requiredItems = requiredItems;
    }

    // Method to clear item data when an item is picked up
    public void clearItem() {
        this.hasItem = false;
        this.itemName = null;
        this.itemMessage = null;
    }

    // Method to clear obstacle data after passing the obstacle
    public void clearObstacle() {
        this.isObstacle = false;
        this.requiredItems = null;
    }

    // Visibility methods
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
