package com.scarecrow.root.palette.ShapeClasses;

import android.graphics.Color;

import com.scarecrow.root.palette.utilty.CoordiPoint;

/**
 * Created by root on 17-10-26.
 *
 * 用于拖拽图形的控制点类
 * 继承与rectangle
 */

public class ControlPoint extends Rectangle {
    public static final int LEFT_UP = 189,
                            LEFT_DOWN = 256,
                            RIGHT_UP = 74,
                            RIGHT_DOWN = 456;

    public static final int[] RELVENT_POS_ENUM= new int[]{
            ControlPoint.LEFT_UP,
            ControlPoint.LEFT_DOWN,
            ControlPoint.RIGHT_DOWN,
            ControlPoint.RIGHT_UP
    };
    private Shape relvatedShape;

    private int relvatedPos;

    public ControlPoint(CoordiPoint center,Shape relvatedShape,int relvatedPos){
        super(center,15);
        this.relvatedShape = relvatedShape;
        this.relvatedPos = relvatedPos;
        touchJudgeExpand = 60;
        bookControlPoint = true;
        setShapeColor(Color.YELLOW);
    }

    @Override
    public boolean isPointInside(CoordiPoint coordiPoint){
        return super.isPointInside(coordiPoint);
    }

    public Shape getRelvatedShape(){
        return relvatedShape;
    }

    public int getRelvatedPos(){
        return  relvatedPos;
    }
}
