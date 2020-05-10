package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Account;
import org.junit.Test;

import static org.junit.Assert.*;


public class CheckpointTest {

    @Test
    public void doAction() {


       Account HG = new Account();

       HG.setFirstCheckPoint(1);

       assertEquals(true,HG.isFirstCheckPoint());


    }
}