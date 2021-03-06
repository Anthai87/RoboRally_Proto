package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Account;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.ConfirmBox;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class AppController {
    //Deklarerer array List som metode til valg af antal spillere og dertilhørende farve.
    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "brown", "cyan");
    final private String fieldChoice[] = {"Default", "Difficult", "Pro"};

    private RoboRally roboRally;
    private GameController gameController;
    private boolean saveNeeded;

    //Konstruktør som kaldes når man skal bruge instans af klassen.
    public AppController(RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    //Valg af antal spillere, hver spiller får tildelt en farve hvorefter man skal vælge mellem de 3 spilleplader.
    public void newGame() {
        /**
         *
         * @author: S195388, S163053, S141479
         *
         */
        //ArrayList som tager objektet integer.
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        String chosenBoard = null;
        //Tjekker for det valgt Board.
        if (result.isPresent()) {
            ChoiceDialog d = new ChoiceDialog(fieldChoice[0], fieldChoice);
            d.setHeaderText("Field Choice");
            d.setContentText("Please select the Field you wanna game on");
            //showAndWait gør, at man ikke kan komme videre i spillet, før ovenstående er udfyldt.
            Optional<String> optional = d.showAndWait();


            if (optional.get().equals("Default")) {
                chosenBoard = "defaultboard";

            } else if (optional.get().equals("Difficult")) {
                chosenBoard = "difficult";

            } else if (optional.get().equals("Pro")) {
                chosenBoard = "pro";
            }
        }

        //Her installeres det valgt board, og hvis ikke der er et valgt board, returnerer den ingenting.
        Board board = LoadBoard.loadBoard(chosenBoard);
        gameController = new GameController(board, this);

        if (!result.isPresent()) {
            return;
        }
        //Oprettes spillerprofilerne
        int no = result.get();
        for (int i = 0; i < no; i++) {
            Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1), i, new Account());
            board.addPlayer(player);
            //Sæt spillerne på startfelter
            for (int j = 0; j < board.getSpaces()[0].length; j++) {
                for (int k = 0; k < board.getSpaces()[1].length; k++) {
                    if (board.getSpace(j, k).getStartFelt() == player.no + 1) {
                        player.setSpace(board.getSpace(j,k));
                        player.setStartingpoint(j,k);
                    }
                }
            }
            if (player.getSpace() == null) {
                System.out.println("Player = null");
                player.setSpace(board.getSpace(i % board.width, i));
                player.setStartingpoint(i % board.width, i);
            }
        }
        //Spillerne blive sat på boardet, kalder derefter på GUI.
        board.setCurrentPlayer(board.getPlayer(0));
        roboRally.createBoardView(gameController);
        gameController.initializeProgrammingPhase();
        RepositoryAccess.getRepository().createGameInDB(board);
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
                        gameController = new GameController(board, this);
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
        Boolean svar = ConfirmBox.displayBox("Confirm Exit", "Are you sure you want to exit RoboRally?");
        if (svar)
            Platform.exit();
    }

}
