package app.logic;

import app.util.Pair;
import app.util.Point;

import java.util.HashMap;

import static app.options.Constants.*;

/**
 * Describes logic of tic-tac-toe app.
 */
class GameLogic {

    private static HashMap<Integer, StateType> gameState;
    private static HashMap<Integer, GameStatus> gameStatus;


    /**
     * For every field state counts whether it is winning or losing and whether it contains winning row.
     */
    private static void countStates() {
        if (gameState == null) {
            gameState = new HashMap<>();
            gameStatus = new HashMap<>();
            dfs(new State(), 0);
        }
    }

    private static void dfs(State state, int depth) {
        if (state.getWinningRow() != null) {
            gameState.put(state.mask, StateType.Lose);
            gameStatus.put(state.mask, depth % 2 == 1 ? GameStatus.X : GameStatus.O);
            return;
        }
        if (depth == FIELD_COLS * FIELD_ROWS) {
            gameState.put(state.mask, StateType.Tie);
            gameStatus.put(state.mask, GameStatus.Tie);
            return;
        }

        boolean existsTie = false;
        boolean existsLose = false;

        for (int cellX = 0; cellX < FIELD_COLS; cellX++) {
            for (int cellY = 0; cellY < FIELD_ROWS; cellY++) {
                if (!state.isEmpty(cellX, cellY)) {
                    continue;
                }

                State nextState = state.makeTurn(cellX, cellY);
                if (!gameState.containsKey(nextState.mask)) {
                    dfs(nextState, depth + 1);
                }

                StateType type = gameState.get(nextState.mask);
                existsTie |= type == StateType.Tie;
                existsLose |= type == StateType.Lose;
            }
        }

        if (existsLose && !gameState.containsKey(state.mask)) {
            gameState.put(state.mask, StateType.Win);
        }
        if (existsTie && !gameState.containsKey(state.mask)) {
            gameState.put(state.mask, StateType.Tie);
        }
        if (!gameState.containsKey(state.mask)) {
            gameState.put(state.mask, StateType.Lose);
        }
    }

    /**
     * Describes field state. State includes current turn and positions of previous turns.
     */
    static class State {
        private static final int DIRECTIONS = 4;
        private static final int[] dx = new int[]{1, 1, 1, 0};
        private static final int[] dy = new int[]{-1, 0, 1, 1};

        private int mask = 0;
        private GameTurn turn = GameTurn.X;

        State() {}

        State(int mask, GameTurn turn) {
            this.mask = mask;
            this.turn = turn;
        }

        /**
         * Checks whether specified cell is empty.
         * @param cellX column
         * @param cellY row
         * @return {@code true} if cell is empty; {@code false} otherwise
         */
        boolean isEmpty(int cellX, int cellY) {
            return getCell(cellX, cellY) == FieldValue.None;
        }

        /**
         * Makes turn to the specified cell.
         * @param cellX column
         * @param cellY row
         * @return new state that describes field after this move
         */
        State makeTurn(int cellX, int cellY) {
            int id = cellX * FIELD_ROWS + cellY;
            int newMask = mask | turn.getValue() << (2 * id);
            return new State(newMask, turn.change());
        }

        /**
         * Returns current turn's role.
         * @return current turn's role
         */
        GameTurn getTurn() {
            return turn;
        }

        /**
         * Returns value that is placed to the specified cell of the field.
         * @param cellX column
         * @param cellY row
         * @return value that is placed to the specified cell of the field
         */
        FieldValue getCell(int cellX, int cellY) {
            int id = cellX * FIELD_ROWS + cellY;
            return FieldValue.get((mask >> (2 * id)) & 3);
        }

        /**
         * Returns winner of the current game or {@code None} if game is still in process.
         * @return winner of the current game or {@code None} if game is still in process
         */
        GameStatus getWinner() {
            countStates();
            if (!gameStatus.containsKey(mask)) {
                return GameStatus.None;
            }
            return gameStatus.get(mask);
        }

        /**
         * Says whether current state is winning, losing or tie.
         * @return {@code Win} if current state is winning; {@code Lose} if current state is losing;
         * {@code Tie} otherwise
         */
        StateType getStateType() {
            countStates();
            return gameState.get(mask);
        }

        /**
         * Returns extreme points of the winning row.
         * @return extreme points of the winning row; {@code null} if no winning row exists
         */
        Pair<Point, Point> getWinningRow() {
            for (int cellX = 0; cellX < FIELD_COLS; cellX++) {
                for (int cellY = 0; cellY < FIELD_ROWS; cellY++) {
                    if (isEmpty(cellX, cellY)) {
                        continue;
                    }
                    for (int direction = 0; direction < DIRECTIONS; direction++) {
                        boolean isWinning = true;
                        for (int dist = 1; dist < NEED_TO_WIN; dist++) {
                            int destX = cellX + dx[direction] * dist;
                            int destY = cellY + dy[direction] * dist;
                            isWinning &= isInside(destX, destY) && getCell(cellX, cellY) == getCell(destX, destY);
                        }
                        if (isWinning) {
                            int destX = cellX + dx[direction] * (NEED_TO_WIN - 1);
                            int destY = cellY + dy[direction] * (NEED_TO_WIN - 1);
                            return new Pair<>(new Point(cellX, cellY), new Point(destX, destY));
                        }
                    }
                }
            }
            return null;
        }

        private static boolean isInside(int cellX, int cellY) {
            return 0 <= cellX && cellX < FIELD_COLS && 0 <= cellY && cellY < FIELD_ROWS;
        }
    }
}
