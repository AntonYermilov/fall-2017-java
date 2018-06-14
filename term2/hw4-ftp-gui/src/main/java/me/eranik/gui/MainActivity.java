package me.eranik.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.eranik.ftp.Client;

import java.io.IOException;

/**
 * Describes activity that starts FTP app.
 */
public class MainActivity extends Application {

    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 500;

    private static String hostName;
    private static int portNumber;

    /**
     * Receives two arguments: host name and port number. After that starts the app.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Expected 2 arguments: <host name> <port number>");
            System.exit(1);
        }

        hostName = args[0];

        try {
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Port number should be an integer.");
            System.exit(2);
        }

        launch(args);
    }

    /**
     * Starts app. Connects to server and lists files in the root directory.
     * @param primaryStage specified primary stage.
     * @throws IOException if any error occurred
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Client.initialize(hostName, portNumber);

        Parent layout = FXMLLoader.load(MainActivity.class.getResource("/MainLayout.fxml"));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
