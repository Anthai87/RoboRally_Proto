/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.ConfirmBox;
import dk.dtu.compute.se.pisd.roborally.view.WinnerOfTheGame;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;
    private AppController appController;

    public GameController(Board board, AppController appController) {
        this.board = board;
        this.appController = appController;
    }

    public void finishProgrammingPhase() {
        /**
         *
         * Logic implemented by Anton, s163053
         *
         */
        if (!board.getCurrentPlayer().isCommandCardsFull()) {

            //GUI som kommer frem, hvis ikke man vælger 5 kort
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please pick 5 cards");
            alert.showAndWait();
            return;
        }
        board.setCurrentPlayer(board.getPlayer(nextPlayer()));

        for (Player player :
                board.getPlayers()) {
            if (!player.isCommandCardsFull()) {
                return;
            }
        }

        //Kører kortene en efter en for hver spiller
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setStep(0);
    }

    //Sætter næste spiller udfra hvor mange spillere der er i spillet
    public int nextPlayer() {
        if (board.getCurrentPlayer().no + 1 < board.getPlayersNumber()) {
            return board.getCurrentPlayer().no + 1;
        } else {
            return 0;
        }
    }

    //Tjekker for at alle spillere ikke har lagt sine 5 kort.
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    //Eksekvere sine kort, hvis man har programmeret 5 kort
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    //Ændre fra Activation phase til stepmode
    public void executePrograms() {
        board.setStepMode(false);
        while (board.getPhase() == Phase.ACTIVATION) {


            executeStep(null);
        }
        //Check for win
        //TODO: implementér kode til at afslutte spillet
        /**
         * @made_by: Anthony og Anton
         */

        //Tjekker for om spilleren har 3. checkpoint i sin account, heraf vundet.
        if (board.getCurrentPlayer().getAccount().isThirdCheckPoint()) {
            board.setGameWon(true);
            WinnerOfTheGame.display("Winner", "The winner is " + board.getCurrentPlayer().getName(), appController);
        }
    }


    //Eksekverer kortene
    private void execute(Command command) {
        executeStep(command);
        if (board.getPhase() == Phase.ACTIVATION && !board.isStepMode()) {
            executePrograms();
        }
    }

    //Genererer Random CommandCard
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    //Gør at man ikke har nogle programmerede kort i starten at programmerings fasen
    public void initializeProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {

                //Sletter de brugte kort
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    player.getProgramField(j).setCard(null);
                }

                //Generer nye kort
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    player.getCardField(j).setCard(generateRandomCommandCard());
                }
            }
        }
    }

    public void executeStep() {
        board.setStepMode(true);
        executeStep(null);

        //Man rykker hen til sidste checkpoint før man får afvide man har vundet.
        if (board.getCurrentPlayer().getAccount().isThirdCheckPoint()) {
            board.setGameWon(true);
            WinnerOfTheGame.display("Winner", "The winner is " + board.getCurrentPlayer().getName(), appController);
        }
    }

    //SPØRG ANTON
    private void executeStep(Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                if (option != null) {
                    executeCommand(currentPlayer, option);
                } else {
                    executeCommandCard(currentPlayer, currentPlayer.getProgramField(step).getCard());
                }

                if (!board.isGameWon()) {
                    if (board.getCurrentPlayer().getAccount().isThirdCheckPoint()) {
                        board.setGameWon(true);
                        WinnerOfTheGame.display("Winner", "The winner is " + board.getCurrentPlayer().getName(), appController);
                    }
                }


                if (board.getPhase() == Phase.ACTIVATION) {
                    if (currentPlayer.no + 1 < board.getPlayersNumber()) {
                        board.setCurrentPlayer(board.getPlayer(currentPlayer.no + 1));
                    } else {
                        step++;
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    }
                }
            }

            if (board.getPhase() == Phase.ACTIVATION && (step < 0 || step >= Player.NO_REGISTERS)) {
                initializeProgrammingPhase();
            }
        }

        if (board.getPhase() == Phase.INITIALISATION) {
            initializeProgrammingPhase();
        }
    }

    //Når du programmerer dine 5 kort, så rykkes de 5 kort du vælger fra source til target
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null & targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    //Vha. getNeighbhour metoden rykkes spilleren en fremad mod sin heading.
    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
                    // we don't do anything here  for now; we just catch the
                    // exception so that we do no pass it on to the caller
                    // (which would be very bad style).
                }
            }
        }
    }

    //SPØRG ANTON
    void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();

        Space target = board.getNeighbour(space, heading);
        if (other != null) {
            if (target != null) {
                // XXX Note that there might be additional problems with
                //     infinite recursion here (in some special cases)!
                //     We will come back to that!
                moveToSpace(other, target, heading);


                // Note that we do NOT embed the above statement in a try catch block, since
                // the thrown exception is supposed to be passed on to the caller

                assert target.getPlayer() == null : target; // make sure target is free now
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);

        //Checking for actions of the field you just landed on
        for (FieldAction action : space.getActions()) {

            //do all in this line
            action.doAction(this, space);
        }
    }

    //Exception bliver kastet, når det ikke er muligt at rykke spilleren
    class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

    //Eksekverer kortene
    private void executeCommandCard(@NotNull Player player, CommandCard card) {
        if (card != null) {
            executeCommand(player, card.command);
        }
    }

    //Flytter spillerne efter deres commandcards
    public void executePlayersOption(Player player, Command option) {
        if (player != null && player.board == board && board.getCurrentPlayer() == player) {
            board.setPhase(Phase.ACTIVATION);
            execute(option);
        }
    }

    //Ved brug af next() drejes spilleren en gang til højre
    public void turnRight(Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }
    //Ved brug af prev() drejes spilleren en gang til venstre
    public void turnLeft(Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());

        }
    }

    //Bruger metoden moveForward() to gange, for at rykke spilleren to gange frem.
    public void fastForward(Player player) {
        moveForward(player);
        moveForward(player);
    }

    //Tager argumentet i command og eksekverer det på spillerne
    private void executeCommand(@NotNull Player player, Command command) {
        if (player.board == board && command != null) {
            // XXX This is an very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a much more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).
            if (command.isInteractive()) {
                board.setPhase(Phase.PLAYER_INTERACTION);
            } else {
                switch (command) {
                    case FORWARD:
                        this.moveForward(player);
                        break;
                    case RIGHT:
                        this.turnRight(player);
                        break;
                    case LEFT:
                        this.turnLeft(player);
                        break;
                    case FAST_FORWARD:
                        this.fastForward(player);
                        break;
                    default:
                        // DO NOTHING (for now)
                }
            }
        }
    }
}
