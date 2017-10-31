package com.scarecrow.root.palette.ShapeClasses;

import android.util.Log;

import com.scarecrow.root.palette.utilty.CoordiPoint;
import com.scarecrow.root.palette.utilty.CoordinateData;
import com.scarecrow.root.palette.utilty.LinearVector;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-10-13.
 * basic shape class for describe
 */

public abstract class Shape implements Cloneable{
    final public static int RECT = 0,
                            LINE = 1,
                            CRICLE = 2,
                            TRANG = 3;
    protected int type;
    protected CoordinateData coordination;
    protected int shapeColor;

    protected int touchJudgeExpand =0;


    /**
     *
     * @return 自左上点开始逆时针一圈的坐标数组
     */
    public CoordiPoint[] getCornerPoints(){
        return new CoordiPoint[]{
                coordination.getLeft_up(),
                coordination.getLeft_down(),
                coordination.getRight_down(),
                coordination.getRight_up()
        };
    }

    protected boolean bookControlPoint = false ;

    public boolean isControlPoint(){
        return bookControlPoint;
    }

    public int getType(){
        return type;
    }

    public CoordinateData getCoordination() {
        return coordination;
    }

    public void setCoordination(CoordinateData coordination) {
        this.coordination = new CoordinateData(coordination);
    }

    public void setShapeColor(int color){
        shapeColor = color;
    }

    public int getShapeColor(){
        return shapeColor;
    }

    public CoordiPoint getCenterPoint(){
        return coordination.getCenter();
    }





    public void move(LinearVector delta_vector){
        CoordiPoint newLeftUp = coordination.getLeft_up(),
                    newRightDown = coordination.getRight_down();
        coordination = new CoordinateData(newLeftUp.moveByVector(delta_vector)
                                         ,newRightDown.moveByVector(delta_vector));

    }

    public boolean isPointInside(CoordiPoint coordiPoint ){
        CoordiPoint left_up = coordination.getLeft_up(),
                    right_down = coordination.getRight_down();
        if(coordiPoint.x > left_up.x-touchJudgeExpand && coordiPoint.x < right_down.x+touchJudgeExpand)
            if(coordiPoint.y > left_up.y-touchJudgeExpand && coordiPoint.y < right_down.y+touchJudgeExpand)
                return true;

        return false;
    }

    public void changeShapeByCornerControl(LinearVector vector,int relavent_pos){
        float tmp_x,tmp_y;
        switch (relavent_pos){
            case ControlPoint.LEFT_DOWN:
                coordination.getLeft_down().moveByVector(vector);
                tmp_y = vector.y;
                vector.y = 0;
                coordination.getLeft_up().moveByVector(vector);
                vector.y = tmp_y;
                vector.x = 0;
                coordination.getRight_down().moveByVector(vector);
                break;
            case ControlPoint.LEFT_UP:
                coordination.getLeft_up().moveByVector(vector);
                tmp_x = vector.x;
                vector.x = 0;
                coordination.getLeft_down().moveByVector(vector);
                vector.x = tmp_x;
                vector.y = 0;
                coordination.getRight_up().moveByVector(vector);
                break;
            case ControlPoint.RIGHT_DOWN:
                coordination.getRight_down().moveByVector(vector);
                tmp_x = vector.x;
                vector.x = 0;
                coordination.getRight_up().moveByVector(vector);
                vector.x = tmp_x;
                vector.y = 0;
                coordination.getLeft_down().moveByVector(vector);
                break;
            case ControlPoint.RIGHT_UP:
                coordination.getRight_up().moveByVector(vector);
                tmp_x = vector.x;
                vector.x = 0;
                coordination.getLeft_up().moveByVector(vector);
                vector.x = tmp_x;
                vector.y = 0;
                coordination.getRight_down().moveByVector(vector);
                break;
            default:
                break;
        }
    }


    @Override
    public Shape clone(){
        try {
            Shape shape = (Shape) super.clone();
            shape.setCoordination(coordination);
            return shape;
        }catch (Exception ee){
            Log.e(TAG, " in ShapesDeepCopy: " + ee);
            return null;
        }

    }


}
