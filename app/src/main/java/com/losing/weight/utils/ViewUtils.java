package com.losing.weight.utils;

import android.view.View;
import androidx.core.util.Consumer;

public class ViewUtils {

  public static void apply(View root, int[] targets, Consumer<View> consumer) {
    for (int target : targets) {
      final View v = root.findViewById(target);
      if (v != null) {

        consumer.accept(v);
      }
    }
  }
}
