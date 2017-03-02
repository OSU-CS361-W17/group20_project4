package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;


public class Civilian extends Ship {
    ArrayList <Coordinate> one_hit() {
        return this.getShipSquares();
    }
}