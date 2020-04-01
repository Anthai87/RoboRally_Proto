package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;


public class RoboRallyMenuBar extends MenuBar{
    private AppController appController;

    MenuItem newGame;
    MenuItem saveGame;
    MenuItem exitGame;

    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;


        Menu file = new Menu("_File");
        file.getItems().add(newGame = new MenuItem("New Game..."));
        newGame.setOnAction(e->{this.appController.newGame();});
        file.getItems().add(saveGame = new MenuItem("Save Game..."));
        saveGame.setOnAction(event -> this.appController.saveGame());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitGame = new MenuItem("Exit Game..."));
        exitGame.setOnAction(event -> ConfirmBox.display("Exit","Are you sure?"));

        this.getMenus().addAll(file);


        // edit menu
        Menu editMenu = new Menu("_Edit");
        editMenu.getItems().add(new MenuItem("Undo..."));
        editMenu.getItems().add(new MenuItem("Redo..."));
        editMenu.getItems().add(new SeparatorMenuItem());
        editMenu.getItems().add(new MenuItem("Cut..."));
        editMenu.getItems().add(new MenuItem("Copy..."));
        editMenu.getItems().add(new SeparatorMenuItem());
        editMenu.getItems().add(new MenuItem("Delete..."));
        this.getMenus().addAll(editMenu);

        // Help Menu
        Menu helpMenu = new Menu("_Help");
        helpMenu.getItems().add(new MenuItem("Find Action..."));
        helpMenu.getItems().add(new MenuItem("Contact..."));
        helpMenu.getItems().add(new SeparatorMenuItem());
        helpMenu.getItems().add(new MenuItem("About..."));

        this.getMenus().addAll(helpMenu);


    }
}


