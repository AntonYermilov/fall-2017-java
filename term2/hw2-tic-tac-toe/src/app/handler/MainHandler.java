package app.handler;

import app.gui.GUIStructure;
import app.logic.GameProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static app.options.Constants.GameType;

public class MainHandler {

    @FXML public void loadGameScene(ActionEvent actionEvent) throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getGameScene());

        GameType type = GameType.get(((Button) actionEvent.getSource()).getText());
        GameProcess.setGameType(type);
    }

    @FXML public void loadSingleplayerModeScene() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getSingleplayerModeScene());
    }

    @FXML public void loadPreviousScene() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getMainScene());
    }

    @FXML public void loadStatistics() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getStatisticsScene());
    }

    @FXML public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
