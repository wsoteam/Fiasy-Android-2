package com.wsoteam.diet.common.views.graph;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class BarRenderer extends BarChartRenderer {

    private Paint myPaint;
    private int[] myColors;
    private int currentNumber;

    public BarRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int[] myColors, int currentNumber) {
        super(chart, animator, viewPortHandler);
        this.myPaint = new Paint();
        this.myColors = myColors;
        this.currentNumber = currentNumber;
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
        int colorIndex = 0;
        for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {
            BarBuffer buffer = mBarBuffers[i];
            float left, right, top, bottom;
            for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                myPaint.setColor(myColors[colorIndex++]);
                left = buffer.buffer[j];
                right = buffer.buffer[j + 2];
                top = buffer.buffer[j + 1];
                bottom = buffer.buffer[j + 3];
                c.drawArc(left, top - 20, right, top + 23, 180, 180, true, myPaint);
            }
        }
    }


}
