package com.wsoteam.diet.common.views.bar.marker;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.wsoteam.diet.R;

import java.util.List;

public class BarMarker extends MarkerView {
    private TextView tvTitle;
    private int[] colors;
    private List<BarEntry> pairs;

    public BarMarker(Context context, int layoutResource, int[] colors, List<BarEntry> pairs) {
        super(context, layoutResource);
        tvTitle = findViewById(R.id.tvContent);
        this.colors = colors;
        this.pairs = pairs;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvTitle.setTextColor(colors[getIndex(e.getX())]);
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvTitle.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + " " + getContext().getString(R.string.marker_kcal));
        } else {
            tvTitle.setText("" + Utils.formatNumber(e.getY(), 0, true) + " " + getContext().getString(R.string.marker_kcal));
        }
        super.refreshContent(e, highlight);
    }

    private int getIndex(float x) {
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i).getX() == x){
                return i;
            }
        }
        return -1;
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() + 15);
    }
}
