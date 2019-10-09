package com.wsoteam.diet.presentation.diary;

import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.Metrics;

public class IntercomFloatingView extends View {

  private final int tresholdWidth = 0;

  private final Paint paint = new Paint();
  private final PointF downPoint = new PointF();

  private final RectF floatingActionBounds = new RectF();
  private final ObjectAnimator animator;
  //
  //public IntercomFloatingView(Context context) {
  //  super(context);
  //}

  public IntercomFloatingView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    int w = Metrics.dp(context, 72);
    int h = Metrics.dp(context, 56);

    floatingActionBounds.set(0, 0, w, h);

    paint.setStyle(Paint.Style.FILL);
    paint.setColor(ContextCompat.getColor(context, R.color.orange));

    animator = new ObjectAnimator();
    animator.setTarget(this);
    animator.setPropertyName("changeBounds");

    setOnClickListener(v -> {
      if (animator.isRunning()) {
        animator.cancel();
      }

      animator.setObjectValues(
          new Rect((int) floatingActionBounds.left,
              (int) floatingActionBounds.top,
              (int) floatingActionBounds.right,
              (int) floatingActionBounds.bottom),
          new Rect(0, 0, getWidth(), getHeight())
      );
      animator.setEvaluator(new RectEvaluator());
      animator.setDuration(300);

      animator.start();
    });
  }

  void changeBounds(Rect rect) {
    floatingActionBounds.set(rect.left, rect.top, rect.right, rect.bottom);
    invalidate();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //super.onMeasure(widthMeasureSpec, MeasureSpec.getSize(heightMeasureSpec));

    setMeasuredDimension(
        MeasureSpec.getSize(widthMeasureSpec),
        MeasureSpec.getSize(heightMeasureSpec)
    );
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    floatingActionBounds.offsetTo(
        (right - left) - floatingActionBounds.width() * 0.5f,
        (bottom - top) - floatingActionBounds.height() - Metrics.dp(getContext(), 24)
    );
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    //if (floatingActionBounds.contains(event.getX(), event.getY())) {
    //  return true;
    //}
    return super.onTouchEvent(event);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.drawOval(floatingActionBounds, paint);
  }
}
