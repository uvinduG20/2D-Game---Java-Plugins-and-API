package edu.curtin.saed.assignment1;

import edu.curtin.saed.api.GameInfo;
import org.python.util.PythonInterpreter;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyException;

import java.util.logging.Logger;

public class JythonScript {
    private static final Logger logger = Logger.getLogger(JythonScript.class.getName());
    private String scriptContent;
    private GameInfo gameInfo;
    private PythonInterpreter interpreter;

    public JythonScript(String scriptContent, GameInfo gameInfo) {
        this.scriptContent = scriptContent;
        this.gameInfo = gameInfo;
        this.interpreter = new PythonInterpreter();

        // Initialize the script if it's valid Python content
        if (isValidPythonScript(scriptContent)) {
            initializeScript();
        } else {
            logger.warning("Invalid Python script content detected, skipping initialization.");
        }
    }

    private void initializeScript() {
        try {
            logger.info(() -> "Initializing script:\n" + scriptContent);
            interpreter.set("api", gameInfo);
            interpreter.exec(scriptContent);
            logger.info(() -> "Script initialized successfully.");
        } catch (PyException pyEx) {
            logger.warning(() -> "Python interpreter error during script initialization: " + pyEx.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warning(() -> "Failed to initialize script due to argument or state issue: " + e.getMessage());
        }
    }

    public void executeOnItemAcquisition(String itemName) {
        try {
            PyObject handler = interpreter.get("handleItemAcquisition");
            if (handler != null) {
                handler.__call__(interpreter.get("api"), new PyString(itemName));
                logger.info(() -> "Executed item acquisition script for item: " + itemName);
            } else {
                logger.warning(() -> "No 'handleItemAcquisition' function found in script.");
            }
        } catch (PyException pyEx) {
            logger.warning(() -> "Python interpreter error during item acquisition execution: " + pyEx.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warning(() -> "Failed to execute item acquisition script due to argument or state issue: " + e.getMessage());
        }
    }

    // Method to check if the script is valid Python syntax
    private boolean isValidPythonScript(String script) {
        return script != null && script.trim().length() > 0 && script.trim().contains("def ");
    }
}
