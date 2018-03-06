package app.logic;

import app.util.Pair;
import app.util.Point;

import static app.options.Constants.*;
import static app.logic.GameLogic.State;

public class GameProcess {

    static GameType gameType;
    static State currentState;

    public static void setGameType(GameType gameType) {
        GameProcess.gameType = gameType;
    }

    public static void initialize() {
        currentState = new State();
    }

    public static void makeTurn(int cellX, int cellY) {
        currentState = currentState.makeTurn(cellX, cellY);
    }

    public static GameType getGameType() {
        return gameType;
    }

    public static GameTurn getCurrentTurn() {
        return currentState.getTurn();
    }

    public static Pair<Point, Point> getWinningRow() {
        return currentState.getWinningRow();
    }

    public static GameStatus getWinner() {
        return currentState.getWinner();
    }

    public static boolean isFinished() {
        return currentState.getWinner() != GameStatus.None;
    }

    public static boolean possibleTurn(int cellX, int cellY) {
        return !isFinished() && currentState.isEmpty(cellX, cellY);
    }

}
