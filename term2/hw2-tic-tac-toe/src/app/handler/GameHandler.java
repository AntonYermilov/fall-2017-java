package app.handler;

import app.data.Statistics;
import app.gui.GUIStructure;
import app.gui.GameActivity;
import app.logic.Bot;
import app.logic.GameProcess;
import app.util.Pair;
import app.util.Point;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;

import static app.options.Constants.*;

public class GameHandler {

    @FXML public Canvas canvas;
    @FXML public Text gameResult;
    @FXML public Text gameProcess;

    @FXML public void initialize() {
        GameActivity.initialize(canvas, gameResult, gameProcess);
        startGame();
    }

    @FXML public void loadPreviousScene() throws IOException {
        switch (GameProcess.getGameType()) {
            case Multiplayer:
                GUIStructure.getPrimaryStage().setScene(GUIStructure.getMainScene());
                break;
            default:
                GUIStructure.getPrimaryStage().setScene(GUIStructure.getSingleplayerModeScene());
                break;
        }
    }

    @FXML public void startGame() {
        GameActivity.drawField();
        GameActivity.showResult(GameStatus.None);
        GameActivity.showProcess(GameStatus.X);
        GameProcess.initialize();
    }

    @FXML public void clickedOnField(MouseEvent mouseEvent) {
        int cellX = Math.min(FIELD_COLS - 1, (int) mouseEvent.getX() / CELL_WIDTH);
        int cellY = Math.min(FIELD_ROWS - 1, (int) mouseEvent.getY() / CELL_HEIGHT);

        if (!GameProcess.possibleTurn(cellX, cellY)) {
            return;
        }

        makeTurn(cellX, cellY);
        GameProcess.makeTurn(cellX, cellY);

        if (!GameProcess.isFinished()) {
            Point botTurn = Bot.makeTurn();
            if (botTurn != null) {
                makeTurn(botTurn.x, botTurn.y);
                GameProcess.makeTurn(botTurn.x, botTurn.y);
            }
        }

        finish();
    }

    private static void makeTurn(int cellX, int cellY) {
        switch (GameProcess.getCurrentTurn()) {
            case X:
                GameActivity.drawX(cellX, cellY);
                break;
            case O:
                GameActivity.drawO(cellX, cellY);
                break;
        }
    }

    private static void finish() {
        if (!GameProcess.isFinished()) {
            GameActivity.showProcess(GameProcess.getCurrentTurn() == GameTurn.X ? GameStatus.X : GameStatus.O);
            return;
        }

        Pair<Point, Point> winningRow = GameProcess.getWinningRow();
        if (winningRow != null) {
            GameActivity.drawLine(winningRow.first.x, winningRow.first.y, winningRow.second.x, winningRow.second.y);
        }
        GameActivity.showResult(GameProcess.getWinner());
        GameActivity.showProcess(GameStatus.None);
        Statistics.addRecord(GameProcess.getGameType().name(), GameProcess.getWinner().name());
    }
}
