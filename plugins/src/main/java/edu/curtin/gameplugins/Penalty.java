package edu.curtin.gameplugins;

import edu.curtin.saed.api.BackgroundPlugin;
import edu.curtin.saed.api.GameInfo;
import edu.curtin.saed.api.PlayerMoveListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Penalty implements BackgroundPlugin, PlayerMoveListener {
    private static final Logger logger = Logger.getLogger(Penalty.class.getName());
    private GameInfo gameInfo;
    private Timer timer;
    private long lastMoveTime;

    public Penalty() {
        this.lastMoveTime = System.currentTimeMillis();
    }

    @Override
    public void activate(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.timer = new Timer(true);

        // Schedule a task that checks for inactivity every second
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                // Check if 5 seconds have passed since the last move
                if (currentTime - lastMoveTime >= 5000) {
                    int[] location = gameInfo.getPlayerLocation();
                    gameInfo.addObstacleAdjacent(location[0], location[1]);
                    logger.info("Penalty applied: Obstacle created due to inactivity.");
                    lastMoveTime = currentTime; // Reset the lastMoveTime after penalty
                }
            }
        }, 1000, 1000); // Delay of 1 second and repeat every second
    }

    @Override
    public void onPlayerMove(String direction) {
        lastMoveTime = System.currentTimeMillis(); // Update the time of the last player move
    }

    @Override
    public void performBackgroundTask() {
        // Background task is handled by the TimerTask
    }
}
