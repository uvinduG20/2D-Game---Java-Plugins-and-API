package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.ItemAcquiredListener;
import java.util.logging.Logger;

/**
 * Implementation of ItemAcquiredListener that reacts when the player acquires an item.
 */
public class ItemAcquiredListenerImpl implements ItemAcquiredListener {
    private static final Logger logger = Logger.getLogger(ItemAcquiredListenerImpl.class.getName());

    @Override
    public void onItemAcquired(String itemName) {
        // Log the acquired item
        logger.info(() -> "Player acquired item: " + itemName);

        // Log specific details if the acquired item is of particular interest
        if ("specialItem".equals(itemName)) {
            logger.info(() -> "Special item obtained! This will unlock additional abilities.");
        } else {
            logger.info(() -> "Player collected a regular item: " + itemName);
        }
    }
}
