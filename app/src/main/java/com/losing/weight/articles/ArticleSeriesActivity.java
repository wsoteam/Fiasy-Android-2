package com.losing.weight.articles;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.losing.weight.R;

public class ArticleSeriesActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_container_article);
    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ArticleSeriesFragment()).commit();
    findViewById(R.id.imageView62).setOnClickListener(v -> onBackPressed());
  }
}
