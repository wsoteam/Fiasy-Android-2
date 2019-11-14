package com.wsoteam.diet.common.views.graph.marker;

import android.content.Context;
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
    private int lineMarkerColor;
    private List<BarEntry> pairs;
    private boolean isBarClick = true;


    public BarMarker(Context context, int layoutResource, int[] colors, List<BarEntry> pairs, int lineMarkerColor) {
        super(context, layoutResource);
        tvTitle = findViewById(R.id.tvContent);
        this.colors = colors;
        this.lineMarkerColor = lineMarkerColor;
        this.pairs = pairs;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof  BarEntry){
            isBarClick = true;
        }else {
            isBarClick = false;
        }
        if (isBarClick) {
            tvTitle.setTextColor(colors[getIndex(e.getX())]);
        }else {
            tvTitle.setTextColor(lineMarkerColor);
        }
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvTitle.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + " " + getContext().getString(R.string.srch_kcal));
        } else {
            tvTitle.setText("" + Utils.formatNumber(e.getY(), 0, true) + " " + getContext().getString(R.string.srch_kcal));
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
        if (isBarClick){
            return new MPPointF(-(getWidth() / 2), -getHeight() + 20);
        }else {
            return new MPPointF(-(getWidth() / 2), -getHeight() - 25);
        }
    }
}
