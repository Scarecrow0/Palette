package com.scarecrow.root.palette;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import com.scarecrow.root.palette.ShapeClasses.ControlPoint;
import com.scarecrow.root.palette.ShapeClasses.Rectangle;
import com.scarecrow.root.palette.ShapeClasses.Shape;
import com.scarecrow.root.palette.utilty.CoordiPoint;
import com.scarecrow.root.palette.utilty.LinearVector;
import com.scarecrow.root.palette.utilty.ShapesListTrace;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-10-13.
 * Control part,
 */

public class Painter {
    private static final int WAITING_SELECTED = 0,
            ON_SELECTED = 1,
            ON_DRAGING = 2,
            WAITING_ADD_NEW_SHAPE = 3;


    private int currState,
    // 工作如同DFA  保留一个状态根据外界的信号进行工作
    addShapeType,
    //  在添加shape状态的 记录准备添加shape的种类
    shapeIndexBeforeSelect,
    //  因为需要对被选中的shape置顶 ，而解除选中后需要返回原来的状态
    //  所以需要对原来位置进行记录
    newShapeColor;


    private Shape beenSelectedShape,
            selectedControlPoint;
    //  在 ON SELECT 状态中记录被选中的shape

    private List<Shape> shapes;
    //  画板中所有shape的记录

    private RedrawCallback mredraw;
    //发生变化时 对canvas 对象重绘请求的callback

    private CoordiPoint lastPos;
    //用于记录拖拽时上一点的位置 从而得到位移向量

    private ShapesListTrace shapeListTrace;

    private ShapesFileManager shapesFileManager;

    public Painter(RedrawCallback redraw) {
        super();
        currState = 0;
        mredraw = redraw;
        addShapeType = 0;
        shapeListTrace = new ShapesListTrace();
        shapes = new LinkedList<>();
        shapesFileManager = new ShapesFileManager();
    }

    public void setaddShapeInfo(int type, int color) {
        setAddShapeState();
        addShapeType = type;
        newShapeColor = color;
    }


    public void OnTouchDown(MotionEvent event) {
        Shape currtouchShape;
        CoordiPoint touchPoint = new CoordiPoint(event.getX(), event.getY());
        switch (currState) {
            case WAITING_ADD_NEW_SHAPE:
                addShape(event);
                currState = WAITING_SELECTED;
                onUpdateShapesList();
                break;

            case WAITING_SELECTED:
                currtouchShape = checkInAnyShape(event);
                if (currtouchShape != null) {
                    beenSelectedShape = currtouchShape;
                    activeSelectedShape(beenSelectedShape);
                    currState = ON_SELECTED;
                    lastPos = touchPoint;
                }
                break;

            case ON_SELECTED:
                Log.d(TAG, "OnTouchDown: shapes size : " + shapes.size());
                currtouchShape = checkInAnyShape(event);
                if (currtouchShape == null) {
                    dativeSelectedShape();
                    goToWaitSelected();
                    //未选中任何shape
                } else {
                    if (currtouchShape == beenSelectedShape) {
                        selectedControlPoint = null;
                        lastPos = touchPoint;
                        //same shape
                    } else {
                        if (currtouchShape.isControlPoint()) {
                            lastPos = touchPoint;
                            selectedControlPoint = currtouchShape;
                            Log.d(TAG, "OnTouchDown:  on control point");
                            //ready reshape
                        } else {
                            //another shape
                            lastPos = touchPoint;
                            dativeSelectedShape();
                            beenSelectedShape = currtouchShape;
                            activeSelectedShape(currtouchShape);
                        }
                    }

                }
                break;
            case ON_DRAGING:
                break;

            default:
                break;
        }
        Log.d(TAG, "OnTouch: curr state:" + currState);
        mredraw.onRedraw(shapes);
    }

    public void onTouchMove(MotionEvent event) {
        LinearVector vector;
        switch (currState) {
            case ON_SELECTED:
                vector = new LinearVector(event.getX() - lastPos.x, event.getY() - lastPos.y);
                currState = ON_DRAGING;
            case ON_DRAGING:
                vector = new LinearVector(event.getX() - lastPos.x, event.getY() - lastPos.y);
                if (selectedControlPoint == null)
                    moveShape(vector);
                else {
                    changeShape(vector, (ControlPoint) selectedControlPoint);
                    Log.d(TAG, "onTouchMove: on control point ");
                }

                lastPos = new CoordiPoint(event.getX(), event.getY());
                break;
            default:
                break;

        }

    }

    public void onTouchUp(MotionEvent event) {
        switch (currState) {
            case ON_DRAGING:
                LinearVector vector = new LinearVector(event.getX() - lastPos.x, event.getY() - lastPos.y);
                if (selectedControlPoint == null)
                    moveShape(vector);
                else {
                    changeShape(vector, (ControlPoint) selectedControlPoint);
                    selectedControlPoint = null;
                }
                currState = ON_SELECTED;
                onUpdateShapesList();
                break;
            default:
                break;

        }
    }

    private void addShape(MotionEvent event) {
        switch (addShapeType) {
            case Shape.RECT:
                Shape newShape = new Rectangle(new CoordiPoint(event.getX(), event.getY()));
                newShape.setShapeColor(newShapeColor);
                shapes.add(newShape);
            default:
                break;
        }
    }

    private void moveShape(LinearVector vector) {
        for (Shape shape : shapes.subList(shapes.size() - 5, shapes.size())) {
            shape.move(vector);
        }
        mredraw.onRedraw(shapes);
    }

    private void changeShape(LinearVector vector, ControlPoint controlPoint) {
        Shape parent = controlPoint.getRelvatedShape();
        parent.changeShapeByCornerControl(vector, controlPoint.getRelvatedPos());
        shapes = shapes.subList(0, shapes.size() - 4);
        Log.d(TAG, "changeShape: ");
        addShapeControlPoint(parent);
        mredraw.onRedraw(shapes);

    }

    private void goToWaitSelected() {
        currState = WAITING_SELECTED;
        beenSelectedShape = null;
        selectedControlPoint = null;
    }

    private void dativeSelectedShape() {
        if (beenSelectedShape != null) {
            shapes = shapes.subList(0, shapes.size() - 5);
            shapes.add(shapeIndexBeforeSelect, beenSelectedShape);
        }
    }

    private void activeSelectedShape(Shape shape) {
        int index = getShapeIndex(shape);
        shapeIndexBeforeSelect = index;
        shapes.remove(index);
        shapes.add(shape);
        addShapeControlPoint(shape);
    }


    public void deleteSelectedShape() {
        if (beenSelectedShape != null) {
            onUpdateShapesList();
            shapes = shapes.subList(0, shapes.size() - 5);
            goToWaitSelected();
            mredraw.onRedraw(shapes);

        }
    }

    public void onUndo(){
        exitSelectState();
        shapes = shapeListTrace.undo();
        mredraw.onRedraw(shapes);
    }

    public void onRedo(){
        exitSelectState();
        shapes = shapeListTrace.redo();
        mredraw.onRedraw(shapes);
    }


    public void save() {
        List<Shape> tmp = new LinkedList<>(shapes);
        dativeSelectedShape();
        shapesFileManager.saveToFile(shapes);
        shapes = tmp;

    }

    public void load() {
        dativeSelectedShape();
        goToWaitSelected();
        shapes = shapesFileManager.loadFromFile();
        mredraw.onRedraw(shapes);
    }

    /**
     * get single shape's index in shapes queue
     *
     * @param shape :a shape in shapes queue
     * @return index :in shapes queue
     */
    private int getShapeIndex(Shape shape) {
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i) == shape) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    private Shape checkInAnyShape(MotionEvent event) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            CoordiPoint coordpoint = new CoordiPoint(event.getX(), event.getY());
            if (shapes.get(i).isPointInside(coordpoint)) {
                return shapes.get(i);
            }
        }
        return null;
    }


    private void addShapeControlPoint(Shape selected_shape) {
        CoordiPoint[] corner = selected_shape.getCornerPoints();
        int i = 0;
        for (CoordiPoint point : corner) {
            ControlPoint control = new ControlPoint(point, selected_shape, ControlPoint.RELVENT_POS_ENUM[i]);
            i++;
            shapes.add(control);
        }
    }

    private void setAddShapeState() {
        currState = WAITING_ADD_NEW_SHAPE;
    }

    public void exitSelectState() {
        if (currState == ON_SELECTED) {
            dativeSelectedShape();
            goToWaitSelected();
        }
    }

    public void onUpdateShapesList(){
        Log.d(TAG, "onUpdateShapesList:");
        List<Shape> tmp = new LinkedList<>(shapes);
        dativeSelectedShape();
        shapeListTrace.updateTrace(ShapesDeepCopy(shapes));
        shapes = tmp;
    }

    private List<Shape> ShapesDeepCopy(List<Shape> shapes){
        List<Shape> newList= new LinkedList<>();
        for (Shape shape:shapes){
            newList.add(shape.clone());
        }
        return  newList;
    }

    public interface RedrawCallback {
        void onRedraw(List<Shape> shapes);
    }

}
