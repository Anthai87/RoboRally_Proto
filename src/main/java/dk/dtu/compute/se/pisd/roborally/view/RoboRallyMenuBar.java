package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;

import java.awt.*;

public class RoboRallyMenuBar extends MenuBar {
    private AppController appController;

    public RoboRallyMenuBar(AppController appController) throws HeadlessException {
        this.appController = appController;

        Menu controlMenu = new Menu("New File");
        this.getMenus(1).add(controlMenu);

        MenuItem newGame = new MenuItem("New Game");
        controlMenu.getItem(2).add(newGame);


    }
}


