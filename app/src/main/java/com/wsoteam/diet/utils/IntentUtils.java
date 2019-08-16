package com.wsoteam.diet.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.common.Analytics.Events;

public class IntentUtils {

  public static void openWebLink(@NonNull Context context, @NonNull String url) {
    Events.logOpenPolitic();
    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

  public static void openMainActivity(@NonNull Context context) {
    final Intent target = new Intent(context, ActivitySplash.class);
    target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    context.startActivity(target);
  }
}
