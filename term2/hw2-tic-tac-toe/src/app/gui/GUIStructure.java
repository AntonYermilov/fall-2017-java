package app.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static app.options.Constants.WINDOW_HEIGHT;
import static app.options.Constants.WINDOW_WIDTH;

public class GUIStructure {

    private static Stage primaryStage;

    public static void initialize(Stage primaryStage) throws Exception {
        GUIStructure.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getMainScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/MainLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

    public static Scene getGameScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/GameLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

    public static Scene getSingleplayerModeScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/SingleplayerModeLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

    public static Scene getStatisticsScene() throws IOException {
        Parent layout = FXMLLoader.load(GUIStructure.class.getResource("layout/StatisticsLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        return scene;
    }

}
