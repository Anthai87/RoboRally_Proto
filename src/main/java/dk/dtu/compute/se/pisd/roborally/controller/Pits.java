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
            if (player != null) {
                Space target = board.getSpace(player.getStartX(), player.getStartY());
                if (target!=null){
                    try {
                        gameController.moveToSpace(player,target,player.getHeading());
                    } catch (GameController.ImpossibleMoveException e) {
                        e.printStackTrace();

                    }

                    return true;
                }
            }
        }
        return false;
    }
}