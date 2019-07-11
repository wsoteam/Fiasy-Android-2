package com.wsoteam.diet.common.helpers;

import android.content.Context;
import android.util.TypedValue;

public class Common {

    public static int convertDpToPx(Context context, int dp) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return Math.round(pixels);
    }
}
