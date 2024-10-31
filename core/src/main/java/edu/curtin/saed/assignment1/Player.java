package edu.curtin.saed.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int row;
    private int col;
    private List<Item> inventory;  // Store Item objects

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
        this.inventory = new ArrayList<>();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    public void collectItem(Item item) {
        inventory.add(item);  // Store the Item object directly
    }

    public List<Item> getInventory() {
        return inventory;  // Return a list of Item objects
    }
}
