package app.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static app.options.Constants.*;

/**
 * Describes drawing of game elements.
 */
public class GameActivity {

    private static Canvas canvas;
    private static Text gameResult;
    private static Text gameProcess;

    /**
     * Saves scene parameters, such as field and text menus.
     * @param canvas game field
     * @param gameResult text menu with results of game
     * @param gameProcess text menu with game's process
     */
    public static void initialize(Canvas canvas, Text gameResult, Text gameProcess) {
        GameActivity.canvas = canvas;
        GameActivity.gameResult = gameResult;
        GameActivity.gameProcess = gameProcess;
    }

    /**
     * Shows game winner.
     * @param status game winner
     */
    public static void showResult(GameStatus status) {
        switch (status) {
            case X:
                gameResult.setText("X wins");
                break;
            case O:
                gameResult.setText("O wins");
                break;
            case Tie:
                gameResult.setText("Tie");
                break;
            default:
                gameResult.setText("");
                break;
        }
    }

    /**
     * Shows current game process and turn.
     * @param status game process
     */
    public static void showProcess(GameStatus status) {
        switch (status) {
            case X:
                gameProcess.setText("X turn");
                break;
            case O:
                gameProcess.setText("O turn");
                break;
            case None:
                gameProcess.setText("Finished");
                break;
        }
    }

    /**
     * Creates field for playing tic-tac-toe.
     */
    public static void drawField() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(7);

        gc.strokeLine(0, CELL_HEIGHT, FIELD_WIDTH, CELL_HEIGHT);
        gc.strokeLine(0, 2 * CELL_HEIGHT, FIELD_WIDTH, 2 * CELL_HEIGHT);
        gc.strokeLine(CELL_WIDTH, 0, CELL_WIDTH, FIELD_HEIGHT);
        gc.strokeLine(2 * CELL_WIDTH, 0, 2 * CELL_WIDTH, FIELD_HEIGHT);
    }

    /**
     * Draws X to the specified cell.
     * @param cellX column index
     * @param cellY row index
     */
    public static void drawX(int cellX, int cellY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(8);

        gc.strokeLine(cellX * CELL_WIDTH + CELL_EPS, cellY * CELL_HEIGHT + CELL_EPS,
                (cellX + 1) * CELL_WIDTH - CELL_EPS, (cellY + 1) * CELL_HEIGHT - CELL_EPS);
        gc.strokeLine(cellX * CELL_WIDTH + CELL_EPS, (cellY + 1) * CELL_HEIGHT - CELL_EPS,
                (cellX + 1) * CELL_WIDTH - CELL_EPS, cellY * CELL_HEIGHT + CELL_EPS);
    }

    /**
     * Draws O to the specified cell.
     * @param cellX column index
     * @param cellY row index
     */
    public static void drawO(int cellX, int cellY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(8);

        gc.strokeOval(cellX * CELL_WIDTH + CELL_EPS, cellY * CELL_HEIGHT + CELL_EPS,
                CELL_WIDTH - 2 * CELL_EPS, CELL_HEIGHT - 2 * CELL_EPS);
    }


    /**
     * Draws line with ends in the specified cells.
     * @param cellX1 column index of first cell
     * @param cellY1 row index of first cell
     * @param cellX2 column index of second cell
     * @param cellY2 row index of second cell
     */
    public static void drawLine(int cellX1, int cellY1, int cellX2, int cellY2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        int dx = (cellX2 - cellX1) / (NEED_TO_WIN - 1);
        int dy = (cellY2 - cellY1) / (NEED_TO_WIN - 1);

        int x1 = cellX1 * CELL_WIDTH + CELL_WIDTH / 2 - dx * (CELL_WIDTH - 2 * CELL_EPS) / 2;
        int y1 = cellY1 * CELL_HEIGHT + CELL_HEIGHT / 2 - dy * (CELL_HEIGHT - 2 * CELL_EPS) / 2;
        int x2 = cellX2 * CELL_WIDTH + CELL_WIDTH / 2 + dx * (CELL_WIDTH - 2 * CELL_EPS) / 2;
        int y2 = cellY2 * CELL_HEIGHT + CELL_HEIGHT / 2 + dy * (CELL_HEIGHT - 2 * CELL_EPS) / 2;
        gc.strokeLine(x1, y1, x2, y2);
    }
}
