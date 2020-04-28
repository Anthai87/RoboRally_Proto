package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Pits extends FieldAction {
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Board board = space.board;
        if (board != null && gameController.board == board) {
            Player player = space.getPlayer();
            if (space.getPlayer() != null) {
                player.setHeading(player.getHeading().next());
            }
        }
        return false;
    }
}
