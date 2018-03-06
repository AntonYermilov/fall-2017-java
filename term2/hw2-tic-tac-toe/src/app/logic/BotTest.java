package app.logic;

import app.util.Point;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

import static app.logic.GameLogic.State;
import static app.options.Constants.*;

class BotTest {

    @RepeatedTest(10)
    void testEasyBotXHardBotO() {
        State state = new State();
        int turn = 0;
        while (state.getWinner() == GameStatus.None) {
            if (turn == 0) {
                Point point = Bot.easyStrategy(state);
                state = state.makeTurn(point.x, point.y);
            } else {
                Point point = Bot.hardStrategy(state);
                state = state.makeTurn(point.x, point.y);
            }
            turn ^= 1;
        }
        assertTrue(state.getWinner() != GameStatus.X);
    }

    @RepeatedTest(10)
    void testEasyBotOHardBotX() {
        State state = new State();
        int turn = 0;
        while (state.getWinner() == GameStatus.None) {
            if (turn == 0) {
                Point point = Bot.hardStrategy(state);
                state = state.makeTurn(point.x, point.y);
            } else {
                Point point = Bot.easyStrategy(state);
                state = state.makeTurn(point.x, point.y);
            }
            turn ^= 1;
        }
        assertTrue(state.getWinner() != GameStatus.O);
    }

    @RepeatedTest(10)
    void testHardBotXHardBotO() {
        State state = new State();
        int turn = 0;
        while (state.getWinner() == GameStatus.None) {
            if (turn == 0) {
                Point point = Bot.hardStrategy(state);
                state = state.makeTurn(point.x, point.y);
            } else {
                Point point = Bot.hardStrategy(state);
                state = state.makeTurn(point.x, point.y);
            }
            turn ^= 1;
        }
        assertTrue(state.getWinner() == GameStatus.Tie);
    }
}