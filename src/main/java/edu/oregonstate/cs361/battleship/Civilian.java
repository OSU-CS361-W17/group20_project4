package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;


public class Civilian extends Ship {

    public Civilian(String n, int l, Coordinate s, Coordinate e) {
        super(n, l, s, e);
    }

    ArrayList <Coordinate> one_hit() {
        return this.getShipSquares();
    }
}
