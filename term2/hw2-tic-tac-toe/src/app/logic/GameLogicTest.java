package app.logic;

import app.util.Pair;
import app.util.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static app.logic.GameLogic.State;
import static app.options.Constants.*;

class GameLogicTest {

    static GameLogic.State fieldToState(FieldValue[][] field) {
        int mask = 0;
        int crosses = 0;
        int noughts = 0;
        for (int cellX = 0; cellX < FIELD_COLS; cellX++) {
            for (int cellY = 0; cellY < FIELD_ROWS; cellY++) {
                int id = cellX * FIELD_ROWS + cellY;
                mask |= field[cellX][cellY].getValue() << (2 * id);
                crosses += field[cellX][cellY] == FieldValue.X ? 1 : 0;
                noughts += field[cellX][cellY] == FieldValue.O ? 1 : 0;
            }
        }
        return new State(mask, crosses == noughts ? GameTurn.X : GameTurn.O);
    }

    @Test
    void testWinnerFirstRowX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.X, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.None},
                {FieldValue.None, FieldValue.None, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerSecondRowX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.None, FieldValue.None},
                {FieldValue.X, FieldValue.X, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerThirdRowX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.O, FieldValue.None},
                {FieldValue.None, FieldValue.None, FieldValue.None},
                {FieldValue.X, FieldValue.X, FieldValue.X}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerFirstColumnX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.O, FieldValue.None},
                {FieldValue.X, FieldValue.O, FieldValue.None},
                {FieldValue.X, FieldValue.None, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerSecondColumnX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.X, FieldValue.O},
                {FieldValue.None, FieldValue.X, FieldValue.O},
                {FieldValue.None, FieldValue.X, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerThirdColumnX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.None, FieldValue.X},
                {FieldValue.None, FieldValue.None, FieldValue.X}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerFirstDiagonalX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.O, FieldValue.X},
                {FieldValue.O, FieldValue.X, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.X}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerSecondDiagonalX() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.O, FieldValue.X, FieldValue.X},
                {FieldValue.X, FieldValue.O, FieldValue.X}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.X, state.getWinner());
    }

    @Test
    void testWinnerFirstRowO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.O, FieldValue.O},
                {FieldValue.None, FieldValue.X, FieldValue.X},
                {FieldValue.X, FieldValue.None, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerSecondRowO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.O},
                {FieldValue.X, FieldValue.X, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerThirdRowO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.None, FieldValue.None},
                {FieldValue.X, FieldValue.X, FieldValue.None},
                {FieldValue.O, FieldValue.O, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerFirstColumnO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.X, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerSecondColumnO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.O, FieldValue.X},
                {FieldValue.X, FieldValue.O, FieldValue.None},
                {FieldValue.None, FieldValue.O, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerThirdColumnO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.X, FieldValue.O},
                {FieldValue.None, FieldValue.X, FieldValue.O},
                {FieldValue.X, FieldValue.None, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerFirstDiagonalO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.X, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.None, FieldValue.X, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerSecondDiagonalO() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.X, FieldValue.O},
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.O, FieldValue.None, FieldValue.X}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.O, state.getWinner());
    }

    @Test
    void testWinnerEmpty() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.None, FieldValue.None},
                {FieldValue.None, FieldValue.None, FieldValue.None},
                {FieldValue.None, FieldValue.None, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.None, state.getWinner());
    }

    @Test
    void testWinnerFull() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.X, FieldValue.O},
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.X, FieldValue.X, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(GameStatus.Tie, state.getWinner());
    }

    @Test
    void testWinningRowFirstRow() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.O, FieldValue.O},
                {FieldValue.None, FieldValue.X, FieldValue.X},
                {FieldValue.X, FieldValue.None, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(0, 0), new Point(0, 2)), state.getWinningRow());
    }

    @Test
    void testWinningRowSecondRow() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.O},
                {FieldValue.X, FieldValue.X, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(1, 0), new Point(1, 2)), state.getWinningRow());
    }

    @Test
    void testWinningRowThirdRow() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.None, FieldValue.None},
                {FieldValue.X, FieldValue.X, FieldValue.None},
                {FieldValue.O, FieldValue.O, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(2, 0), new Point(2, 2)), state.getWinningRow());
    }

    @Test
    void testWinningRowFirstColumn() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.None, FieldValue.X},
                {FieldValue.O, FieldValue.X, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(0, 0), new Point(2, 0)), state.getWinningRow());
    }

    @Test
    void testWinningRowSecondColumn() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.O, FieldValue.X},
                {FieldValue.X, FieldValue.O, FieldValue.None},
                {FieldValue.None, FieldValue.O, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(0, 1), new Point(2, 1)), state.getWinningRow());
    }

    @Test
    void testWinningRowThirdColumn() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.X, FieldValue.O},
                {FieldValue.None, FieldValue.X, FieldValue.O},
                {FieldValue.X, FieldValue.None, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(0, 2), new Point(2, 2)), state.getWinningRow());
    }

    @Test
    void testWinningRowFirstDiagonal() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.O, FieldValue.X, FieldValue.X},
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.None, FieldValue.X, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(0, 0), new Point(2, 2)), state.getWinningRow());
    }

    @Test
    void testWinningRowSecondDiagonal() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.X, FieldValue.O},
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.O, FieldValue.None, FieldValue.X}
        };
        State state = fieldToState(field);
        assertEquals(new Pair<>(new Point(0, 2), new Point(2, 0)), state.getWinningRow());
    }

    @Test
    void testWinningRowEmpty() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.None, FieldValue.None, FieldValue.None},
                {FieldValue.None, FieldValue.None, FieldValue.None},
                {FieldValue.None, FieldValue.None, FieldValue.None}
        };
        State state = fieldToState(field);
        assertEquals(null, state.getWinningRow());
    }

    @Test
    void testWinningRowFull() {
        FieldValue[][] field = new FieldValue[][]{
                {FieldValue.X, FieldValue.X, FieldValue.O},
                {FieldValue.O, FieldValue.O, FieldValue.X},
                {FieldValue.X, FieldValue.X, FieldValue.O}
        };
        State state = fieldToState(field);
        assertEquals(null, state.getWinningRow());
    }

    @Test
    void testTurns() {
        State state = new State();
        assertEquals(GameTurn.X, state.getTurn());
        state = state.makeTurn(0, 0);
        assertEquals(GameTurn.O, state.getTurn());
        state = state.makeTurn(0, 1);
        assertEquals(GameTurn.X, state.getTurn());
        state = state.makeTurn(0, 2);
        assertEquals(GameTurn.O, state.getTurn());
    }

}