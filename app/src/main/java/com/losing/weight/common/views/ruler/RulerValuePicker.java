/*
 * Copyright 2018 Keval Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance wit
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 *  the specific language governing permissions and limitations under the License.
 */

package com.losing.weight.common.views.ruler;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.losing.weight.R;

public final class RulerValuePicker extends FrameLayout implements ObservableHorizontalScrollView.ScrollChangedListener {

    @SuppressWarnings("NullableProblems")
    @NonNull
    private View mLeftSpacer;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private View mRightSpacer;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private RulerView mRulerView;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private ObservableHorizontalScrollView mHorizontalScrollView;

    @Nullable
    private RulerValuePickerListener mListener;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Paint mNotchPaint;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Path mNotchPath;

    private int mNotchColor = Color.WHITE;

    public RulerValuePicker(@NonNull final Context context) {
        super(context);
        init(null);
    }

    public RulerValuePicker(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RulerValuePicker(@NonNull final Context context,
                            @Nullable final AttributeSet attrs,
                            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RulerValuePicker(@NonNull final Context context,
                            @Nullable final AttributeSet attrs,
                            final int defStyleAttr,
                            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attributeSet) {

        //Add detailed the children
        addChildViews();

        if (attributeSet != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet,
                    R.styleable.RulerValuePicker,
                    0,
                    0);

            try { //Parse params
                if (a.hasValue(R.styleable.RulerValuePicker_notch_color)) {
                    mNotchColor = a.getColor(R.styleable.RulerValuePicker_notch_color, Color.WHITE);
                }

                if (a.hasValue(R.styleable.RulerValuePicker_ruler_text_color)) {
                    setTextColor(a.getColor(R.styleable.RulerValuePicker_ruler_text_color, Color.WHITE));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_ruler_text_size)) {
                    setTextSize((int) a.getDimension(R.styleable.RulerValuePicker_ruler_text_size, 14));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_indicator_color)) {
                    setIndicatorColor(a.getColor(R.styleable.RulerValuePicker_indicator_color, Color.WHITE));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_indicator_width)) {
                    setIndicatorWidth(a.getDimensionPixelSize(R.styleable.RulerValuePicker_indicator_width,
                            4));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_indicator_interval)) {
                    setIndicatorIntervalDistance(a.getDimensionPixelSize(R.styleable.RulerValuePicker_indicator_interval,
                            4));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_long_height_height_ratio)
                        || a.hasValue(R.styleable.RulerValuePicker_short_height_height_ratio)) {

                    setIndicatorHeight(a.getFraction(R.styleable.RulerValuePicker_long_height_height_ratio,
                            1, 1, 0.4f),
                            a.getFraction(R.styleable.RulerValuePicker_middle_height_height_ratio,
                                    1, 1, 0.3f),
                            a.getFraction(R.styleable.RulerValuePicker_short_height_height_ratio,
                                    1, 1, 0.1f));
                }

                if (a.hasValue(R.styleable.RulerValuePicker_min_value) ||
                        a.hasValue(R.styleable.RulerValuePicker_max_value)) {
                    setMinMaxValue(a.getInteger(R.styleable.RulerValuePicker_min_value, 0),
                            a.getInteger(R.styleable.RulerValuePicker_max_value, 100));
                }
            } finally {
                a.recycle();
            }
        }

        //Prepare the notch color.
        mNotchPaint = new Paint();
        prepareNotchPaint();

        mNotchPath = new Path();
    }

    private void prepareNotchPaint() {
        mNotchPaint.setColor(mNotchColor);
        mNotchPaint.setStrokeWidth(5f);
        mNotchPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void addChildViews() {
        mHorizontalScrollView = new ObservableHorizontalScrollView(getContext(), this);
        mHorizontalScrollView.setHorizontalScrollBarEnabled(false);

        final LinearLayout rulerContainer = new LinearLayout(getContext());

        mLeftSpacer = new View(getContext());
        rulerContainer.addView(mLeftSpacer);

        mRulerView = new RulerView(getContext());
        rulerContainer.addView(mRulerView);

        mRightSpacer = new View(getContext());
        rulerContainer.addView(mRightSpacer);

        mHorizontalScrollView.removeAllViews();
        mHorizontalScrollView.addView(rulerContainer);

        removeAllViews();
        addView(mHorizontalScrollView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mNotchPath, mNotchPaint);
    }

    @Override
    protected void onLayout(boolean isChanged, int left, int top, int right, int bottom) {
        super.onLayout(isChanged, left, top, right, bottom);

        if (isChanged) {
            final int width = getWidth();

            //Set width of the left spacer to the half of this view.
            final ViewGroup.LayoutParams leftParams = mLeftSpacer.getLayoutParams();
            leftParams.width = width / 2;
            mLeftSpacer.setLayoutParams(leftParams);

            //Set width of the right spacer to the half of this view.
            final ViewGroup.LayoutParams rightParams = mRightSpacer.getLayoutParams();
            rightParams.width = width / 2;
            mRightSpacer.setLayoutParams(rightParams);

            calculateNotchPath();

            invalidate();
        }
    }

    private void calculateNotchPath() {
        mNotchPath.reset();

        mNotchPath.moveTo(getWidth() / 2 - 30, 0);
        mNotchPath.lineTo(getWidth() / 2, 40);
        mNotchPath.lineTo(getWidth() / 2 + 30, 0);
    }

    public void selectValue(final int value) {
        mHorizontalScrollView.postDelayed(() -> {
            int valuesToScroll;
            if (value < mRulerView.getMinValue()) {
                valuesToScroll = 0;
            } else if (value > mRulerView.getMaxValue()) {
                valuesToScroll = mRulerView.getMaxValue() - mRulerView.getMinValue();
            } else {
                valuesToScroll = value - mRulerView.getMinValue();
            }

            mHorizontalScrollView.smoothScrollTo(
                    valuesToScroll * mRulerView.getIndicatorIntervalWidth(), 0);
        }, 400);
    }

    public int getCurrentValue() {
        int absoluteValue = mHorizontalScrollView.getScrollX() / mRulerView.getIndicatorIntervalWidth();
        int value = mRulerView.getMinValue() + absoluteValue;

        if (value > mRulerView.getMaxValue()) {
            return mRulerView.getMaxValue();
        } else if (value < mRulerView.getMinValue()) {
            return mRulerView.getMinValue();
        } else {
            return value;
        }
    }

    @Override
    public void onScrollChanged() {
        if (mListener != null) mListener.onIntermediateValueChange(getCurrentValue());
    }

    @Override
    public void onScrollStopped() {
        makeOffsetCorrection(mRulerView.getIndicatorIntervalWidth());
        if (mListener != null) {
            mListener.onValueChange(getCurrentValue());
        }
    }

    private void makeOffsetCorrection(final int indicatorInterval) {
        int offsetValue = mHorizontalScrollView.getScrollX() % indicatorInterval;
        if (offsetValue < indicatorInterval / 2) {
            mHorizontalScrollView.scrollBy(-offsetValue, 0);
        } else {
            mHorizontalScrollView.scrollBy(indicatorInterval - offsetValue, 0);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = getCurrentValue();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        selectValue(ss.value);
    }

    public void setNotchColorRes(@ColorRes final int notchColorRes) {
        setNotchColor(ContextCompat.getColor(getContext(), notchColorRes));
    }

    @ColorInt
    public int getNotchColor() {
        return mNotchColor;
    }

    public void setNotchColor(@ColorInt final int notchColor) {
        mNotchColor = notchColor;
        prepareNotchPaint();
        invalidate();
    }

    @CheckResult
    @ColorInt
    public int getTextColor() {
        return mRulerView.getTextColor();
    }

    public void setTextColor(@ColorInt final int color) {
        mRulerView.setTextColor(color);
    }

    public void setTextColorRes(@ColorRes final int color) {
        setTextColor(ContextCompat.getColor(getContext(), color));
    }

    @CheckResult
    public float getTextSize() {
        return mRulerView.getTextSize();
    }

    public void setTextSize(final int dimensionDp) {
        mRulerView.setTextSize(dimensionDp);
    }

    public void setTextSizeRes(@DimenRes final int dimension) {
        setTextSize((int) getContext().getResources().getDimension(dimension));
    }

    @CheckResult
    @ColorInt
    public int getIndicatorColor() {
        return mRulerView.getIndicatorColor();
    }

    public void setIndicatorColor(@ColorInt final int color) {
        mRulerView.setIndicatorColor(color);
    }

    public void setIndicatorColorRes(@ColorRes final int color) {
        setIndicatorColor(ContextCompat.getColor(getContext(), color));
    }

    @CheckResult
    public float getIndicatorWidth() {
        return mRulerView.getIndicatorWidth();
    }

    public void setIndicatorWidth(final int widthPx) {
        mRulerView.setIndicatorWidth(widthPx);
    }

    public void setIndicatorWidthRes(@DimenRes final int width) {
        setIndicatorWidth(getContext().getResources().getDimensionPixelSize(width));
    }

    @CheckResult
    public int getMinValue() {
        return mRulerView.getMinValue();
    }

    @CheckResult
    public int getMaxValue() {
        return mRulerView.getMaxValue();
    }

    public void setMinMaxValue(final int minValue, final int maxValue) {
        mRulerView.setValueRange(minValue, maxValue);
        invalidate();
        selectValue(minValue);
    }

    @CheckResult
    public int getIndicatorIntervalWidth() {
        return mRulerView.getIndicatorIntervalWidth();
    }

    public void setIndicatorIntervalDistance(final int indicatorIntervalPx) {
        mRulerView.setIndicatorIntervalDistance(indicatorIntervalPx);
    }

    @CheckResult
    public float getLongIndicatorHeightRatio() {
        return mRulerView.getLongIndicatorHeightRatio();
    }

    @CheckResult
    public float getShortIndicatorHeightRatio() {
        return mRulerView.getShortIndicatorHeightRatio();
    }

    public void setIndicatorHeight(final float longHeightRatio,
                                   final float middleHeightRatio,
                                   final float shortHeightRatio) {
        mRulerView.setIndicatorHeight(longHeightRatio, middleHeightRatio, shortHeightRatio);
    }

    public void setValuePickerListener(@Nullable final RulerValuePickerListener listener) {
        mListener = listener;
    }

    public static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        private int value = 0;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
        }
    }
}