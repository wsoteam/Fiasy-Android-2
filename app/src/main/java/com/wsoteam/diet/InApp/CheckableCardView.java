package com.wsoteam.diet.InApp;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.TextView;

import com.wsoteam.diet.R;

public class CheckableCardView extends CardView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked,
    };

    private boolean isChecked;

    public CheckableCardView(Context context) {
        super(context);
        init(null);
    }

    public CheckableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CheckableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.checkable_card_view, this, true);

        setClickable(true);
        setChecked(false);

        setCardBackgroundColor(ContextCompat.getColorStateList(getContext(), R.color.selector_card_view_background));

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CheckableCardView, 0, 0);
            try {
                String text = ta.getString(R.styleable.CheckableCardView_card_text);
                String text_month = ta.getString(R.styleable.CheckableCardView_card_text_month);
                String text_m = "В МЕСЯЦ";
                TextView itemText = (TextView) findViewById(R.id.text);
                TextView itemTextMonth = (TextView) findViewById(R.id.text3);
                TextView itemTextMonth2 = (TextView) findViewById(R.id.text4);


                if (text != null) {
                    itemText.setText(text);
                    itemTextMonth.setText(text_month);
                    itemTextMonth2.setText("В МЕСЯЦ");
                }

            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;

    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!this.isChecked);
    }
}

