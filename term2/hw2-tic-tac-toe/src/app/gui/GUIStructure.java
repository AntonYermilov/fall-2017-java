package app.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static app.options.Constants.WINDOW_HEIGHT;
import static app.options.Constants.WINDOW_WIDTH;

/**
 * Stores primary stage and provides easy creating of scenes for switching between them.
 */
public class GUIStructure {

    private static Stage primaryStage;

    /**
     * Saves primary stage.
     * @param primaryStage specified stage
     * @throws Exception if any error occurred
     */
    public static void initialize(Stage primaryStage) throws Exception {
        GUIStructure.primaryStage = primaryStage;
    }

    /**
     * Returns primary stage.
     * @return primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Returns scene that describes main menu.
     * @return scene that describes main menu
     * @throws IOException if any error when searching layout occurred
     */
    public static Scene getMainScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/MainLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

    /**
     * Returns scene that describes game field and everything connected with game process.
     * @return scene that describes game menu
     * @throws IOException if any error when searching layout occurred
     */
    public static Scene getGameScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/GameLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

    /**
     * Returns scene that describes singleplayer bot types.
     * @return scene that describes singleplayer bot types
     * @throws IOException if any error when searching layout occurred
     */
    public static Scene getSingleplayerModeScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/SingleplayerModeLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

    /**
     * Returns scene that describes statistics of played games.
     * @return scene that describes statistics of played games
     * @throws IOException if any error when searching layout occurred
     */
    public static Scene getStatisticsScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/StatisticsLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

}
