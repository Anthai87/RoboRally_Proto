package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 *
 * @author S163053 & S195388
 *
 */
//Nedarver fra fieldaction

public class Gear extends FieldAction {

    @Override
    //Sætter players heading til at være prev = en gang til venstre
    public boolean doAction(GameController gameController, Space space) {
        Board board = space.board;
        if (board != null && gameController.board == board) {
            Player player = space.getPlayer();
            if (space.getPlayer() != null) {
                player.setHeading(player.getHeading().prev());
            }
        }
        return false;
    }
}
