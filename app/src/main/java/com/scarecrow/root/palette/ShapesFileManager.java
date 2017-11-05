package com.scarecrow.root.palette;

import android.os.Environment;
import android.util.Log;

import com.scarecrow.root.palette.ShapeClasses.Shape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-10-13.
 * Model part
 */

public class ShapesFileManager {
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory().toString();
    private List<Shape> innerSerializableList;

    public void saveToFile(List<Shape> shapeList) {
        innerSerializableList = new ArrayList<>(shapeList);
        writeObjectToFile(innerSerializableList);
    }

    public List<Shape> loadFromFile() {
        innerSerializableList = (ArrayList<Shape>) readObjectFromFile();
        return new LinkedList<>(innerSerializableList);
    }

    private void writeObjectToFile(Object obj) {
        Log.d(TAG, "writeObjectToFile: ");
        File file = new File(ALBUM_PATH, "shape_list.dat");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    private Object readObjectFromFile() {
        Object obj;
        Log.d(TAG, "readObjectFromFile: ");
        File file = new File(ALBUM_PATH, "shape_list.dat");
        FileInputStream inputStream;
        ObjectInputStream objIn;
        try {
            inputStream = new FileInputStream(file);
            objIn = new ObjectInputStream(inputStream);
            obj = objIn.readObject();
            inputStream.close();
            objIn.close();
        } catch (Exception ee) {
            obj = null;
            Log.e(TAG, "readObjectFromFile: " + ee);
        }
        return obj;
    }

}

