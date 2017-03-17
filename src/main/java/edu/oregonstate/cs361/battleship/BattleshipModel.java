package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
 public class BattleshipModel {

    private Civilian clipper = new Civilian("Clipper", 3, new Coordinate(0, 0), new Coordinate(0, 0));
    private Civilian dinghy = new Civilian("Dinghy", 1, new Coordinate(0, 0), new Coordinate(0, 0));
	private Military aircraftCarrier = new Military(false,"AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    private Military battleship = new Military(true,"Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    private Military submarine = new Military(true,"Submarine",2, new Coordinate(0,0),new Coordinate(0,0));

    // computer ships are formally initialized (hardcoded) in the default constructor.
    protected Civilian computer_clipper = new Civilian("Computer_Clipper", 3, new Coordinate(0, 0), new Coordinate(0, 0));
    protected Civilian computer_dinghy = new Civilian("Computer_Dinghy", 1, new Coordinate(0, 0), new Coordinate(0, 0));
    protected Military computer_aircraftCarrier = new Military(false,"Computer_AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    protected Military computer_battleship = new Military(true,"Computer_Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    protected Military computer_submarine = new Military(true,"Computer_Submarine",2, new Coordinate(0,0),new Coordinate(0,0));

    ArrayList<Coordinate> playerHits = new ArrayList<>();
    private ArrayList<Coordinate> playerMisses = new ArrayList<>();
    ArrayList<Coordinate> computerHits = new ArrayList<>();
    private ArrayList<Coordinate> computerMisses = new ArrayList<>();

    ArrayList<Coordinate> shipSquares = new ArrayList<>();      // to hold all the squares of all ships already placed by the user

    boolean scanResult = false;
    boolean overlapResult = false;
    boolean offBoard = false;

    int snakeRow = 1;
    int snakeCol = 1;


    public BattleshipModel() {
        // System.out.println("This is the parent BattleshipModel default constructor.");
        computer_clipper.setLocation(new Coordinate(5, 1), new Coordinate(5, 3));
        computer_dinghy.setLocation(new Coordinate(10, 10), new Coordinate(10, 10));
        computer_aircraftCarrier.setLocation(new Coordinate(2, 2), new Coordinate(2, 6));
        computer_battleship.setLocation(new Coordinate(2, 8), new Coordinate(5, 8));
        computer_submarine.setLocation(new Coordinate(9, 6), new Coordinate(9, 7));
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return this.aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return this.battleship;
        } if(shipName.equalsIgnoreCase("submarine")) {
            return this.submarine;
        }if(shipName.equalsIgnoreCase("clipper")) {
            return this.clipper;
        }if(shipName.equalsIgnoreCase("dinghy")) {
            return this.dinghy;
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
            }if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt+2));
            }if(shipName.equalsIgnoreCase("dinghy")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+3,colInt));
                } if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint + 1, colInt));
                } if(shipName.equalsIgnoreCase("clipper")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint+2, colInt));
                } if(shipName.equalsIgnoreCase("dinghy")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt));
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
        //System.out.println("end of placeShips. shipSquares contains: ");
        /*for (int i = 0; i < shipSquares.size(); i++) {
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
        }else if (computer_clipper.covers(coor)){
            computerHits.addAll(computer_clipper.one_hit());
        }else if (computer_dinghy.covers(coor)){
            computerHits.addAll(computer_dinghy.one_hit());
        } else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
        Coordinate coor = new Coordinate(snakeRow,snakeCol);
        playerShot(coor);

        if (snakeCol == 10) {
            snakeCol = 1;
            snakeRow++;
        }
        else {
            snakeCol++;
        }
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
        }else if (clipper.covers(coor)){
            playerHits.addAll(clipper.one_hit());
        }else if (dinghy.covers(coor)){
            playerHits.addAll(dinghy.one_hit());
        } else {
            playerMisses.add(coor);
        }
    }


    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt,colInt);
        scanResult = false;
        if(computer_aircraftCarrier.scan(coor)){
            scanResult = true;
        } else if (computer_battleship.scan(coor)){
            scanResult = true;
        }else if (computer_submarine.scan(coor)){
            scanResult = true;
        }else if (computer_clipper.scan(coor)){
            scanResult = true;
        }else if (computer_dinghy.scan(coor)) {
            scanResult = true;
        } else {
            scanResult = false;
        }
    }

    public boolean getScanResult() {
        return scanResult;
    }
}
