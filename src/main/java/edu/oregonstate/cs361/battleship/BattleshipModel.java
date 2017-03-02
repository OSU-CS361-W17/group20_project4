package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
 public class BattleshipModel {

    private Military aircraftCarrier = new Military(false,"AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    private Military battleship = new Military(true,"Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    private Military submarine = new Military(true,"Submarine",2, new Coordinate(0,0),new Coordinate(0,0));

    private Military computer_aircraftCarrier = new Military(false,"Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,6));
    private Military computer_battleship = new Military(true,"Computer_Battleship",4, new Coordinate(2,8),new Coordinate(5,8));
    private Military computer_submarine = new Military(true,"Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,7));

    ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;

    ArrayList<Coordinate> shipSquares;      // to hold all the squares of all ships already placed by the user

    boolean scanResult = false;
    boolean overlapResult = false;
    boolean offBoard = false;


    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();

        shipSquares = new ArrayList<>();
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public BattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        int rowint = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);

        /* before attempting to place ship, reset overlap flag... Also, if prospective ship placing is actually a
        replacing, i.e. it's coming in with initialized head-butts that aren't zeroed out, remove all its squares from
        the squares-occupied-by-current-ships masterlist...
         */
       overlapResult = false;
        offBoard = false;
        /*System.out.println("start of placeShips. shipSquares contains: ");
        for (int i = 0; i < shipSquares.size(); i++) {
            System.out.println(shipSquares.get(i).getAcross() + ", " + shipSquares.get(i).getDown());
        }*/
        if (this.getShip(shipName).alreadyPlaced()) {
            //System.out.println("hey you've placed this before...");
            shipSquares.removeAll(this.getShip(shipName).getShipSquares());
        }

        if(orientation.equals("horizontal")){
            if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+4));
            } if(shipName.equalsIgnoreCase("battleship")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+3));
            } if(shipName.equalsIgnoreCase("submarine")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt+1));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+3,colInt));
                } if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 1, colInt));
                }
        }

        // for each square that the freshly placed ship now occupies...
        for (int i = 0; i < this.getShip(shipName).getShipSquares().size(); i++) {
            if (this.getShip(shipName).getShipSquares().get(i).getAcross() > 10 || this.getShip(shipName).getShipSquares().get(i).getDown() > 10) {
                offBoard = true;
                // "un-place" ship
                this.getShip(shipName).setLocation(new Coordinate(0,0), new Coordinate(0, 0));
                return this;
            }
            // if master list already contains one of the new ship's squares, it's an overlap!
            if (shipSquares.contains(this.getShip(shipName).getShipSquares().get(i))) {
                overlapResult = true;
                // "un-place" ship
                this.getShip(shipName).setLocation(new Coordinate(0,0), new Coordinate(0, 0));
                return this;
            }
        }
        // if no overlap + not off the board, leave the ship alone and add its squares to the master list
        shipSquares.addAll(this.getShip(shipName).getShipSquares());
        /*System.out.println("end of placeShips. shipSquares contains: ");
        for (int i = 0; i < shipSquares.size(); i++) {
            System.out.println(shipSquares.get(i).getAcross() + ", " + shipSquares.get(i).getDown());
        }*/
        return this;
    }

    public void shootAtComputer(int row, int col) {
        overlapResult = false;
        offBoard = false;
        Coordinate coor = new Coordinate(row,col);
        if(computer_aircraftCarrier.covers(coor)){
            computerHits.add(coor);
        }else if (computer_battleship.covers(coor)){
            computerHits.add(coor);
        }else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
        } else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;

        Coordinate coor = new Coordinate(randRow,randCol);
        playerShot(coor);
    }

    void playerShot(Coordinate coor) {
        if(playerMisses.contains(coor)){
            System.out.println("Dupe");
        }

        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
        }else if (battleship.covers(coor)){
            playerHits.add(coor);
        }else if (submarine.covers(coor)){
            playerHits.add(coor);
        } else {
            playerMisses.add(coor);
        }
    }


    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt,colInt);
        scanResult = false;
        if(computer_aircraftCarrier.scan(coor)){
            scanResult = true;
        }
        else if (computer_battleship.scan(coor)){
            scanResult = true;
        }else if (computer_submarine.scan(coor)){
            scanResult = true;
        } else {
            scanResult = false;
        }
    }

    public boolean getScanResult() {
        return scanResult;
    }
}