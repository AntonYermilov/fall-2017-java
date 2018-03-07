package app.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Describes activity that starts app.
 */
public class MainActivity extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts app elements' drawing.
     * @param primaryStage specified primary stage.
     * @throws Exception if any error occurred
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        GUIStructure.initialize(primaryStage);

        primaryStage.setResizable(false);
        primaryStage.setScene(GUIStructure.getMainScene());
        primaryStage.show();
    }
}
