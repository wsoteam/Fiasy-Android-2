package com.wsoteam.diet.Articles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ListArticlesAdapter extends RecyclerView.Adapter<ListArticlesAdapter.ArticlesViewHolder>{

    private List<Article> listItem;
    private Context context;
    private Activity activity;

    public ListArticlesAdapter(List<Article> listItem, Activity activity) {
        this.listItem = listItem;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.article_item, viewGroup, false);

        ArticlesViewHolder viewHolder = new ArticlesViewHolder(view);

        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder articlesViewHolder, int position) {

            articlesViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class ArticlesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView premium;
        TextView title;
        TextView intro;
        CardView cvBorder;

        public ArticlesViewHolder(View view){
            super(view);

            imageView = view.findViewById(R.id.ivArticle);
            premium = view.findViewById(R.id.tvArticlePremium);
            title = view.findViewById(R.id.tvArticleTitle);
            intro = view.findViewById(R.id.tvArticleIntro);
            cvBorder = view.findViewById(R.id.cvArticleBorder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    boolean isPremArticle = listItem.get(getAdapterPosition()).isPremium();

                    if (!checkSubscribe() && isPremArticle) {
                        intent = new Intent(activity, ItemArticleWithoutPremActivity.class);
                    } else {
                        intent = new Intent(activity, ItemArticleActivity.class);
                    }

                    intent.putExtra(Config.ARTICLE_INTENT, getAdapterPosition());
                    activity.startActivity(intent);
                }
            });
        }

        void bind(int position){

            boolean isPrem = listItem.get(position).isPremium();
            String url = listItem.get(position).getImgUrl();

            Glide.with(context).load(url).into(imageView);
            title.setText(Html.fromHtml(listItem.get(position).getTitle()));
            intro.setText(Html.fromHtml(listItem.get(position).getIntroPart()));

            if (isPrem){
                cvBorder.setCardBackgroundColor(Color.parseColor("#FF5722"));
                premium.setVisibility(View.VISIBLE);
            } else {
                cvBorder.setCardBackgroundColor(Color.parseColor("#00FF5722"));
                premium.setVisibility(View.GONE);
            }
        }

        private boolean checkSubscribe() {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
