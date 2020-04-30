package dk.dtu.compute.se.pisd.roborally.view;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;

/**
 * ...
 *
 * @author Anthony Haidari, s141479
 *
 */


public class RoboRallyMenuBar extends MenuBar{
    private AppController appController;

    MenuItem newGame;
    MenuItem saveGame;
    MenuItem loadGame;
    MenuItem exitGame;

    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        Menu file = new Menu("_File");
        file.getItems().add(newGame = new MenuItem("New Game..."));
        newGame.setOnAction(e->{this.appController.newGame();});
        file.getItems().add(saveGame = new MenuItem("Save Game..."));
        saveGame.setOnAction(event -> this.appController.saveGame());
        file.getItems().add(loadGame = new MenuItem("Load Game..."));
        loadGame.setOnAction(event -> this.appController.loadGame());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitGame = new MenuItem("Exit Game..."));
        exitGame.setOnAction(event -> ConfirmBox.display("Exit","Are you sure you want to exit?"));

        this.getMenus().addAll(file);

        // Help Menu
        Menu helpMenu = new Menu("_Help");
        helpMenu.getItems().add(new MenuItem("Contact..."));
        helpMenu.getItems().add(new MenuItem("About..."));

        this.getMenus().addAll(helpMenu);

    }
}


