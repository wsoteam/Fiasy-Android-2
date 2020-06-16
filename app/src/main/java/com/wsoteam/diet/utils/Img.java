package com.wsoteam.diet.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.R;

import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class Img {
    public static void setBackGround(Drawable drawable, View background){
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
            if(bitmap != null) {
                Palette p = Palette.from(bitmap).generate();
                int mainColor = p.getMutedColor(0);
                int alphaColor = 191;
                background.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
            }
        }
    }

    public static void setImg(ImageView img, String url, View view){
        Picasso.get()
                .load(url)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Img.setBackGround(img.getDrawable(), view);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    public static void setBlurImg(ImageView img, String url, View view){

        Picasso.get()
                .load(url)
                .fit().centerCrop()
                .transform(new BlurTransformation(img.getContext(), 25, 1))
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Img.setBackGround(img.getDrawable(), view);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
}
