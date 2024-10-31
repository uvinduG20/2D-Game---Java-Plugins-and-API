package edu.curtin.saed.assignment1;

import java.util.List;

public class GameConfig {
    public int rows;
    public int cols;
    public int startRow;
    public int startCol;
    public int goalRow;
    public int goalCol;
    public List<Item> items;
    public List<Obstacle> obstacles;
    public List<String> plugins;
    public List<String> scripts;

    public GameConfig(int rows, int cols, int startRow, int startCol, int goalRow, int goalCol,
                      List<Item> items, List<Obstacle> obstacles, List<String> plugins, List<String> scripts) {
        this.rows = rows;
        this.cols = cols;
        this.startRow = startRow;
        this.startCol = startCol;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.items = items;
        this.obstacles = obstacles;
        this.plugins = plugins;
        this.scripts = scripts;
    }
}
