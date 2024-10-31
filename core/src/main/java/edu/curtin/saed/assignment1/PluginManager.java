// PluginManager.java
package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.GameInfo;
import edu.curtin.saed.api.UIPlugin;
import edu.curtin.saed.api.BackgroundPlugin;
import edu.curtin.saed.api.PluginButtonListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PluginManager {
    private static final Logger logger = Logger.getLogger(PluginManager.class.getName());
    private final GameInfo gameInfo;
    private List<BackgroundPlugin> backgroundPlugins;

    public PluginManager(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.backgroundPlugins = new ArrayList<>();
    }

    public List<PluginButtonListener> loadPlugins(List<String> pluginClassNames) {
        List<PluginButtonListener> buttonListeners = new ArrayList<>();

        for (String pluginClassName : pluginClassNames) {
            try {
                Class<?> pluginClass = Class.forName(pluginClassName);
                Object pluginInstance = pluginClass.getDeclaredConstructor().newInstance();

                if (pluginInstance instanceof UIPlugin uiPlugin) {
                    uiPlugin.activate(gameInfo);
                    buttonListeners.add(uiPlugin.getButtonListener());
                    logger.info(() -> "UI Plugin " + pluginClassName + " loaded successfully.");
                } else if (pluginInstance instanceof BackgroundPlugin bgPlugin) {
                    bgPlugin.activate(gameInfo);
                    backgroundPlugins.add(bgPlugin);
                    logger.info(() -> "Background Plugin " + pluginClassName + " loaded successfully.");
                } else {
                    logger.warning(() -> "Class " + pluginClassName + " is not a recognized plugin type.");
                }
            } catch (ReflectiveOperationException | ClassCastException e) {
                logger.warning(() -> "Failed to load plugin " + pluginClassName + ": " + e.getMessage());
            }
        }
        return buttonListeners;
    }

    public void checkBackgroundPlugins() {
        for (BackgroundPlugin bgPlugin : backgroundPlugins) {
            bgPlugin.performBackgroundTask();
        }
    }
}
