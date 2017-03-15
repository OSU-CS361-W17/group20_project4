package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by annie on 3/9/17.
 */
public class HardBattleshipModel extends BattleshipModel {

    // warning: if extra variables introduced here, json/gson conversion may require extra magic in Main.java.

    boolean randToggle = true;
    int numHit = 0;
    int length;
    Coordinate nextHit = new Coordinate(0,0);
    Coordinate firstHit = new Coordinate(0,0);
    boolean horizontal = false;

    public HardBattleshipModel() {
        super();    // first initialize stuff using parent default constructor...
        // TODO: formally initialize computer ships (randomized starts and ends)
        // The following is for testing inheritance:
        computer_aircraftCarrier = new Military(false, "Computer_AircraftCarrier", 5, new Coordinate(3, 3), new Coordinate(3, 7));
    }

    @Override
    public void shootAtPlayer() {

        // TODO: smart shooting. Pew pew

        //if the AI has not hit a ship fire randomly until it has
        if (randToggle == true) {
            int max = 10;
            int min = 1;
            Random random = new Random();
            int randRow = random.nextInt(max - min + 1) + min;
            int randCol = random.nextInt(max - min + 1) + min;

            Coordinate coor = new Coordinate(randRow, randCol);
            playerShot(coor);

            //if one of the following ships was hit begin hitting the rest of the ship
            if (aircraftCarrier.covers(coor)) {
                setShipInfo(getShip("aircraftcarrier"), coor);
            }
            if (battleship.covers(coor)) {
                setShipInfo(getShip("battleship"), coor);
            }
            if (submarine.covers(coor)) {
                setShipInfo(getShip("submarine"), coor);
            }
            return;
        }

        //if the AI hit a ship continue hitting it
        if (randToggle == false) {
            /*
            if (horizontal == false && numHit == 1){
                nextHit.setAcross(nextHit.getAcross() - 1);
            }

            if (horizontal == true && numHit == 1){
                nextHit.setDown(nextHit.getDown() - 1);
            }
            */

            playerShot(nextHit);
            numHit++;


            //if the AI has hit all the places on the ship reset the ship information so it will randomly fire again
            if (numHit == length){
                resetShipInfo();
                return;
            }

            if (horizontal == false){
                nextHit.setAcross(nextHit.getAcross() + 1);
                if (nextHit.equals(firstHit)){
                    nextHit.setAcross(nextHit.getAcross() + 1);
                }
            }

            if (horizontal == true){
                nextHit.setDown(nextHit.getDown() + 1);
                if (nextHit.equals(firstHit)){
                    nextHit.setDown(nextHit.getDown() + 1);
                }
            }

            return;
        }
    }

    void setShipInfo(Ship currentShip, Coordinate coor) {
        length = currentShip.length;
        nextHit = currentShip.start;
        System.out.println(nextHit.getAcross());
        System.out.println(nextHit.getDown());
        firstHit = coor;
        horizontal = currentShip.isHorizontal();
        randToggle = false;
        numHit = 1;
    }

    void resetShipInfo(){
        length = 0;
        randToggle = true;
        numHit = 0;
    }
}