package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.ConfirmBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class AppController {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magneta");
    private RoboRally roboRally;
    private GameController gameController;


    public AppController(RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // if (!stopGame){
                return;
            }
            ;
        }
        Board board = new Board(8, 8);
        gameController = new GameController(board);

        int no = result.get();
        for (int i = 0; i < no; i++) {
            Player player = new Player(board, "", "", (i + 1));
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));

        }
        board.setCurrentPlayer(board.getPlayer(0));
        roboRally.createBoardView(gameController);
        gameController.initializeProgrammingPhase();
    }

    //
    public void saveGame() {
        //TODO need to be implemented
    }

    public void exit() {
        // TODO needs to be implemented
        Boolean svar = ConfirmBox.display("Exit","Hov! Er du nu helt sikker?");
        if(svar)
            saveGame();


    }
}
