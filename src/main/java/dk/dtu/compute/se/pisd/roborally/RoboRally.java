/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.ConfirmBox;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.WEST;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RoboRally extends Application {
    private Board board;
    private BoardView boardView;
    private GameController gameController;
    private static final int MIN_APP_WIDTH = 600;
    private Stage stage;
    private BorderPane boardRoot;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) {
        // creates the model
        board = new Board(8, 8);
        // and game controller
        gameController = new GameController(board);

        Player player = new Player(board, "red", "Anton",0);
        board.addPlayer(player);
        board.setCurrentPlayer(player);

        player.setSpace(board.getSpace(0,0));
        player.setHeading(WEST);

        Player player2 = new Player(board, "green", "RezaJoon",1);
        player2.setSpace(board.getSpace(1,0));
        board.addPlayer(player2);

       // creates the view (for the model)
        boardView = new BoardView(gameController);
        gameController.initializeProgrammingPhase();

        AppController appController = new AppController(this);

        primaryStage.setTitle("Gruppe D");
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);

        GridPane topMenu = new GridPane();
        topMenu.getChildren().addAll(menuBar);


        primaryStage.setOnCloseRequest(
                e -> {
                    e.consume();
                    appController.exit();

                });

        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game

        BorderPane root = new BorderPane();
        root.setTop(topMenu);
        root.setCenter(boardView);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // add the view and show it
        primaryStage.setResizable(false);
        primaryStage.sizeToScene(); // this is to fix a likely bug with the nonresizable stage
        primaryStage.show();
    }

    public void createBoardView(GameController gameController) {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();

        if (gameController != null) {
            // create and add view for new board
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }

        stage.sizeToScene();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        // XXX just in case we need to do something here eventually;
        //     but right now the only way for the user to exit the app
        //     is delegated to the exit() method in the AppController,
        //     so that the AppController can take care of that.
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}