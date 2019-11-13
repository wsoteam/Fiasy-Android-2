package com.wsoteam.diet.utils;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;
import com.squareup.picasso.Transformation;
import io.reactivex.functions.Consumer;
import java.lang.ref.WeakReference;

public class PaletteExtractorTransformation implements Transformation {

  private final WeakReference<Consumer<Palette>> consumer;

  public PaletteExtractorTransformation(Consumer<Palette> consumer) {
    this.consumer = new WeakReference<>(consumer);
  }

  @Override
  public Bitmap transform(Bitmap source) {
    Palette.from(source).generate(palette -> {
      Consumer<Palette> callback = consumer.get();
      if (callback != null) {
        try {
          callback.accept(palette);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    return source;
  }

  @Override
  public String key() {
    return "palette-extractor";
  }
}
