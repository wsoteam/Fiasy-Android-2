package com.wsoteam.diet.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

  public static void openWebLink(Context context, String url){
    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

}
