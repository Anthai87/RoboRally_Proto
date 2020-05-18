package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 *
 * @author S163053 & S195388
 *
 */

//Klassen nedarver fra FieldAction

public class Checkpoint extends FieldAction {
    private int checkpoint;

    public int getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Board board = space.board;
        //Tjekker for om spilleren er på det første checkpoint felt osv.
        if (board != null && gameController.board == board) {
            if (checkpoint == 1) {
                space.getPlayer().getAccount().setFirstCheckPoint(checkpoint);
            } else if (checkpoint == 2) {
                space.getPlayer().getAccount().setSecondCheckPoint(checkpoint);
            }
            else if (checkpoint == 3){
                space.getPlayer().getAccount().setThirdCheckPoint(checkpoint);
            }
        }

        return false;
    }
}
