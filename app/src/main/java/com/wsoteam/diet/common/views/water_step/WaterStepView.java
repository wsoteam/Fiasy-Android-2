package com.wsoteam.diet.common.views.water_step;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsoteam.diet.R;

public class WaterStepView extends LinearLayout implements View.OnClickListener {

    private static final int lineCount = 8;
    private final int rowMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    private final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
    private final LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final Drawable mSelectedIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_water_selected);
    private final Drawable mUnSelectedIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_water_unselected);
    private final Drawable mAddWaterIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_water_add);
    private LinearLayout rlContainer, firstLineatLayout;
    private OnWaterClickListener listener;
    private int waterWidth, count;

    public WaterStepView(Context context) {
        super(context);
    }

    public WaterStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_water, this);
        rlContainer = rootView.findViewById(R.id.rlContainer);
        rowLayoutParams.setMargins(0, rowMargin, 0, rowMargin);

        firstLineatLayout = new LinearLayout(getContext());
        for (int i = 0; i < lineCount; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(i == 0 ? mAddWaterIcon : mUnSelectedIcon);
            imageView.setOnClickListener(i == 0 ? this : null);
            imageView.setTag(i + 1);
            firstLineatLayout.addView(imageView, layoutParams);
        }
        rlContainer.addView(firstLineatLayout, rowLayoutParams);
    }

    @Override
    public void onClick(View view) {
        listener.onWaterClick(count + 1);
    }

    public void setStepNum(int count, boolean addMore) {
        this.count = count;
        redrawView(count, addMore);
        requestLayout();
    }

    private void redrawView(int count, boolean addMore) {
        if (rlContainer.getChildCount() > 1)
            rlContainer.removeViews(1, rlContainer.getChildCount() - 1);
        int rows = count < lineCount ? 1 : (count / lineCount + 1);
        for (int row = 0; row < rows; row++) {
            if (row == 0) {
                for (int i = 0; i < Math.min(lineCount, count); i++) {
                    ImageView imageView = (ImageView) firstLineatLayout.getChildAt(i);
                    imageView.setImageDrawable(mSelectedIcon);
                    imageView.setOnClickListener(null);
                    waterWidth = imageView.getWidth();
                }
                if (count < lineCount) {
                    ImageView imageView = (ImageView) firstLineatLayout.getChildAt(count);
                    imageView.setOnClickListener(this);
                    imageView.setImageDrawable(mAddWaterIcon);
                }
            } else {
                LinearLayout ll = new LinearLayout(getContext());
                int left = count - (row * lineCount);
                for (int i = 0; i < Math.min(lineCount, left); i++) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageDrawable(mSelectedIcon);
                    imageView.setMinimumWidth(waterWidth);
                    imageView.setOnClickListener(null);
                    imageView.setTag((row * lineCount) + i + 1);
                    ll.addView(imageView);
                }
                if (addMore && left < lineCount) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageDrawable(mAddWaterIcon);
                    imageView.setMinimumWidth(waterWidth);
                    imageView.setOnClickListener(this);
                    imageView.setTag((row * lineCount) + left + 1);
                    ll.addView(imageView);
                }
                rlContainer.addView(ll, rowLayoutParams);
            }
        }
    }

    public void setOnWaterClickListener(OnWaterClickListener listener) {
        this.listener = listener;
    }

    public interface OnWaterClickListener {
        void onWaterClick(int progress);
    }
}
