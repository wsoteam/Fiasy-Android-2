package com.wsoteam.diet.Articles;

import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.Articles.Util.HtmlTagHandler;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


public class ItemArticleWithoutPremActivity extends AppCompatActivity {

    @BindView(R.id.imgArticleWP) ImageView imgArticle;
    @BindView(R.id.tvTitleArticleWP) TextView tvTitle;
    @BindView(R.id.tvIntroArticleWP) TextView tvIntro;
    @BindView(R.id.tvMainArticleWP) TextView tvMain;
    @BindView(R.id.testID) LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_article_without_prem);
        ButterKnife.bind(this);


        int position = getIntent().getIntExtra(Config.ARTICLE_INTENT, 0);
        Article article = ArticlesHolder.getListArticles().getListArticles().get(position);

        setValue(article);

        float radius = 20;

        View decorView = getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        Drawable windowBackground = decorView.getBackground();

        BlurView blurView = findViewById(R.id.blurView);
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(this))
                .blurRadius(radius)
                .setHasFixedTransformationMatrix(true);

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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        blurLayout.startBlur();
    }

    @Override
    protected void onStart() {
//        blurLayout.pauseBlur();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick(R.id.goPremArticle)
    public void startPrem(){
        startActivity(new Intent(this, ActivitySubscription.class));
    }

}
