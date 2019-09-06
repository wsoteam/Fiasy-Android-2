package com.wsoteam.diet.BranchOfExercises.ActivitiesArticles;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.BranchOfExercises.ObjectHolder;
import com.wsoteam.diet.POJOSExercises.Article;
import com.wsoteam.diet.POJOSExercises.WholeArticle;
import com.wsoteam.diet.R;

import java.util.ArrayList;


public class ActivityDetailAcrticles extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    public static final String TAG = "ActivityDetailAcrticles";
    private WholeArticle wholeArticle;
    private TextView collapsingTitle;
    private ImageView collapsingImage;
    private RecyclerView recyclerView;
    private int selectedNumber = 0;
    private AdView banner;

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_detail_aticles);

        banner = findViewById(R.id.ex_bannerFromArticle);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.admob_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        banner.loadAd(adRequest);

        selectedNumber = getIntent().getIntExtra(TAG, 0);
        wholeArticle = ObjectHolder.getGlobalObject().getWholeArticles().getWholeArticleList().get(selectedNumber);

        collapsingTitle = findViewById(R.id.ex_tvCollapsingTitleOfArticle);
        collapsingImage = findViewById(R.id.ex_ivCollapsingImageArticle);
        collapsingTitle.setText(wholeArticle.getTitle());
        Glide.with(this).load(wholeArticle.getImg_url()).into(collapsingImage);

        recyclerView = findViewById(R.id.ex_rvArticle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PartOfArticleAdapter((ArrayList<Article>) wholeArticle.getArticleList()));

    }

    private class PartOfArticleVH extends RecyclerView.ViewHolder {
        private TextView textOfOneItem;
        private ImageView imageOfOneItem;

        public PartOfArticleVH(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.ex_item_activity_detail_arctiles_item, viewGroup, false));
            textOfOneItem = itemView.findViewById(R.id.ex_tvTextDetailOfArticle);
            imageOfOneItem = itemView.findViewById(R.id.ex_ivImageOfDetailArticle);
        }

        public void bind(Article article) {
            textOfOneItem.setText(Html.fromHtml(article.getText()));
            Glide.with(ActivityDetailAcrticles.this).load(article.getImg_url()).into(imageOfOneItem);
        }
    }

    private class PartOfArticleAdapter extends RecyclerView.Adapter<PartOfArticleVH> {
        ArrayList<Article> articles;

        public PartOfArticleAdapter(ArrayList<Article> articles) {
            this.articles = articles;
        }

        @NonNull
        @Override
        public PartOfArticleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityDetailAcrticles.this);
            return new PartOfArticleVH(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PartOfArticleVH holder, int position) {
            holder.bind(articles.get(position));
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }
    }
}
