package me.eranik.app.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.eranik.app.logic.GameLogic;
import me.eranik.app.options.Constants;
import me.eranik.app.util.Pair;

import static me.eranik.app.options.Constants.WINDOW_HEIGHT;
import static me.eranik.app.options.Constants.WINDOW_WIDTH;

/**
 * Describes activity that starts app.
 */
public class MainActivity extends Application {

    private static int fieldSize;
    private static int cellWidth;
    private static int cellHeight;

    private static Button buttons[][];

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected one argument: <field size>");
            System.err.println("Field size should be an even number between 2 and 10");
            System.exit(1);
        }

        fieldSize = Integer.parseInt(args[0]);
        if (fieldSize < 2 || fieldSize > 10 || fieldSize % 2 != 0) {
            System.err.println("Field size should be an even number between 2 and 10");
            System.exit(1);
        }

        cellWidth = Constants.FIELD_WIDTH / fieldSize;
        cellHeight = Constants.FIELD_HEIGHT / fieldSize;
        buttons = new Button[fieldSize][fieldSize];

        launch(args);
    }

    /**
     * Starts app elements' drawing.
     * @param primaryStage specified primary stage.
     */
    @Override
    public void start(Stage primaryStage) {
        GameLogic.initialize(fieldSize);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        for (int row = 0; row < fieldSize; row++) {
            for (int column = 0; column < fieldSize; column++) {
                Button button = new Button();
                button.setUserData(new Pair<>(row, column));
                button.setMinWidth(cellWidth);
                button.setMinHeight(cellHeight);
                button.setOnMouseClicked(this::handleMouseClick);
                gridPane.add(button, row, column);
                buttons[row][column] = button;
            }
        }

        Scene scene = new Scene(gridPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startGame() {
        GameLogic.initialize(fieldSize);
        for (int row = 0; row < fieldSize; row++) {
            for (int column = 0; column < fieldSize; column++) {
                buttons[row][column].setText("");
                buttons[row][column].setDisable(false);
            }
        }
    }

    /**
     * Handles mouse click on button and shows it's text.
     * @param event mouse click event
     */
    @SuppressWarnings("unchecked")
    public void handleMouseClick(MouseEvent event) {
        Button button = (Button) event.getSource();

        Pair<Integer, Integer> position = (Pair<Integer, Integer>) button.getUserData();
        if (GameLogic.alreadyOpened(position)) {
            return;
        }

        GameLogic.open(position);
        button.setText(String.valueOf(GameLogic.getNumber(position)));

        if (GameLogic.countAlreadyOpened() == 2) {
            Pair<Integer, Integer> positionFirst = GameLogic.getPositionFirst();
            Pair<Integer, Integer> positionSecond = GameLogic.getPositionSecond();
            if (GameLogic.equal()) {
                GameLogic.setOpened();
                buttons[positionFirst.first][positionFirst.second].setDisable(true);
                buttons[positionSecond.first][positionSecond.second].setDisable(true);
            } else {
                new Timeline(new KeyFrame(Duration.millis(500), task -> {
                    if (!GameLogic.alreadyOpened(positionFirst)) {
                        buttons[positionFirst.first][positionFirst.second].setText("");
                    }
                    if (!GameLogic.alreadyOpened(positionSecond)) {
                        buttons[positionSecond.first][positionSecond.second].setText("");
                    }
                })).play();
            }
            GameLogic.close();
        }

        if (GameLogic.isFinished()) {
            new Timeline(new KeyFrame(Duration.millis(1500), task -> startGame())).play();
        }
    }

}
