package com.wsoteam.diet.Articles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.R;


public class ArticleViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageView) ImageView imageView;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.background) LinearLayout llBackground;
  Context context;

  public ArticleViewHolder(@NonNull View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    context = itemView.getContext();

  }

  public void bind(Article article){
    Glide.with(context).load(article.getImgUrl()).into(imageView);
    tvName.setText(article.getTitle().replaceAll("\\<.*?\\>", ""));

    Glide.with(context)
        .asBitmap()
        .load(article.getImgUrl())
        .into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            Palette p = Palette.from(resource).generate();
            int mainColor = p.getDarkVibrantColor(0);
            int alphaColor = 191;
            llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
          }
        });

  }
}
