package com.losing.weight.articles.recycler;

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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.losing.weight.model.Article;
import com.losing.weight.R;
import com.losing.weight.utils.Img;

import io.reactivex.functions.Consumer;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageView) ImageView imageView;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.background) LinearLayout llBackground;
  @BindView(R.id.premiumLabel) ConstraintLayout premiumLabel;
  @BindView(R.id.parentrLayout) ConstraintLayout parentrLayout;
  private ListArticlesAdapter.OnItemClickListener onItemClickListener;
  private HorizontalArticlesAdapter.OnItemClickListener mItemClickListener;

  private Consumer<Palette> paletteConsumer = p -> {
    int mainColor = p.getMutedColor (0);
    int alphaColor = 191;
    llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
  };

  private Article article;

  public ArticleViewHolder(ViewGroup parent, ListArticlesAdapter.OnItemClickListener onItemClickListener) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
    ButterKnife.bind(this, itemView);
    this.onItemClickListener = onItemClickListener;

    itemView.setOnClickListener(view -> {
      if (onItemClickListener != null) onItemClickListener.onItemClick(view, article);
    });
  }

  public ArticleViewHolder(ViewGroup parent, HorizontalArticlesAdapter.OnItemClickListener mItemClickListener) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
    ButterKnife.bind(this, itemView);
    this.mItemClickListener = mItemClickListener;

    itemView.setOnClickListener(view -> {
      if (mItemClickListener != null) mItemClickListener.onItemClick(view, getAdapterPosition(), article);
    });
  }

  public ArticleViewHolder(ViewGroup parent, int layout, ListArticlesAdapter.OnItemClickListener onItemClickListener) {
    super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    ButterKnife.bind(this, itemView);

    this.onItemClickListener = onItemClickListener;

    itemView.setOnClickListener(view -> {
      if (onItemClickListener != null) onItemClickListener.onItemClick(view, article);
    });

  }
  public void setOnClickListener(View.OnClickListener listener){
    itemView.setOnClickListener(listener);
  }

  public void bind(Article article){
    this.article = article;

    tvName.setText(article.getTitle().replaceAll("\\<.*?\\>", ""));
//    premiumLabel.setVisibility(article.isPremium() ? View.VISIBLE : View.GONE);
    setImg(imageView, article.getImage(), llBackground);

  }

  private void setImg(ImageView img, String url, LinearLayout layout){
    Picasso.get()
            .load(url)
            .resizeDimen(R.dimen.article_card_width, R.dimen.article_card_height)
            .centerCrop()
            .into(img, new Callback() {
              @Override
              public void onSuccess() {
                Img.setBackGround(img.getDrawable(), layout);
              }

              @Override
              public void onError(Exception e) {

              }
            });
  }
}
