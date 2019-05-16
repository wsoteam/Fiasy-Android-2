package com.wsoteam.diet.Articles;


import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.Articles.Util.HtmlTagHandler;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemArticleActivity extends AppCompatActivity {

    @BindView(R.id.imgArticle) ImageView imgArticle;
    @BindView(R.id.tvTitleArticle) TextView tvTitle;
    @BindView(R.id.tvIntroArticle) TextView tvIntro;
    @BindView(R.id.tvMainArticle) TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_article);
        ButterKnife.bind(this);

        int position = getIntent().getIntExtra(Config.ARTICLE_INTENT, 0);
        Article article = ArticlesHolder.getListArticles().getListArticles().get(position);

        setValue(article);
    }

    private void setValue(Article article){

        Glide.with(this).load(article.getImgUrl()).into(imgArticle);

        tvTitle.setText(Html.fromHtml(article.getTitle()));
        tvIntro.setText(Html.fromHtml(article.getIntroPart()));

        HtmlTagHandler tagHandler = new HtmlTagHandler();
        Spanned styledText = HtmlCompat.fromHtml(article.getMainPart(),
                HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);
        tvMain.setText(styledText);

    }

}
