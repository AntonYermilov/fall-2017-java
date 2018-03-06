package app.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainActivity extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUIStructure.initialize(primaryStage);

        primaryStage.setResizable(false);
        primaryStage.setScene(GUIStructure.getMainScene());
        primaryStage.show();
    }
}
