package com.scarecrow.root.palette;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.scarecrow.root.palette.ShapeClasses.Shape;

public class MainInterfaceActivity extends AppCompatActivity {
    private static int[] Colors = new int[]{Color.RED,Color.BLUE,Color.GRAY,
                                            Color.GREEN,Color.CYAN,Color.MAGENTA};
    private Painter canvasPainter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        final MyCanvas myCanvas = (MyCanvas) findViewById(R.id.canvas);

        canvasPainter = myCanvas.getPainter();

        final NumberPicker spinner = (NumberPicker) findViewById(R.id.color_picker);
        spinner.setMinValue(0);
        spinner.setMaxValue(5);
        spinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        Button button = (Button) findViewById(R.id.button_add_shape);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPainter.exitSelectState();
                int color = spinner.getValue();
                myCanvas.setInAddShapeState(Shape.RECT,Colors[color]);
            }
        });
        button = (Button) findViewById(R.id.button_delete_shape);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPainter.deleteSelectedShape();
            }
        });


        button =(Button) findViewById(R.id.button_redo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPainter.onRedo();
            }
        });

        button = (Button) findViewById(R.id.button_undo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPainter.onUndo();
            }
        });

        button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPainter.save();
            }
        });

        button = (Button) findViewById(R.id.button_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPainter.load();
            }
        });
    }
}
