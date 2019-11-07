package com.wsoteam.diet.articles.recycler;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.R;


public class ArticleViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageView) ImageView imageView;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.background) LinearLayout llBackground;
  @BindView(R.id.premiumLabel) ConstraintLayout premiumLabel;
  @BindView(R.id.parentrLayout) ConstraintLayout parentrLayout;
  Context context;

  public ArticleViewHolder(ViewGroup parent) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
    ButterKnife.bind(this, itemView);
    context = itemView.getContext();

  }

  public ArticleViewHolder(ViewGroup parent, int layout) {
    super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    ButterKnife.bind(this, itemView);
    context = itemView.getContext();

  }
  public void setOnClickListener(View.OnClickListener listener){
    itemView.setOnClickListener(listener);
  }

  public void bind(Article article){
    Glide.with(context).load(article.getImage()).into(imageView);
    tvName.setText(article.getTitle().replaceAll("\\<.*?\\>", ""));

    Glide.with(context)
        .asBitmap()
        .load(article.getImage())
        .into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            Palette p = Palette.from(resource).generate();
            int mainColor = p.getDarkVibrantColor(0);
            int alphaColor = 191;
            llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
          }
        });

    premiumLabel.setVisibility(article.isPremium() ? View.VISIBLE : View.GONE);

  }
}
