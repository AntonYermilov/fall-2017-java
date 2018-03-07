package app.handler;

import app.data.Statistics;
import app.gui.GUIStructure;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.IOException;

public class StatisticsHandler {

    @FXML private TableView<Statistics.Record> tableView;

    /**
     * Loads main menu scene.
     * @throws IOException if any error occurred
     */
    @FXML public void loadPreviousScene() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getMainScene());
    }

    /**
     * Initializes table to show statistics.
     */
    @FXML public void initialize() {
        ObservableList<Statistics.Record> data = tableView.getItems();
        data.addAll(Statistics.getRecords());
    }
}
