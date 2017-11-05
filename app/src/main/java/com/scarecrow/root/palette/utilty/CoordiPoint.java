package com.scarecrow.root.palette.utilty;

import java.io.Serializable;

/**
 * Created by root on 17-10-13.
 *
 */

public class CoordiPoint implements Serializable {
    public float x,y;
    public CoordiPoint(float x,float y){
        this.x = x;
        this.y = y;
    }

    public CoordiPoint(CoordiPoint coordiPoint){
        this.x = coordiPoint.x;
        this.y = coordiPoint.y;
    }

    public CoordiPoint moveByVector(LinearVector vector){
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public String toString(){
        return  "x : " + x + " , y: " + y;
    }


}
