package com.wsoteam.diet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.6f;
    private static final float BLUR_RADIUS = 15f;
    private static Bitmap blur(Context context, Bitmap image) {
        Log.d("kkkb", "blur: 6");
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        Log.d("kkkb", "blur: 7");
        return outputBitmap;
    }

    public static Bitmap blur(Context context, View view) {
        Log.d("kkkb", "blur: 1");
        if (view != null && context != null) {
            Log.d("kkkb", "blur: 2");
            return blur(context, takeScreenShot(view));
        } else{
            Log.d("kkkb", "blur: 3");
            return null;
        }
    }

    private static Bitmap takeScreenShot(View view) {
        Log.d("kkkb", "blur: 4");
        if (view == null) return null;

        // configuramos para que la view almacene la cache en una imagen
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache();

        if(view.getDrawingCache() == null) return null; // Verificamos antes de que no sea null

        // utilizamos esa cache, para crear el bitmap que tendra la imagen de la view actual
        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        Log.d("kkkb", "blur: 5");
        return snapshot;
    }
}
