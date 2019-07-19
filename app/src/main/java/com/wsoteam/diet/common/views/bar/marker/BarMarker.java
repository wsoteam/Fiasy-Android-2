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

public class BarMarker extends MarkerView {
    private TextView tvTitle;
    private int[] colors;

    public BarMarker(Context context, int layoutResource, int[] colors) {
        super(context, layoutResource);
        tvTitle = findViewById(R.id.tvContent);
        this.colors = colors;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvTitle.setTextColor(colors[highlight.getDataSetIndex()]);
        Log.e("LOL", String.valueOf(highlight.getStackIndex()));
        Log.e("LOL", String.valueOf(highlight.getDataSetIndex()));
        Log.e("LOL", String.valueOf(e.getData()));
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            BarEntry barEntry = (BarEntry) e;
            tvTitle.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + " " + getContext().getString(R.string.marker_kcal));

        } else {
            tvTitle.setText("" + Utils.formatNumber(e.getY(), 0, true) + " " + getContext().getString(R.string.marker_kcal));
        }
        super.refreshContent(e, highlight);
    }



    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
