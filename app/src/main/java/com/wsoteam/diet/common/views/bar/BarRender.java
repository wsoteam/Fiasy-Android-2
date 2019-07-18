package com.wsoteam.diet.common.views.bar;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
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
        for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {
            BarBuffer buffer = mBarBuffers[i];
            float left, right, top, bottom;
            for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                myPaint.setColor(myColors.get(0));
                left = buffer.buffer[j];
                right = buffer.buffer[j + 2];
                top = buffer.buffer[j + 1];
                bottom = buffer.buffer[j + 3];
                c.drawArc(left, top - 10, right, top + 15, 180, 180, true, myPaint);
            }
        }
    }
}
