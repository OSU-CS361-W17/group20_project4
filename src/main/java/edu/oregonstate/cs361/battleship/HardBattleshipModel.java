package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;

/**
 * Created by annie on 3/9/17.
 */
public class HardBattleshipModel extends BattleshipModel {

    // warning: if extra variables introduced here, json/gson conversion may require extra magic in Main.java.

    public HardBattleshipModel() {
        super();    // first initialize stuff using parent default constructor...
        // then formally initialize computer ships (randomized starts and ends)
        // Uncomment following line to test inheritance:
        // computer_aircraftCarrier = new Military(false,"Computer_AircraftCarrier",5, new Coordinate(3,3),new Coordinate(3,7));
    }

    @Override
    public void shootAtPlayer() {
        return;
    }
}
