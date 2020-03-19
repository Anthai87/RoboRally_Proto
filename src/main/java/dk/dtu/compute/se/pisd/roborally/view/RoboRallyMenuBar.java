package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.MenuBar;

import java.awt.*;

public class RoboRallyMenuBar extends MenuBar {
    private AppController appController;


    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        Menu file = new Menu("File");
        //this.getMenus().add(file);

        MenuItem newGame = new MenuItem("New Game");
        //newGame.seOntAction(e->{this.appController.newGame();});
        //file.getItems().add(newGame);

        MenuItem saveGame = new MenuItem("Save Game");

    }
}

