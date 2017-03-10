package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by annie on 3/9/17.
 */
public class HardBattleshipModel extends BattleshipModel {

    // warning: if extra variables introduced here, json/gson conversion may require extra magic in Main.java.

    public HardBattleshipModel() {
        super();    // first initialize stuff using parent default constructor...
        // TODO: formally initialize computer ships (randomized starts and ends)
        // The following is for testing inheritance:
        computer_aircraftCarrier = new Military(false,"Computer_AircraftCarrier",5, new Coordinate(3,3),new Coordinate(3,7));
    }

    @Override
    public void shootAtPlayer() {
        // TODO: smart shooting. Pew pew
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;

        Coordinate coor = new Coordinate(randRow,randCol);
        playerShot(coor);
    }
}
