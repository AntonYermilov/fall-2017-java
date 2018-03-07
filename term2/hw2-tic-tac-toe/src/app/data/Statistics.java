package app.data;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores information about all played games in current session.
 */
public class Statistics {

    private static ArrayList<Record> records = new ArrayList<>();

    /**
     * Adds record to statistics table. Record includes type of played game and winner's role.
     * @param gameType type of played game
     * @param winner winner's role (X or O)
     */
    public static void addRecord(String gameType, String winner) {
        records.add(new Record(gameType, winner));
    }

    /**
     * Returns list of existing records.
     * @return list of existing records
     */
    public static Collection<Record> getRecords() {
        return records;
    }

    /**
     * Describes result of each game. Stores date of game, its type and winner's role.
     */
    public static class Record {
        private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd HH:mm");

        public final SimpleStringProperty date;
        public final SimpleStringProperty gameType;
        public final SimpleStringProperty winner;

        public Record(String gameType, String winner) {
            this.date = new SimpleStringProperty(dateFormat.format(LocalDateTime.now()));
            this.gameType = new SimpleStringProperty(gameType);
            this.winner = new SimpleStringProperty(winner);
        }

        /**
         * Returns date of played game.
         * @return date of played game
         */
        public String getDate() {
            return date.get();
        }

        /**
         * Returns type of played game.
         * @return type of played game
         */
        public String getGameType() {
            return gameType.get();
        }

        /**
         * Returns winner of played game.
         * @return winner of played game
         */
        public String getWinner() {
            return winner.get();
        }
    }
}
