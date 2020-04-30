package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Account;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.ConfirmBox;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AppController {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "magneta", "cyan");
    final private String fieldChoice[] = {"Default", "Dizzy Highway", "Risky Crossing", "High Octane", "BurnOut"};

    private RoboRally roboRally;
    private GameController gameController;
    private boolean saveNeeded;

    public AppController(RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            ChoiceDialog d = new ChoiceDialog(fieldChoice[0], fieldChoice);
            d.setHeaderText("Field Choice");
            d.setContentText("Please select the Field you wanna game on");
            Optional<String> optional = d.showAndWait();

            if (optional.isPresent())
                if (gameController != null)
                    // if (!stopGame()){
                    return;
        }

        //TODO: her skal der vælges hvilket board der skal bruges
        Board board = LoadBoard.loadBoard(null);
        gameController = new GameController(board);

        int no = result.get();
        for (int i = 0; i < no; i++) {
            Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1), i, new Account());
            board.addPlayer(player);
            //her bliver spillere sat
            player.setSpace(board.getSpace(i % board.width, i));
        }

        board.setCurrentPlayer(board.getPlayer(0));
        roboRally.createBoardView(gameController);
        gameController.initializeProgrammingPhase();

        RepositoryAccess.getRepository().createGameInDB(board);
        // attachSaveNeedObserver();
    }


    public void saveGame() {
        //TODO need to be implemented
        if (gameController != null) {
            Board board = gameController.board;
            if (board.getGameId() != null) {
                RepositoryAccess.getRepository().updateGameInDB(board);

            }
        }
    }

    public void loadGame() {
        if (gameController == null) {
            List<GameInDB> games = RepositoryAccess.getRepository().getGames();
            if (!games.isEmpty()) {
                ChoiceDialog<GameInDB> dialog = new ChoiceDialog<>(games.get(games.size() - 1), games);
                dialog.setTitle("Select Game");
                dialog.setHeaderText("Select a game number");
                Optional<GameInDB> result = dialog.showAndWait();

                if (result.isPresent()) {
                    Board board = RepositoryAccess.getRepository().loadGameFromDB(result.get().id);
                    if (board != null) {
                        gameController = new GameController(board);
                        //  attachSaveNeedObserver();
                        roboRally.createBoardView(gameController);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Problem loading Game");
                        alert.setHeaderText("There was a problem loading the game!");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Game");
                alert.setHeaderText("There is no games in the database yet!");
                alert.showAndWait();
            }
        }
    }

    public void exit() {
        // Checking the answer, if yes, it terminates the game.
        Boolean svar = ConfirmBox.display("Confirm Exit", "Are you sure you want to exit RoboRally?");
        if (svar)
            Platform.exit();
    }
}
