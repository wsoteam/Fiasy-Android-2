package com.wsoteam.diet.common.views.water_step;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsoteam.diet.R;

public class WaterStepView extends LinearLayout implements View.OnClickListener {


    private int MAX_PROGRESS;

    private int currentProgress;

    private final int imageSize = dpToPx(52);
    private  int lineCount = getResources().getDisplayMetrics().widthPixels / imageSize;
    private final int rowMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lineCount, getResources().getDisplayMetrics());
    private final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize, imageSize, 1);
    private final LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private final Drawable mSelectedIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_water_selected);
    private final Drawable mUnSelectedIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_water_unselected);
    private final Drawable mAddWaterIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_water_add);
    private LinearLayout rlContainer, firstLinearLayout;
    private OnWaterClickListener listener;
    private int count;

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

   private int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public void setMaxProgress(int MAX_PROGRESS) {
    this.MAX_PROGRESS = MAX_PROGRESS;
    }

  private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_water, this);
        rlContainer = rootView.findViewById(R.id.rlContainer);
        rowLayoutParams.setMargins(0, rowMargin, 0, rowMargin);

        layoutParams.gravity = Gravity.LEFT;

        firstLinearLayout = new LinearLayout(getContext());
        for (int i = 0; i < lineCount; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(i == 0 ? mAddWaterIcon : mUnSelectedIcon);
            imageView.setOnClickListener(this);
            imageView.setTag(i + 1);
            firstLinearLayout.addView(imageView, layoutParams);
        }
        rlContainer.addView(firstLinearLayout, rowLayoutParams);
    }

  @Override
  public void onClick(View view) {
    int item = (int) view.getTag();

    if (item <= currentProgress) {
      currentProgress = item - 1;
    } else {
      currentProgress = item;
    }
    setStepNum(currentProgress, MAX_PROGRESS == 0 || currentProgress < MAX_PROGRESS);
    if (listener != null) {
      listener.onWaterClick(currentProgress);
    }
  }

  public void setStepNum(int _count) {
    this.count = _count;
    redrawView(count, MAX_PROGRESS == 0 || _count < MAX_PROGRESS);
    requestLayout();
  }

    public void setStepNum(int _count, boolean _addMore) {
        this.count = _count;
        redrawView(count, _addMore);
        requestLayout();
    }

    private void redrawView(int count, boolean addMore) {
      currentProgress = count;

        if (rlContainer.getChildCount() > 1) {
            rlContainer.removeViews(1, rlContainer.getChildCount() - 1);
        }

        int rows = count < lineCount ? 1 : (count / lineCount + 1);

        for (int row = 0; row < rows; row++) {

            if (row == 0) {


                for (int i = 0; i < Math.min(lineCount, count); i++) {
                    ImageView imageView = (ImageView) firstLinearLayout.getChildAt(i);
                    imageView.setImageDrawable(mSelectedIcon);
                    imageView.setOnClickListener(this);

                }

                if (count < lineCount) {
                    ImageView imageView = (ImageView) firstLinearLayout.getChildAt(count);
                    imageView.setOnClickListener(this);
                    imageView.setImageDrawable(mAddWaterIcon);
                    for (int i = count + 1; i < lineCount; i++) {
                        ImageView childImageView = (ImageView) firstLinearLayout.getChildAt(i);
                        childImageView.setImageDrawable(mUnSelectedIcon);
                        childImageView.setOnClickListener(this);
                    }
                }

            } else {
                LinearLayout ll = new LinearLayout(getContext());
                int left = count - (row * lineCount);
                for (int i = 0; i < Math.min(lineCount, left); i++) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageDrawable(mSelectedIcon);
                    imageView.setOnClickListener(this);
                    imageView.setTag((row * lineCount) + i + 1);
                    ll.addView(imageView, layoutParams);
                }
                if (addMore && left < lineCount) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageDrawable(mAddWaterIcon);
                    imageView.setOnClickListener(this);
                    imageView.setTag((row * lineCount) + left + 1);
                    ll.addView(imageView, layoutParams);
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
