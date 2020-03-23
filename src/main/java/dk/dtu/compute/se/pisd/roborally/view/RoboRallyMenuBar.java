package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;

public class RoboRallyMenuBar extends MenuBar {
    private AppController appController;


    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        Menu file = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem saveGame = new MenuItem("Save Game");
        file.getItems().addAll(newGame, saveGame);

        //newGame.seOntAction(e->{this.appController.newGame();});
        //file.getItems().add(newGame);

        this.getMenus().add(file);

    }
}


