package edu.oregonstate.cs361.battleship;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by annie on 3/9/17.
 */
public class HardBattleshipModel extends BattleshipModel {

    // warning: if extra variables introduced here, json/gson conversion may require extra magic in Main.java.

    boolean randToggle = true;
    int numHit = 0;
    int length = 0;
    Coordinate nextHit = new Coordinate(0, 0);
    Coordinate firstHit = new Coordinate(0, 0);
    boolean horizontal = false;

    public HardBattleshipModel() {

        // System.out.println("This is the child HardBattleshipModel default constructor.\n");

        boolean placed = false;
        Random random = new Random();
        int rand1, rand2, orient;
        ArrayList<Coordinate> occupied = new ArrayList<>();     // track coors occupied by already-placed ships

        ArrayList<Ship> comp_ships = new ArrayList<>(5);
        comp_ships.add(computer_aircraftCarrier);
        comp_ships.add(computer_battleship);
        comp_ships.add(computer_clipper);
        comp_ships.add(computer_submarine);
        comp_ships.add(computer_dinghy);

        /* System.out.println("Ship locations prior to printing: ");
        for (Ship ship: comp_ships) {
            System.out.println(ship.getName() + ": starts " + ship.start.getAcross() + ", " + ship.start.getDown() + "; ends " + ship.end.getAcross() + ", " + ship.end.getDown());
        }
        System.out.println();

        System.out.println("Ship locations after resetting back to 0: ");
        for (Ship ship: comp_ships) {
            ship.setLocation(new Coordinate(0,0), new Coordinate(0,0));
            System.out.println(ship.getName() + ": starts " + ship.start.getAcross() + ", " + ship.start.getDown() + "; ends " + ship.end.getAcross() + ", " + ship.end.getDown());
        }
        System.out.println(); */

        // place each ship.
        for (Ship ship : comp_ships) {
            while (placed == false) {                           // while given ship not already successfully placed,
                // generate a random across or down for the ship's starting coor. For example: aircraft carrier has
                // length 5. Therefore it should be able to start at 1, 2, 3, 4, 5, or 6. nextInt's bound would be 0
                // (inclusive) through 10 - 5 + 1 = 6 (exclusive, so the actual max number would be 5). Add 1 again to
                // the whole thing, and rand1's possibilities become 1, 2, ..., 6.
                rand1 = random.nextInt(10 - ship.length + 1) + 1;
                // the other across/down just needs to be between 1 and 10, inclusive. We add 1 so that 0, 1, ..., 9
                // becomes 1, 2, ..., 10.
                rand2 = random.nextInt(10) + 1;

                orient = random.nextInt(2);             // randomize orientation: 0 for horiz, 1 for vert

                if (orient == 0) {                            // if horiz, row anywhere, col restricted by length
                    ship.setLocation(new Coordinate(rand2, rand1), new Coordinate(rand2, rand1 + ship.length - 1));
                } else if (orient == 1) {                       // if vert, row restricted by length, col anywhere
                    ship.setLocation(new Coordinate(rand1, rand2), new Coordinate(rand1 + ship.length - 1, rand2));
                }

                // check for overlap with other ships: for each coordinate occupied by our freshly placed ship,
                for (Coordinate coor : ship.getShipSquares()) {
                    if (occupied.contains(coor)) {            // if the coor is already occupied, cancel placement...
                        ship.setLocation(new Coordinate(0, 0), new Coordinate(0, 0));
                        placed = false;
                        break;
                    } else {
                        placed = true;
                    }
                }
                if (placed) {
                    occupied.addAll(ship.getShipSquares());
                    placed = false;
                    break;                                    // this ship is good. Break out to advance to next ship
                }
            }
        }

        /*System.out.println("Ship locations after random placement: ");
        for (Ship ship: comp_ships) {
            System.out.println(ship.getName() + ": starts " + ship.start.getAcross() + ", " + ship.start.getDown() + "; ends " + ship.end.getAcross() + ", " + ship.end.getDown());
        }
        System.out.println(); */

    }

    @Override
    public void shootAtPlayer() {

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
                setShipInfo(getShip("aircraftCarrier"), coor);
            }
            if (battleship.covers(coor)) {
                setShipInfo(getShip("battleship"), coor);
            }
            if (submarine.covers(coor)) {
                setShipInfo(getShip("battleship"), coor);
            }
            return;
        }

        //if the AI hit a ship continue hitting it
        if (randToggle == false)

            if (horizontal == false) {
                if (nextHit.equals(firstHit)) {
                    nextHit.setAcross(nextHit.getAcross() + 1);
                }
            }


        if (horizontal == true) {
            if (nextHit.equals(firstHit)) {
                nextHit.setDown(nextHit.getDown() + 1);
            }
        }

        Coordinate coor = new Coordinate(nextHit.getAcross(), nextHit.getDown());
        playerShot(coor);
        numHit++;


        //if the AI has hit all the places on the ship reset the ship information so it will randomly fire again
        if (numHit == length) {
            randToggle = true;
            numHit = 0;
            return;
        }

        if (horizontal == false) {
            nextHit.setAcross(nextHit.getAcross() + 1);
        }

        if (horizontal == true) {
            nextHit.setDown(nextHit.getDown() + 1);
        }

        return;
    }

    void setShipInfo(Ship currentShip, Coordinate coor) {
        length = currentShip.length;
        nextHit = currentShip.start;
        horizontal = currentShip.isHorizontal();
        firstHit = coor;
        randToggle = false;
        numHit = 1;
    }
}