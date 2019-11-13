package com.wsoteam.diet.articles.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.R;


public class ArticleViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageView) ImageView imageView;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.background) LinearLayout llBackground;
  @BindView(R.id.premiumLabel) ConstraintLayout premiumLabel;
  @BindView(R.id.parentrLayout) ConstraintLayout parentrLayout;
  private Context context;
  private HorizontalArticlesAdapter.OnItemClickListener mItemClickListener;

  private Article article;

  public ArticleViewHolder(ViewGroup parent, HorizontalArticlesAdapter.OnItemClickListener mItemClickListener) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
    ButterKnife.bind(this, itemView);
    this.context = itemView.getContext();
    this.mItemClickListener = mItemClickListener;

    itemView.setOnClickListener(view -> {
      if (mItemClickListener != null) mItemClickListener.onItemClick(view, getAdapterPosition(), article);
    });
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
    this.article = article;
    Picasso.get().load(article.getImage()).into(imageView);
    tvName.setText(article.getTitle().replaceAll("\\<.*?\\>", ""));

//    llBackground.setBackgroundColor(Color.parseColor(article.getTitle_color()));
    Picasso.get()
        .load(article.getImage())
        .into(new Target() {
          @Override
          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Palette p = Palette.from(bitmap).generate();
//            int mainColor = p.getDarkVibrantColor(0);
            int mainColor = p.getMutedColor(0);
            int alphaColor = 191;
            llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
          }

          @Override
          public void onBitmapFailed(Exception e, Drawable errorDrawable) {

          }

          @Override
          public void onPrepareLoad(Drawable placeHolderDrawable) {

          }
        });

    premiumLabel.setVisibility(article.isPremium() ? View.VISIBLE : View.GONE);

  }
}
