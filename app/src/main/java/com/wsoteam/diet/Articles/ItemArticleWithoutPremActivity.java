package com.wsoteam.diet.Articles;

import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.Articles.Util.HtmlTagHandler;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.Events;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class ItemArticleWithoutPremActivity extends AppCompatActivity {

    @BindView(R.id.imgArticleWP) ImageView imgArticle;
    @BindView(R.id.tvTitleArticleWP) TextView tvTitle;
    @BindView(R.id.tvIntroArticleWP) TextView tvIntro;
    @BindView(R.id.tvMainArticleWP) TextView tvMain;
    @BindView(R.id.testID) LinearLayout layout;
    @BindView(R.id.toolbar) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_article_without_prem);
        ButterKnife.bind(this);



        int position = getIntent().getIntExtra(Config.ARTICLE_INTENT, 0);
        Article article = ArticlesHolder.getListArticles().getListArticles().get(position);

        setValue(article);

        tvMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        float radius = tvMain.getTextSize() / 3;
        BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
        tvMain.getPaint().setMaskFilter(filter);

        toolbar.setPadding(0, dpToPx(24), 0, 0);
        toolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Events.logViewArticle(article.getTitle());
//        tvMain.getPaint().setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
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

    @OnClick(R.id.goPremArticle)
    public void startPrem(){
        Box box = new Box();
        box.setSubscribe(false);
        box.setOpenFromPremPart(true);
        box.setOpenFromIntrodaction(false);
        box.setComeFrom(AmplitudaEvents.view_prem_content);
        box.setComeFrom(AmplitudaEvents.buy_prem_content);
        Intent intent = new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box);
        startActivity(intent);
    }

    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}
