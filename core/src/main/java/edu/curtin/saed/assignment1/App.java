package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Use the argument passed for the input file, or default if not provided
        String inputFilePath = getParameters().getRaw().isEmpty()
                ? "../testinput-0.utf8.map" // default file if no argument is given
                : getParameters().getRaw().get(0);

        try {
            // Initialize parser and parse the input file
            GameConfigParser parser = new GameConfigParser(new FileReader(inputFilePath));
            parser.parseInputFile();

            // Log parsed data for verification
            logger.info(() -> "Grid Size: " + parser.rows + "x" + parser.cols);
            logger.info(() -> "Start Position: (" + parser.startRow + "," + parser.startCol + ")");
            logger.info(() -> "Goal Position: (" + parser.goalRow + "," + parser.goalCol + ")");
            logger.info(() -> "Items: " + parser.items);
            logger.info(() -> "Obstacles: " + parser.obstacles);
            logger.info(() -> "Plugins: " + parser.plugins);
            logger.info(() -> "Scripts: " + parser.scripts);

            // Create a GameConfig object to store parsed data
            GameConfig config = new GameConfig(
                    parser.rows,
                    parser.cols,
                    parser.startRow,
                    parser.startCol,
                    parser.goalRow,
                    parser.goalCol,
                    parser.items,
                    parser.obstacles,
                    parser.plugins,
                    parser.scripts
            );

            // Create and start the game with GameConfig
            Game game = new Game(primaryStage, config);
            game.start();

        } catch (FileNotFoundException e) {
            logger.severe(() -> "File not found: " + inputFilePath);
        } catch (IOException | ParseException e) {
            logger.severe(() -> "I/O error while reading the file: " + e.getMessage());
        }
    }
}
