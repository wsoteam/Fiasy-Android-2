package com.github.jhonnyx2012.horizontalpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

/**
 * Created by Jhonny Barrios on 22/02/2017.
 */

public class HorizontalPicker extends LinearLayout implements HorizontalPickerListener {

    private static final int NO_SETTED = -1;
    private TextView tvMonth;
    private DatePickerListener listener;
    private HorizontalPickerRecyclerView rvDays;
    private int days;
    private int offset;
    private int mDateSelectedColor = -1;
    private int mDateSelectedTextColor = -1;
    private int mMonthAndYearTextColor = -1;
    private int mTodayDateTextColor = -1;
    private int mTodayDateBackgroundColor = -1;
    private int mDayOfWeekTextColor = -1;
    private int mUnselectedDayTextColor = -1;

    public HorizontalPicker(Context context) {
        super(context);
        internInit();
    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        internInit();

    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        internInit();
    }

    private void internInit() {
        this.days = NO_SETTED;
        this.offset = NO_SETTED;
    }

    public HorizontalPicker setListener(DatePickerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setDate(final DateTime date) {
        rvDays.post(new Runnable() {
            @Override
            public void run() {
                rvDays.setDate(date);
            }
        });
    }

    public void plusDay() {
        rvDays.post(new Runnable() {
            @Override
            public void run() {
                rvDays.plusDay();
            }
        });
    }

    public void minusDay() {
        rvDays.post(new Runnable() {
            @Override
            public void run() {
                rvDays.minusDay();
            }
        });
    }

    public void init() {
        inflate(getContext(), R.layout.horizontal_picker, this);
        rvDays = findViewById(R.id.rvDays);
        int DEFAULT_DAYS_TO_PLUS = 360;
        int finalDays = days == NO_SETTED ? DEFAULT_DAYS_TO_PLUS : days;
        int DEFAULT_INITIAL_OFFSET = 120;
        int finalOffset = offset == NO_SETTED ? DEFAULT_INITIAL_OFFSET : offset;

        tvMonth = findViewById(R.id.tvMonth);

        rvDays.setListener(this);
        tvMonth.setTextColor(mMonthAndYearTextColor != -1 ? mMonthAndYearTextColor : Color.WHITE);
        int mBackgroundColor = getBackgroundColor();
        setBackgroundColor(mBackgroundColor != Color.TRANSPARENT ? mBackgroundColor : Color.WHITE);
        mDateSelectedColor = mDateSelectedColor == -1 ? Color.GRAY : mDateSelectedColor;
        mDateSelectedTextColor = mDateSelectedTextColor == -1 ? Color.parseColor("#ef7d02") : mDateSelectedTextColor;
        mTodayDateTextColor = mTodayDateTextColor == -1 ? Color.WHITE : mTodayDateTextColor;
        mDayOfWeekTextColor = mDayOfWeekTextColor == -1 ? Color.WHITE : mDayOfWeekTextColor;
        mUnselectedDayTextColor = mUnselectedDayTextColor == -1 ? Color.WHITE : mUnselectedDayTextColor;
        rvDays.init(
                getContext(),
                finalDays,
                finalOffset,
                mBackgroundColor,
                mDateSelectedColor,
                mDateSelectedTextColor,
                mTodayDateTextColor,
                mTodayDateBackgroundColor,
                mDayOfWeekTextColor,
                mUnselectedDayTextColor);
    }

    public int getBackgroundColor() {
        int color = Color.TRANSPARENT;
        Drawable background = getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        return color;
    }

    @Override
    public boolean post(Runnable action) {
        return rvDays.post(action);
    }

    @Override
    public void onStopDraggingPicker() {
//        if (vHover.getVisibility() == VISIBLE)
//            vHover.setVisibility(INVISIBLE);
    }

    @Override
    public void onDraggingPicker() {
    }

    @Override
    public void onDateSelected(Day item) {
        if (item == null || listener == null)
            return;
        onMonthSelected(item);
        listener.onDateSelected(item.getDate());
    }

    @Override
    public void onMonthSelected(Day item) {
        if (item == null)
            return;
        int month = Integer.parseInt(item.getNumericMonth());
        tvMonth.setText(getResources().getStringArray(R.array.monthList)[month - 1]);
    }

    public int getDays() {
        return days;
    }

    public HorizontalPicker setDays(int days) {
        this.days = days;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public HorizontalPicker setOffset(int offset) {
        this.offset = offset;
        return this;
    }

}