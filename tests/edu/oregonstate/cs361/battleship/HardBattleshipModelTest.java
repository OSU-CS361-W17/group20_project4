package edu.oregonstate.cs361.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Rebec on 3/19/2017.
 */
class HardBattleshipModelTest {
    @Test
    void shootAtPlayer() {
        HardBattleshipModel model = new HardBattleshipModel();
        model.placeShip("Aircraftcarrier","1","5","horizontal");
        model.placeShip("Battleship","2","4","horizontal");
        model.placeShip("Clipper","3","3","horizontal");
        model.placeShip("Dinghy","4","2","horizontal");
        model.placeShip("Submarine","5","1","horizontal");

        model.playerShot(new Coordinate(9,9));
        assertEquals(true, model.playerHits.isEmpty());

        model.playerShot(new Coordinate(1,5));
        assertEquals(1, model.playerHits.get(0).getAcross());
        assertEquals(5, model.playerHits.get(0).getDown());

        model.playerShot(new Coordinate(2,4));
        assertEquals(2, model.playerHits.get(1).getAcross());
        assertEquals(4, model.playerHits.get(1).getDown());

        model.playerShot(new Coordinate(3,3));
        assertEquals(3, model.playerHits.get(2).getAcross());
        assertEquals(3, model.playerHits.get(2).getDown());

        //Note: since the above shot added three coordinates to the hit array, we next check index 5, not index 3.
        model.playerShot(new Coordinate(4,2));
        assertEquals(4, model.playerHits.get(5).getAcross());
        assertEquals(2, model.playerHits.get(5).getDown());

        //The dinghy only adds one hit, so we continue incrementing as normal.
        model.playerShot(new Coordinate(5,1));
        assertEquals(5, model.playerHits.get(6).getAcross());
        assertEquals(1, model.playerHits.get(6).getDown());
    }
}
