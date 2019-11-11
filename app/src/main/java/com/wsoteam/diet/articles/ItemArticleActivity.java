package com.wsoteam.diet.articles;


import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.Events;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.model.ApiResult;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.model.ArticleViewModel;
import java.util.Locale;

public class ItemArticleActivity extends AppCompatActivity {

    @BindView(R.id.imgArticle) ImageView imgArticle;
    @BindView(R.id.tvTitleArticle) TextView tvTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.author) ConstraintLayout authorLabel;
    @BindView(R.id.webV) WebView webView;
    @BindView(R.id.progBar) ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_article);
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

        toolbar.setPadding(0, dpToPx(24), 0, 0);
        toolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        authorLabel.setOnClickListener(v ->
            startActivity(new Intent(this, BurlakovAuthorActivity.class)
                .putExtra(BurlakovAuthorActivity.HIDE_BTN, true)));

    }

    private void setValue(Article article){
        progressBar.setVisibility(View.VISIBLE);
        authorLabel.setVisibility(article.getCategory().getId() == 4 ? View.VISIBLE : View.GONE);

        Glide.with(this).load(article.getImage()).into(imgArticle);

        tvTitle.setText(article.getTitle(Locale.getDefault()).replaceAll("\\<.*?\\>", ""));
        //tvTitle.setText();



        //HtmlTagHandler tagHandler = new HtmlTagHandler();
        //Spanned styledText = HtmlCompat.fromHtml(article.getBody(Locale.getDefault()),
        //        HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);


        webView.setWebChromeClient(new WebChromeClient(){
            @Override public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadData(article.getBody(Locale.getDefault()), "text/html; charset=utf-8", "UTF-8");
    }

    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}
