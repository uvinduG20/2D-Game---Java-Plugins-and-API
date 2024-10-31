package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.PlayerMoveListener;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerMoveListenerImpl implements PlayerMoveListener {
    private static final Logger logger = Logger.getLogger(PlayerMoveListenerImpl.class.getName());
    private final GameInfoImpl gameInfo;
    private boolean penaltyEnabled;
    private Timer inactivityTimer;

    public PlayerMoveListenerImpl(GameInfoImpl gameInfo) {
        this.gameInfo = gameInfo;
        this.penaltyEnabled = false;
    }

    public void setPenaltyEnabled(boolean enabled) {
        this.penaltyEnabled = enabled;
        if (enabled) {
            startInactivityTimer();
        }
    }

    @Override
    public void onPlayerMove(String direction) {
        logger.info(() -> "Player moved in direction: " + direction);
        resetInactivityTimer();

        int[] playerLocation = gameInfo.getPlayerLocation();
        int playerRow = playerLocation[0];
        int playerCol = playerLocation[1];
        logger.info(() -> "Player is now at row " + playerRow + ", column " + playerCol);

        Object squareContents = gameInfo.getSquareContents(playerRow, playerCol);
        if ("Goal".equals(squareContents)) {
            logger.info(() -> "Player reached the goal!");
        } else if (squareContents instanceof String item) {
            logger.info(() -> "Player encountered item: " + item);
        } else if ("Obstacle".equals(squareContents)) {
            logger.info(() -> "Player encountered an obstacle.");
        }
    }

    private void startInactivityTimer() {
        inactivityTimer = new Timer(true);
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (penaltyEnabled) {
                    int[] location = gameInfo.getPlayerLocation();
                    gameInfo.addObstacleAdjacent(location[0], location[1]);
                    logger.info(() -> "Obstacle created due to inactivity.");
                }
            }
        }, 5000, 5000);  // 5-second delay, repeating every 5 seconds if inactive
    }

    private void resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        startInactivityTimer();  // Restart timer on player move
    }
}
