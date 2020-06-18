package com.losing.weight.articles.recycler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.losing.weight.R;
import com.losing.weight.model.Article;
import com.losing.weight.utils.RichTextUtils;
import java.util.Locale;

public class SeriesViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.tvArticleTitle) TextView tvArticleTitle;
  @BindView(R.id.tvDay) TextView tvDay;
  @BindView(R.id.dividerBottom) View dividerBottom;
  @BindView(R.id.dividerTop) View dividerTop;
  @BindView(R.id.radioButton) RadioButton radioButton;

  private Context context;
  private final SeriesCallback callback;

  private Article article;
  private boolean isActivate;

  public SeriesViewHolder(@NonNull ViewGroup parent, SeriesCallback callback) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_article_series, parent, false));
    this.context = parent.getContext();
    ButterKnife.bind(this,itemView);
    this.callback = callback;
  }

  @OnClick(R.id.series_view_holder)
  void ckicked(View v){

    if (isActivate) {
      callback.Clicked(v, article);
    }
  }

  public void bind(Article article, int day, boolean isActive, boolean top, boolean bottom){
    this.article = article;
    this.isActivate = isActive;
    dividerTop.setVisibility(View.VISIBLE);
    dividerBottom.setVisibility(View.VISIBLE);

    if (isActive){
      RichTextUtils.RichText rt = new RichTextUtils.RichText(article.getTitle(Locale.getDefault()).replaceAll("\\<.*?\\>", ""))
          .colorRes(context, R.color.active_drop)
          .underline();
      tvArticleTitle.setText(rt.text());
    } else {
      tvArticleTitle.setText(article.getTitle(Locale.getDefault()).replaceAll("\\<.*?\\>", ""));
      tvDay.setTextColor(Color.parseColor("#59000000"));
      tvArticleTitle.setTextColor(Color.parseColor("#59000000"));
    }
    tvDay.setText(String.format(context.getString(R.string.vh_article_series_day), day));
    radioButton.setChecked(isActive);


    if (top) dividerTop.setVisibility(View.INVISIBLE);
    if (bottom) dividerBottom.setVisibility(View.INVISIBLE);
  }
}
