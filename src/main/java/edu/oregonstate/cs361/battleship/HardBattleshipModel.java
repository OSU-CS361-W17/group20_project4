package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;

/**
 * Created by annie on 3/9/17.
 */
public class HardBattleshipModel extends BattleshipModel {

    // warning: if extra variables introduced here, json/gson conversion may require extra magic in Main.java.

    public HardBattleshipModel() {
        super();
    }

    @Override
    public HardBattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        return this;
    }

    @Override
    public void shootAtPlayer() {
        return;
    }
}
