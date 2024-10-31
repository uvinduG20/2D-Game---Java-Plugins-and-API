package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.GameInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ScriptManager {
    private static final Logger logger = Logger.getLogger(ScriptManager.class.getName());
    private GameInfo gameInfo;
    private List<JythonScript> loadedScripts;

    // Constructor accepting GameInfo to provide game context to scripts
    public ScriptManager(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.loadedScripts = new ArrayList<>();
    }

    // Loads scripts from the list of script strings
    public void loadScripts(List<String> scripts) {
        for (String script : scripts) {
            try {
                JythonScript jythonScript = new JythonScript(script, gameInfo); // Updated constructor
                loadedScripts.add(jythonScript);
                logger.info(() -> "Script loaded successfully: " + script);
            } catch (IllegalArgumentException | IllegalStateException e) {  // Specific exceptions
                logger.warning(() -> "Failed to load script: " + script + " - " + e.getMessage());
            }
        }
    }

    // Runs specific scripts triggered by item acquisition
    public void runScriptsOnItemAcquisition(String itemName) {
        for (JythonScript script : loadedScripts) {
            script.executeOnItemAcquisition(itemName); // Calls the appropriate method in JythonScript
        }
    }
}
