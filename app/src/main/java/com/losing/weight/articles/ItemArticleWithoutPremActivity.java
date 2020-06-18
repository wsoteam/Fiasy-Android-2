package com.losing.weight.articles;

import android.content.Intent;
import android.graphics.BlurMaskFilter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;
import com.losing.weight.AmplitudaEvents;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle;
import com.losing.weight.ads.nativetemplates.TemplateView;
import com.losing.weight.articles.Util.HtmlTagHandler;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losing.weight.model.ApiResult;
import com.losing.weight.model.Article;
import com.losing.weight.model.ArticleViewModel;

import java.util.Locale;

public class ItemArticleWithoutPremActivity extends AppCompatActivity {

    @BindView(R.id.imgArticleWP) ImageView imgArticle;
    @BindView(R.id.tvTitleArticleWP) TextView tvTitle;
    @BindView(R.id.tvMainArticleWP) TextView tvMain;
    @BindView(R.id.testID) LinearLayout layout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.author) ConstraintLayout authorLabel;
    @BindView(R.id.my_template) TemplateView nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_article_without_prem);
        ButterKnife.bind(this);

        int articleId = getIntent().getIntExtra(Config.ARTICLE_INTENT, 0);

        ArticleViewModel model = ViewModelProviders.of(this).get(ArticleViewModel.class);
        LiveData<ApiResult<Article>> data = model.getData();
        data.observe(this,
            new androidx.lifecycle.Observer<ApiResult<Article>>() {
                @Override
                public void onChanged(ApiResult<Article> articleApiResult) {
                    for (Article article: articleApiResult.getResults()) {
                        if (article.getId() == articleId) {
                            setValue(article);
                            Events.logViewArticle(article.getTitle());
                        }
                    }
                }
            });



        tvMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        float radius = tvMain.getTextSize() / 3;
        BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
        tvMain.getPaint().setMaskFilter(filter);

        toolbar.setPadding(0, dpToPx(24), 0, 0);
        toolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        authorLabel.setOnClickListener(v ->
                startActivity(new Intent(this, BurlakovAuthorActivity.class)
                        .putExtra(BurlakovAuthorActivity.HIDE_BTN, true)));

        FiasyAds.getLiveDataAdView().observe(this, ad -> {
            if (ad != null) {
                nativeAd.setVisibility(View.VISIBLE);
                nativeAd.setStyles(new NativeTemplateStyle.Builder().build());
                nativeAd.setNativeAd(ad);

            } else{
                nativeAd.setVisibility(View.GONE);
            }
        });
    }

    private void setValue(Article article){
        Picasso.get().load(article.getImage()).fit().centerCrop().into(imgArticle);

        tvTitle.setText(article.getTitle(Locale.getDefault()).replaceAll("\\<.*?\\>", ""));
        if (article.getCategory().getId() == 4) {
            authorLabel.setVisibility(View.VISIBLE);
        } else {
            authorLabel.setVisibility(View.GONE);
        }

        HtmlTagHandler tagHandler = new HtmlTagHandler();
        Spanned styledText = HtmlCompat.fromHtml(article.getBody(Locale.getDefault()),
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
        box.setBuyFrom(EventProperties.trial_from_articles);
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
