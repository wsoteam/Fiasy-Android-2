package com.losing.weight.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.losing.weight.EntryPoint.ActivitySplash;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;

public class IntentUtils {

  public static void openWebLink(@NonNull Context context, @NonNull String url) {
    Events.logOpenPolitic();
    Events.logPushButtonReg(EventProperties.enter_push_button_privacy);
    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

  public static void openMainActivity(@NonNull Context context) {
    final Intent target = new Intent(context, ActivitySplash.class);
//    target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    context.startActivity(target);
  }
}