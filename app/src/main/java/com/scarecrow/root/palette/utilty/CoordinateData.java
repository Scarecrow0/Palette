package com.scarecrow.root.palette.utilty;

/**
 * Created by root on 17-10-13.
 * every object can be assume as a rectangle box
 * which coordinate can be given by two points
 * left up and right down .
 * But we also need more points info to maintain this obj.
 */

public class CoordinateData{
    private CoordiPoint left_up,right_down,center;

    /**
     * Constructer for rectangle or orv
     * @param left_up: basic describe points of this object
     * @param right_down: right down
     */
    public CoordinateData (CoordiPoint left_up,CoordiPoint right_down){
        this.left_up = left_up;
        this.right_down = right_down;
        this.center = new CoordiPoint((left_up.x + right_down.x)/2,
                                      (left_up.y + right_down.y)/2);
    }

    public CoordinateData(CoordinateData coordinateData){
        left_up = new CoordiPoint(coordinateData.getLeft_up());
        right_down = new CoordiPoint(coordinateData.getRight_down());
        center = new CoordiPoint(coordinateData.getCenter());
    }

    public CoordiPoint getLeft_up(){
        return left_up;
    }

    public  CoordiPoint getRight_down(){
        return right_down;
    }

    public CoordiPoint getCenter(){
        return center;
    }

    public CoordiPoint getRight_up(){
        return new CoordiPoint(right_down.x,left_up.y);
    }
    public CoordiPoint getLeft_down(){
        return new CoordiPoint(left_up.x,right_down.y);
    }
}
