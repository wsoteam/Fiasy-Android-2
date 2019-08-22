package com.wsoteam.diet.Articles;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.Events;

import java.util.List;

import io.intercom.android.sdk.Intercom;

import static android.content.Context.MODE_PRIVATE;

public class ListArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Article> listItem;
  private Context context;
  private OnItemClickListener onItemClickListener;

  public ListArticlesAdapter(List<Article> listItem, OnItemClickListener onItemClickListener) {
    this.listItem = listItem;
    this.onItemClickListener = onItemClickListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    this.context = viewGroup.getContext();

    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.article_view_holder, viewGroup, false);
    RecyclerView.ViewHolder viewHolder = new ArticleViewHolder(view);
    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onItemClick(v, listItem.get(viewHolder.getAdapterPosition()));
      }
    });

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ((ArticleViewHolder) viewHolder).bind(listItem.get(position));
  }

  @Override
  public int getItemCount() {
    if (listItem == null) return 0;

    return listItem.size();
  }


  public void updateData(List<Article> listItem){
    this.listItem = listItem;
    notifyDataSetChanged();
  }

  class MyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivArticle) ImageView imageView;
    @BindView(R.id.tvArticlePremium) TextView premium;
    @BindView(R.id.tvArticleTitle) TextView title;
    @BindView(R.id.tvArticleIntro) TextView intro;
    @BindView(R.id.cvArticleBorder) CardView cvBorder;

    public MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);

      view.setOnClickListener((View v) -> {
          onItemClickListener.onItemClick(v, listItem.get(getAdapterPosition()));
          //Intent intent;
          //boolean isPremArticle = listItem.get(getAdapterPosition()).isPremium();
          //
          //if (!checkSubscribe() && isPremArticle) {
          //  intent = new Intent(activity, ItemArticleWithoutPremActivity.class);
          //} else {
          //  intent = new Intent(activity, ItemArticleActivity.class);
          //}
          //
          //intent.putExtra(Config.ARTICLE_INTENT, getAdapterPosition());
          //activity.startActivity(intent);
        });
    }

    void bind(int position) {

      boolean isPrem = listItem.get(position).isPremium();
      String url = listItem.get(position).getImgUrl();

      Glide.with(context).load(url).into(imageView);
      title.setText(Html.fromHtml(listItem.get(position).getTitle()));
      intro.setText(Html.fromHtml(listItem.get(position).getIntroPart()));

      if (isPrem) {
        cvBorder.setCardBackgroundColor(Color.parseColor("#FF5722"));
        premium.setVisibility(View.VISIBLE);
      } else {
        cvBorder.setCardBackgroundColor(Color.parseColor("#00FF5722"));
        premium.setVisibility(View.GONE);
      }
    }

    //private boolean checkSubscribe() {
    //  SharedPreferences sharedPreferences =
    //      activity.getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
    //  if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
    //    return true;
    //  } else {
    //    return false;
    //  }
    //}
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener){
    this.onItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, Article dietPlan);
  }
}
