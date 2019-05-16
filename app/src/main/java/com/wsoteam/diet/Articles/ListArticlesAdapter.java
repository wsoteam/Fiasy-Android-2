package com.wsoteam.diet.Articles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

        public ArticlesViewHolder(View view){
            super(view);

            imageView = view.findViewById(R.id.ivArticle);
            premium = view.findViewById(R.id.tvArticlePremium);
            title = view.findViewById(R.id.tvArticleTitle);
            intro = view.findViewById(R.id.tvArticleIntro);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ItemArticleActivity.class);
                    intent.putExtra(Config.ARTICLE_INTENT, getAdapterPosition());
                    activity.startActivity(intent);
                }
            });
        }

        void bind(int position){

            String url = listItem.get(position).getImgUrl();

            Glide.with(context).load(url).into(imageView);
            title.setText(Html.fromHtml(listItem.get(position).getTitle()));
            intro.setText(Html.fromHtml(listItem.get(position).getIntroPart()));
        }

    }
}
