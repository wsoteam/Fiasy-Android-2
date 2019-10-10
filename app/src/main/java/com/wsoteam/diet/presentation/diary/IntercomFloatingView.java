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
import android.util.Log;
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
  private ObjectAnimator animator;
  private boolean expanded = false;

  public IntercomFloatingView(Context context) {
    this(context, null);
  }

  public IntercomFloatingView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    int w = Metrics.dp(context, 72);
    int h = Metrics.dp(context, 56);

    floatingActionBounds.set(0, 0, w, h);

    paint.setStyle(Paint.Style.FILL);
    paint.setColor(ContextCompat.getColor(context, R.color.orange));

    setOnClickListener(v -> {
      if (animator != null && animator.isRunning()) {
        animator.cancel();
      }

      final Rect from = new Rect((int) floatingActionBounds.left,
          (int) floatingActionBounds.top,
          (int) floatingActionBounds.right,
          (int) floatingActionBounds.bottom);

      final double size = Math.hypot(getWidth(), getHeight());
      final Rect to = new Rect();

      if (!expanded) {
        to.set(0, 0, (int) size, (int) size);
        to.offsetTo(-getWidth() / 2, -getHeight() / 2);
        expanded = true;
      } else {
        to.set(0, 0, w, h);
        to.offsetTo(
            (int) (getWidth() - w * 0.5f),
            getHeight() - h - Metrics.dp(getContext(), 24)
        );
        expanded = false;
      }

      animator = ObjectAnimator.ofObject(this, "changeBounds",
          new RectEvaluator(), from, to);
      animator.setDuration(300);
      animator.start();
    });
  }

  public void setChangeBounds(Rect rect) {
    Log.d("Lol", "Lol=" + rect);

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

    if (!expanded) {
      floatingActionBounds.offsetTo(
          (right - left) - floatingActionBounds.width() * 0.5f,
          (bottom - top) - floatingActionBounds.height() - Metrics.dp(getContext(), 24)
      );
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    //if (floatingActionBounds.contains(event.getX(), event.getY())) {
    //  return true;
    //}
    return super.onTouchEvent(event);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Log.d("Lol", "drawing=" + floatingActionBounds);
    canvas.drawOval(floatingActionBounds, paint);
  }
}
