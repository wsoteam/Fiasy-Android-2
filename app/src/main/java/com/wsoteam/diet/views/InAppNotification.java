package com.wsoteam.diet.views;

import android.content.Context;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.IntDef;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.Metrics;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Used to show in-app notifications
 */
public class InAppNotification {

  public static final int DURATION_QUICK = 0;
  public static final int DURATION_LONG = 1;
  public static final int DURATION_FOREVER = 2;

  @IntDef({ DURATION_QUICK, DURATION_LONG, DURATION_FOREVER })
  public @interface Duration {

  }

  private final ProgressBar progressView;
  private final TextView textView;
  private final ViewGroup rootView;

  private final Runnable dismissCallback = this::dismiss;

  private ViewGroup parent;

  public InAppNotification(Context context) {
    rootView = (ViewGroup) LayoutInflater.from(context)
        .inflate(R.layout.view_custom_toast, null);

    rootView.setTranslationY(Metrics.dp(context, 16));

    textView = rootView.findViewById(R.id.text);
    progressView = rootView.findViewById(R.id.progress);
  }

  public void setText(CharSequence text) {
    TransitionManager.beginDelayedTransition(rootView);

    textView.setText(text);
    textView.requestLayout();
  }

  public void show(View view, @Duration int duration) {
    parent = findSuitableParent(view);

    if (parent == null) {
      throw new IllegalArgumentException(
          "No suitable parent found from the given view. Please provide a valid view.");
    }

    if (parent instanceof CoordinatorLayout) {
      final CoordinatorLayout.LayoutParams lp =
          new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

      lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

      rootView.setLayoutParams(lp);
    } else if (parent instanceof FrameLayout) {
      final FrameLayout.LayoutParams lp =
          new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

      lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

      rootView.setLayoutParams(lp);
    }

    if (!isAttached()) {
      final Slide slide = new Slide();
      slide.setSlideEdge(Gravity.TOP);

      TransitionManager.beginDelayedTransition(parent, slide);

      parent.addView(rootView);
    } else {
      rootView.requestLayout();
    }

    if (duration != DURATION_FOREVER) {
      final int delay;

      if (duration == DURATION_LONG) {
        delay = 3000;
      } else {
        delay = 1500;
      }

      delayedDismiss(delay);
    }
  }

  public boolean isAttached() {
    return parent != null && parent.indexOfChild(rootView) >= 0;
  }

  public void delayedDismiss(long delay) {
    parent.removeCallbacks(dismissCallback);
    parent.postDelayed(dismissCallback, delay);
  }

  public void dismiss() {
    if (parent == null) {
      throw new IllegalStateException("notification not showing");
    }

    final Slide slide = new Slide();
    slide.setSlideEdge(Gravity.TOP);

    TransitionManager.beginDelayedTransition(parent, slide);

    parent.removeView(rootView);
    parent = null;
  }

  public void setProgressVisible(boolean visible, boolean changeWithAnimation) {
    final int state = visible ? View.VISIBLE : View.GONE;

    if (progressView.getVisibility() == state) {
      return;
    }

    TransitionManager.beginDelayedTransition(rootView);

    if (!changeWithAnimation) {
      progressView.setVisibility(state);
    } else {

      if (state == View.VISIBLE) {
        progressView.animate().cancel();
        progressView.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(350)
            .setInterpolator(new AccelerateInterpolator())
            .withStartAction(() -> {
              progressView.setVisibility(View.VISIBLE);
              progressView.setScaleX(0.4f);
              progressView.setScaleY(0.4f);
            })
            .start();
      } else {
        progressView.animate().cancel();
        progressView.animate()
            .scaleX(0.5f)
            .scaleY(0.5f)
            .alpha(0f)
            .setDuration(350)
            .setInterpolator(new AccelerateInterpolator())
            .withEndAction(() -> {
              progressView.setVisibility(View.GONE);
              progressView.setAlpha(1f);
            })
            .start();
      }
    }
  }

  public ViewGroup getRootView() {
    return rootView;
  }

  public TextView getTextView() {
    return textView;
  }

  public ProgressBar getProgressView() {
    return progressView;
  }

  private static ViewGroup findSuitableParent(View view) {
    ViewGroup fallback = null;

    do {
      if (view instanceof CoordinatorLayout) {
        return (ViewGroup) view;
      }

      if (view instanceof FrameLayout) {
        if (view.getId() == android.R.id.content) {
          return (ViewGroup) view;
        }

        fallback = (ViewGroup) view;
      }

      if (view != null) {
        ViewParent parent = view.getParent();
        view = parent instanceof View ? (View) parent : null;
      }
    } while (view != null);

    return fallback;
  }
}
