package com.losing.weight.utils;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;
import com.squareup.picasso.Transformation;
import io.reactivex.functions.Consumer;

public class PaletteExtractorTransformation implements Transformation {

  private final Consumer<Palette> consumer;

  public PaletteExtractorTransformation(Consumer<Palette> consumer) {
    this.consumer = consumer;
  }

  @Override
  public Bitmap transform(Bitmap source) {
    Palette.from(source).generate(palette -> {
      try {
        consumer.accept(palette);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    return source;
  }

  @Override
  public String key() {
    return "palette-extractor";
  }
}
