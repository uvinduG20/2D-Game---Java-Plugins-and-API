package edu.curtin.gameplugins;

import edu.curtin.saed.api.GameInfo;
import edu.curtin.saed.api.PluginButtonListener;
import edu.curtin.saed.api.UIPlugin;

import java.util.Random;

public class Teleport implements UIPlugin {
    private final PluginButtonListener buttonListener;

    public Teleport() {
        this.buttonListener = new PluginButtonListener() {
            @Override
            public void onButtonPressed() {
                int[] gridSize = gameInfo.getGridSize();
                Random rand = new Random();
                int newRow, newCol;

                // Find a random empty spot
                do {
                    newRow = rand.nextInt(gridSize[0]);
                    newCol = rand.nextInt(gridSize[1]);
                } while (!"Empty".equals(gameInfo.getSquareContents(newRow, newCol)));

                // Clear the old player icon, update player's location, and set new position
                int[] currentLocation = gameInfo.getPlayerLocation();
                gameInfo.clearSquare(currentLocation[0], currentLocation[1]);  // Clear the old position
                gameInfo.setPlayerLocation(newRow, newCol);  // Update to the new position
            }

            @Override
            public String getButtonLabel() {
                return "Teleport";
            }
        };
    }

    private GameInfo gameInfo;

    @Override
    public void activate(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    @Override
    public PluginButtonListener getButtonListener() {
        return buttonListener;
    }
}
