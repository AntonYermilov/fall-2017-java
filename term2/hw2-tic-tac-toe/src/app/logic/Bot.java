package app.logic;

import app.util.Point;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static app.options.Constants.*;
import static app.logic.GameLogic.State;

public class Bot {

    private static Random random = new Random(new Date().getTime());

    public static Point makeTurn() {
        switch (GameProcess.getGameType()) {
            case SingleplayerEasy:
                return easyStrategy(GameProcess.currentState);
            case SingleplayerHard:
                return hardStrategy(GameProcess.currentState);
        }
        return null;
    }

    static Point easyStrategy(State state) {
        ArrayList<Point> emptyCells = new ArrayList<>();
        for (int cellX = 0; cellX < FIELD_COLS; cellX++) {
            for (int cellY = 0; cellY < FIELD_ROWS; cellY++) {
                if (state.isEmpty(cellX, cellY)) {
                    emptyCells.add(new Point(cellX, cellY));
                }
            }
        }
        return emptyCells.get(random.nextInt(emptyCells.size()));
    }

    static Point hardStrategy(State state) {
        Point winTurn = null;
        Point tieTurn = null;
        Point anyTurn = null;

        for (int cellX = 0; cellX < FIELD_COLS; cellX++) {
            for (int cellY = 0; cellY < FIELD_ROWS; cellY++) {
                if (!state.isEmpty(cellX, cellY)) {
                    continue;
                }
                State nextState = state.makeTurn(cellX, cellY);
                if (nextState.getStateType() == StateType.Lose) {
                    winTurn = new Point(cellX, cellY);
                }
                if (nextState.getStateType() == StateType.Tie) {
                    tieTurn = new Point(cellX, cellY);
                }
                anyTurn = new Point(cellX, cellY);
            }
        }

        if (winTurn != null) {
            return winTurn;
        }
        if (tieTurn != null) {
            return tieTurn;
        }
        return anyTurn;
    }

}
