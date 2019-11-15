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
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.PaletteExtractorTransformation;
import io.reactivex.functions.Consumer;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageView) ImageView imageView;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.background) LinearLayout llBackground;
  @BindView(R.id.premiumLabel) ConstraintLayout premiumLabel;
  @BindView(R.id.parentrLayout) ConstraintLayout parentrLayout;
  private HorizontalArticlesAdapter.OnItemClickListener mItemClickListener;

  private Consumer<Palette> paletteConsumer = p -> {
    int mainColor = p.getMutedColor (0);
    int alphaColor = 191;
    llBackground.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
  };

  private Article article;

  public ArticleViewHolder(ViewGroup parent, HorizontalArticlesAdapter.OnItemClickListener mItemClickListener) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
    ButterKnife.bind(this, itemView);
    this.mItemClickListener = mItemClickListener;

    itemView.setOnClickListener(view -> {
      if (mItemClickListener != null) mItemClickListener.onItemClick(view, getAdapterPosition(), article);
    });
  }

  public ArticleViewHolder(ViewGroup parent, int layout) {
    super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    ButterKnife.bind(this, itemView);


  }
  public void setOnClickListener(View.OnClickListener listener){
    itemView.setOnClickListener(listener);
  }

  public void bind(Article article){
    this.article = article;

    tvName.setText(article.getTitle().replaceAll("\\<.*?\\>", ""));
    premiumLabel.setVisibility(article.isPremium() ? View.VISIBLE : View.GONE);

    Picasso.get()
            .load(article.getImage())
            .resizeDimen(R.dimen.article_card_width, R.dimen.article_card_height)
            .centerCrop()
            .config(Bitmap.Config.RGB_565)
            .transform(new PaletteExtractorTransformation(paletteConsumer))
            .into(imageView);

  }
}
