package app.options;

public class Constants {
    public static final int WINDOW_WIDTH = 430;
    public static final int WINDOW_HEIGHT = 430;
    public static final int FIELD_WIDTH = 300;
    public static final int FIELD_HEIGHT = 300;
    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 100;
    public static final int CELL_EPS = 15;

    public static final int FIELD_ROWS = 3;
    public static final int FIELD_COLS = 3;

    public static final int NEED_TO_WIN = 3;

    public enum GameTurn {
        X(1), O(2);
        private final int value;

        GameTurn(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public GameTurn change() {
            return this == X ? O : X;
        }
    }

    public enum FieldValue {
        X(1), O(2), None(0);
        private final int value;

        FieldValue(int value) {
            this.value = value;
        }

        public static FieldValue get(int value) {
            switch (value) {
                case 1:
                    return X;
                case 2:
                    return O;
                default:
                    return None;
            }
        }

        public int getValue() {
            return value;
        }
    }

    public enum GameStatus {
        X, O, Tie, None
    }

    public enum StateType {
        Win, Lose, Tie
    }

    public enum GameType {
        Multiplayer, SingleplayerEasy, SingleplayerHard;

        public static GameType get(String name) {
            switch (name) {
                case "Multiplayer":
                    return Multiplayer;
                case "Easy":
                    return SingleplayerEasy;
                case "Hard":
                    return SingleplayerHard;
            }
            return null;
        }
    }
}
