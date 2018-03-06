package app.data;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class Statistics {

    private static ArrayList<Record> records = new ArrayList<>();

    public static void addRecord(String gameType, String winner) {
        records.add(new Record(gameType, winner));
    }

    public static Collection<Record> getRecords() {
        return records;
    }

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

        public String getDate() {
            return date.get();
        }

        public String getGameType() {
            return gameType.get();
        }

        public String getWinner() {
            return winner.get();
        }
    }
}
