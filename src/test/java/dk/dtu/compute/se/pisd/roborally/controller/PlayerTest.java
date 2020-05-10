package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.Test;

import static org.junit.Assert.*;


    public class PlayerTest {

        @Test
        public void doAction() {


            Board TestBoard = new Board(20,20);
            Account TestAccount = new Account();


            Player TestPlayer = new Player(TestBoard,"Green","Sara",1,TestAccount);
            Space TestSpace = new Space(TestBoard,10,10);
            Space Playermove = new Space(TestBoard,9,10);


            TestPlayer.setSpace(TestSpace);
            TestSpace.setPlayer(TestPlayer);
            TestPlayer.setSpace(Playermove);
            Playermove.setPlayer(TestPlayer);
            TestSpace.setPlayer(null);

            assertNotNull(Playermove.getPlayer());
            assertNull(TestSpace.getPlayer());


        }

}
