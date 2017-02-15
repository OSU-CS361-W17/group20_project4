package edu.oregonstate.cs361.battleship;

/**
 * Created by michaelhilton on 1/5/17.
 */
public class Ship {
    private String name;
    private int length;
    private Coordinate start;
    private Coordinate end;

    public Ship(String n, int l,Coordinate s, Coordinate e) {
        name = n;
        length = l;
        start = s;
        end = e;
    }


    public void setLocation(Coordinate s, Coordinate e) {
        start = s;
        end = e;

    }

    public boolean covers(Coordinate test) {
        //horizontal
        if(start.getAcross() == end.getAcross()){
            if(test.getAcross() == start.getAcross()){
                if((test.getDown() >= start.getDown()) &&
                (test.getDown() <= end.getDown()))
                return true;
            } else {
                return false;
            }
        }
        //vertical
        else{
            if(test.getDown() == start.getDown()){
                if((test.getAcross() >= start.getAcross()) &&
                        (test.getAcross() <= end.getAcross()))
                    return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean alreadyPlaced() {
        if (this.start.getAcross() == 0) {
            return false;
        }
        return true;
    }


    public boolean scan(Coordinate coor) {
        if(covers(coor)){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross()-1,coor.getDown()))){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross()+1,coor.getDown()))){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross(),coor.getDown()-1))){
            return true;
        }
        if(covers(new Coordinate(coor.getAcross(),coor.getDown()+1))){
            return true;
        }
        return false;
    }

    public boolean overlaps(Ship ship) {
        Ship shortShip;
        Ship longShip;
        boolean overlap = false;
        Coordinate test = new Coordinate(0, 0);
        Coordinate test2 = new Coordinate(0, 0);

        if (this.length <= ship.length) {
            shortShip = this;
            longShip = ship;
        }
        else {
            shortShip = ship;
            longShip = this;
        }

        // for each square in shorter ship, does longer ship share the same square?
        for (int i = 0; i <= shortShip.length; i++) {
            // if longShip horizontal, want to loop through length horizontally
            if(longShip.start.getAcross() == longShip.end.getAcross()) {
                test.setAcross(shortShip.start.getAcross() + i);
                test2.setAcross(shortShip.start.getAcross() - i);
                test.setDown(shortShip.start.getDown());
                test2.setDown(shortShip.start.getDown());
            }
            // if longShip vertical, want to loop through length vertically
            else if(longShip.start.getDown() == longShip.end.getDown()) {
                test.setDown(shortShip.start.getDown() + i);
                test2.setDown(shortShip.start.getDown() - i);
                test.setAcross(shortShip.start.getAcross());
                test2.setAcross(shortShip.start.getAcross());
            }
            overlap = longShip.covers(test) || longShip.covers(test2);
            if (overlap) {
                break;
            }
        }
        return overlap;
    }
}
