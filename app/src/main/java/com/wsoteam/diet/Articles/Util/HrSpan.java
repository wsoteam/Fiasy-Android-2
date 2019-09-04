package com.wsoteam.diet.Articles.Util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.style.ReplacementSpan;

public class HrSpan extends ReplacementSpan {

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3); // 8px tall line
        paint.setColor(Color.parseColor("#EF7D02"));

        int middle = (top + bottom) / 2;
        // Draw a line across the middle of the canvas
        canvas.drawLine(0, middle, canvas.getWidth(), middle, paint);
    }
}
