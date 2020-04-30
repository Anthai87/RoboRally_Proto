package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;

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
