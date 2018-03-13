package me.eranik.app.logic;

import me.eranik.app.util.Pair;
import me.eranik.app.util.Point;

import static me.eranik.app.options.Constants.*;
import static me.eranik.app.logic.GameLogic.State;

/**
 * Provides functionality for game handler to process moves of players and bots.
 */
public class GameProcess {

    static GameType gameType;
    static State currentState;

    /**
     * Sets current game type.
     * @param gameType specified type of the current game
     */
    public static void setGameType(GameType gameType) {
        GameProcess.gameType = gameType;
    }

    /**
     * Initializes game and creates state that corresponds to the empty field.
     */
    public static void initialize() {
        currentState = new State();
    }

    /**
     * Makes turn to the specified cell.
     * @param cellX column
     * @param cellY row
     */
    public static void makeTurn(int cellX, int cellY) {
        currentState = currentState.makeTurn(cellX, cellY);
    }

    /**
     * Returns current game type
     * @return current game type
     */
    public static GameType getGameType() {
        return gameType;
    }

    /**
     * Returns current turn
     * @return current turn
     */
    public static GameTurn getCurrentTurn() {
        return currentState.getTurn();
    }

    /**
     * Returns winning row if exists.
     * @return winning row if exists; {@code null} otherwise
     */
    public static Pair<Point, Point> getWinningRow() {
        return currentState.getWinningRow();
    }

    /**
     * Returns winner of the game if exists.
     * @return winner of the game if exists; {@code None} otherwise
     */
    public static GameStatus getWinner() {
        return currentState.getWinner();
    }

    /**
     * Checks whether the game is finished or not.
     * @return {@code true} if game is finished; {@code false} otherwise
     */
    public static boolean isFinished() {
        return currentState.getWinner() != GameStatus.None;
    }

    /**
     * Says if it is possible to make a move to the specified cell.
     * @param cellX column
     * @param cellY row
     * @return {@code true} if it is possible to make a move to the specified cell; {@code false} otherwise
     */
    public static boolean possibleTurn(int cellX, int cellY) {
        return !isFinished() && currentState.isEmpty(cellX, cellY);
    }

}
