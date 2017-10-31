package com.scarecrow.root.palette.utilty;

import com.scarecrow.root.palette.ShapeClasses.Shape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 17-10-29.
 * 记录撤销 重做操作时的图形队列
 */

public class ShapesListTrace { private List<Integer> traceIndexStore;
    private int currStatePointer ;
    private TraceStore traceStore;

    public ShapesListTrace(){
        traceStore = new TraceStore();
        currStatePointer = 0;
        traceIndexStore = new ArrayList<>();
        traceStore.add(new LinkedList<Shape>());
        traceIndexStore.add(0);
    }

    public void updateTrace(List<Shape> shapes){
        for(int i = currStatePointer+1;i < traceIndexStore.size();i++){
            traceStore.delete(traceIndexStore.get(i));
            traceIndexStore.remove(traceIndexStore.size()-1);
        }
        traceIndexStore.add(traceStore.add(shapes));
        currStatePointer ++;
    }

    public List<Shape> redo(){
        if(currStatePointer + 1 < traceIndexStore.size()){
            currStatePointer++;
        }
        return traceStore.get(currStatePointer);
    }


    public List<Shape> undo(){
        if(currStatePointer - 1 >= 0)
            currStatePointer--;
        return traceStore.get(currStatePointer);
    }

    private class TraceStore{
        List<Integer> emptyIndexBook;
        List<List<Shape> > Store;

        public TraceStore(){
            emptyIndexBook = new ArrayList<>();
            Store = new ArrayList<>();
        }


        public int add(List<Shape> newShapes){
            int newElemIndex;
            if(emptyIndexBook.isEmpty()){
                Store.add(newShapes);
                newElemIndex = Store.size()-1;
            }
            else {
                Store.set(emptyIndexBook.get(0),newShapes);
                newElemIndex = emptyIndexBook.get(0);
                emptyIndexBook.remove(0);
            }
            return newElemIndex;
        }

        public void delete(int index){
            emptyIndexBook.add(index);
        }

        public List<Shape> get(int index){
            return Store.get(index);
        }

    }
}