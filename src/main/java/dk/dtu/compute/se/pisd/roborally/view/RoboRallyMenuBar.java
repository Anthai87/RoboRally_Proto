package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.MenuBar;

import java.awt.*;

public class RoboRallyMenuBar extends MenuBar {
    private AppController appController;


    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        Menu file = new Menu("File");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(file);

        MenuItem newGame = new MenuItem("New Game");
        MenuItem saveGame = new MenuItem("Save Game");

    }
}


