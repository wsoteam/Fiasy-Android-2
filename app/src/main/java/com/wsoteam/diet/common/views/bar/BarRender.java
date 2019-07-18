package com.wsoteam.diet.common.views.bar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class BarRender extends BarChartRenderer {

    private Paint myPaint;
    private ArrayList<Integer> myColors;

    public BarRender(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, ArrayList<Integer> myColors) {
        super(chart, animator, viewPortHandler);
        this.myPaint = new Paint();
        this.myColors = myColors;
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
        // you can modify the original method
        // so that everything is drawn on the canvas inside a single loop
        // also you can add logic here to meet your requirements
        int colorIndex = 0;
        for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {
            BarBuffer buffer = mBarBuffers[i];
            float left, right, top, bottom;
            for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                myPaint.setColor(myColors.get(0));
                left = buffer.buffer[j];
                right = buffer.buffer[j + 2];
                top = buffer.buffer[j + 1];
                bottom = buffer.buffer[j + 3];
//                myPaint.setShader(new LinearGradient(left,top,right,bottom, Color.CYAN, myColors.get(colorIndex++), Shader.TileMode.MIRROR ));
                //c.drawRect(left, top + 50, right, top+5f, myPaint);
                c.drawOval(left, top - 5, right, top + 10, myPaint);
            }
        }
    }
}
