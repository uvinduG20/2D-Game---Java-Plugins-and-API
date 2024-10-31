package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.PluginButtonListener;
import java.util.logging.Logger;

/**
 * Implementation of PluginButtonListener that responds to plugin button presses.
 */
public class PluginButtonListenerImpl implements PluginButtonListener {
    private static final Logger logger = Logger.getLogger(PluginButtonListenerImpl.class.getName());
    private final String pluginName;

    public PluginButtonListenerImpl(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public void onButtonPressed() {
        // Log or trigger the action associated with the plugin button
        logger.info(() -> "Plugin button '" + pluginName + "' pressed.");
        // Additional plugin-specific actions can be added here
    }

    @Override
    public String getButtonLabel() {
        return pluginName;  // Returns the plugin name as the button label
    }
}
