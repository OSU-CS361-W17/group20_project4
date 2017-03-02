package edu.oregonstate.cs361.battleship;

/**
 * Created by kbajn on 3/1/2017.
 */

public class Military extends Ship {
    private boolean stealth;

    public Military(boolean b, String n, int l,Coordinate s, Coordinate e) {
        super(n,l,s,e);
        stealth = b;
    }



}
