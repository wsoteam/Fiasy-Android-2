package com.wsoteam.diet.Articles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.R;


public class ArticleViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageView) ImageView imageView;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.background) LinearLayout llBackground;
  @BindView(R.id.premiumLabel) ConstraintLayout premiumLabel;
  Context context;

  public ArticleViewHolder(ViewGroup parent) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
    ButterKnife.bind(this, itemView);
    context = itemView.getContext();

  }

  public void setOnClickListener(View.OnClickListener listener){
    itemView.setOnClickListener(listener);
  }

  public void bind(Article article){
    Picasso.get().load(article.getImgUrl()).into(imageView);
    tvName.setText(article.getTitle().replaceAll("\\<.*?\\>", ""));

    //Glide.with(context)
    //    .asBitmap()
    //    .load(article.getImgUrl())
    //    .into(new SimpleTarget<Bitmap>() {
    //      @Override
    //      public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
    //        Palette p = Palette.from(resource).generate();
    //        int mainColor = p.getDarkVibrantColor(0);
    //        int alphaColor = 191;
    //        llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
    //      }
    //    });
    //
    Picasso.get().load(article.getImgUrl())
        .into(new Target() {
          @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //TODO не успевает грузить
            Palette p = Palette.from(bitmap).generate();
            int mainColor = p.getDarkVibrantColor(0);
            int alphaColor = 191;
            llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
          }

          @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) {

          }

          @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

          }
        });

    premiumLabel.setVisibility(article.isPremium() ? View.VISIBLE : View.GONE);

  }
}
