package com.scarecrow.root.palette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scarecrow.root.palette.ShapeClasses.Shape;
import com.scarecrow.root.palette.utilty.CoordiPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-10-13.
 * view part ,for display all shapes
 */

public class MyCanvas extends View {
    private Paint paint;
    private Painter painter;
    private List<Shape> shapeList;

    public MyCanvas(Context context, AttributeSet attr){
        super(context,attr);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        shapeList = new ArrayList<>();
        painter = new Painter(new Painter.RedrawCallback() {
            @Override
            public void onRedraw(List<Shape> shapes) {
                shapeList = shapes;
                postInvalidate();
            }
        });

    }
    public  Painter getPainter(){
        return painter;
    }
    public void setInAddShapeState(int type,int color){
        painter.setaddShapeInfo(type,color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        int action = motionEvent.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
              //  Log.d(TAG, "onTouchEvent: ACTION DOWN :"+ "x: "+motionEvent.getX() +" ,y: "+motionEvent.getY());
                painter.OnTouchDown(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                painter.onTouchMove(motionEvent);
             //   Log.d(TAG, "onTouchEvent: ACTION MOVE :"+ "x: "+motionEvent.getX() +" ,y: "+motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                painter.onTouchUp(motionEvent);
              //  Log.d(TAG, "onTouchEvent: ACTION UP :"+ "x: "+motionEvent.getX() +" ,y: "+motionEvent.getY());
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas){
        for (Shape shape:shapeList){
            drawShape(shape,canvas);
        }
    }

    private void drawShape(Shape shape,Canvas canvas){
        CoordiPoint[] cornerPoints = shape.getCornerPoints();
        CoordiPoint centerPoint = shape.getCenterPoint();
        switch (shape.getType()){
            case Shape.RECT:
                int expan = 0;
                paint.setColor(shape.getShapeColor());
         //       Log.d(TAG, "drawShape: RECT " + cornerPoints[0] + cornerPoints[2]);
                if(shape.isControlPoint())
                    expan = 20;
                canvas.drawRect(cornerPoints[0].x-expan,
                                cornerPoints[0].y-expan,
                                cornerPoints[2].x+expan,
                                cornerPoints[2].y+expan,paint);

                break;
            case Shape.CRICLE:
                break;
            case Shape.LINE:
                break;
            case Shape.TRANG:
                break;
            default:
                break;

        }
    }
}
