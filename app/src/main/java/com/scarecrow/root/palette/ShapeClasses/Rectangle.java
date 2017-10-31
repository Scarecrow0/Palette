package com.scarecrow.root.palette.ShapeClasses;

import com.scarecrow.root.palette.utilty.CoordiPoint;
import com.scarecrow.root.palette.utilty.CoordinateData;
import com.scarecrow.root.palette.utilty.LinearVector;

/**
 * Created by root on 17-10-26.
 */

public class Rectangle extends Shape implements Cloneable{

    public Rectangle(CoordiPoint leftup){
        super();
        type = Shape.RECT;

        CoordiPoint rightDown = new CoordiPoint(leftup.x,leftup.y);
        rightDown.moveByVector(new LinearVector(600,400));
        coordination = new CoordinateData(leftup,rightDown);
    }
    public Rectangle(CoordiPoint center,int size){
        super();
        type = Shape.RECT;
        CoordiPoint leftup = new CoordiPoint(center.x - size/2,center.y - size/2),
                    rightdown = new CoordiPoint(center.x + size/2,center.y + size/2);
        coordination = new CoordinateData(leftup,rightdown);
    }

}
