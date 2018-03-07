package app.handler;

import app.gui.GUIStructure;
import app.logic.GameProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static app.options.Constants.GameType;

/**
 * Handles event that happen in different menus.
 */
public class MainHandler {

    /**
     * Loads game scene to play tic-tac-toe. Saves chosen game type.
     * @param actionEvent event of clicking on button that moves you to the scene with game field
     * @throws IOException if any error occurred
     */
    @FXML public void loadGameScene(ActionEvent actionEvent) throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getGameScene());

        GameType type = GameType.get(((Button) actionEvent.getSource()).getText());
        GameProcess.setGameType(type);
    }

    /**
     * Loads scene that allows you to chose singleplayer mode.
     * @throws IOException if any error occurred
     */
    @FXML public void loadSingleplayerModeScene() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getSingleplayerModeScene());
    }

    /**
     * Loads previous scene. That may be either main menu scene or singleplayer-mode scene.
     * @throws IOException if any error occurred
     */
    @FXML public void loadPreviousScene() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getMainScene());
    }

    /**
     * Loads scene that shows current statistics of winnings and losings.
     * @throws IOException if any error occurred
     */
    @FXML public void loadStatistics() throws IOException {
        GUIStructure.getPrimaryStage().setScene(GUIStructure.getStatisticsScene());
    }

    /**
     * Exits game.
     */
    @FXML public void exit() {
        System.exit(0);
    }
}
