package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;

public class RoboRallyMenuBar extends MenuBar {
    private AppController appController;


    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        Menu file = new Menu("File");
        // Ændr til this :-) og opret ikke ny
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(file);
        //this.getMenus().add(file);

        MenuItem newGame = new MenuItem("New Game");
        //newGame.seOntAction(e->{this.appController.newGame();});
        //file.getItems().add(newGame);

        MenuItem saveGame = new MenuItem("Save Game");

    }
}


