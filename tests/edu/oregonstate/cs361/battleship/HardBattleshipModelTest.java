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

        model.randToggle = false;
        model.numHit = 1;
        model.length = 2;
        model.nextHit = new Coordinate(5,1);
        model.firstHit = new Coordinate(6,1);
        model.horizontal = true;
        assertEquals(false, model.nextHit.equals(model.firstHit));
        //model.playerShot(new Coordinate(5,1));
        model.shootAtPlayer();
        assertEquals(5, model.playerHits.get(6).getAcross());
        assertEquals(1, model.playerHits.get(6).getDown());
        assertEquals(0, model.numHit);
        assertEquals(true, model.randToggle);

    }

    @Test
    void setShipInfo(){
        HardBattleshipModel model = new HardBattleshipModel();
        model.placeShip("Aircraftcarrier","1","5","horizontal");

        model.setShipInfo(model.getShip("Aircraftcarrier"), new Coordinate(1,5));
        assertEquals(5, model.length);
        assertEquals(1, model.nextHit.getAcross());
        assertEquals(5, model.nextHit.getDown());
        assertEquals(true, model.horizontal);
        assertEquals(1, model.firstHit.getAcross());
        assertEquals(5, model.firstHit.getDown());
        assertEquals(false, model.randToggle);
        assertEquals(1, model.numHit);
    }
}
