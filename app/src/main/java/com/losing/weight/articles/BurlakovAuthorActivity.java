package com.losing.weight.articles;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.losing.weight.R;

public class BurlakovAuthorActivity extends AppCompatActivity {

  public static final String HIDE_BTN = "BurlakovAuthorActivityHideBtn";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_container_article);

    boolean hideButon = getIntent().getBooleanExtra(HIDE_BTN, false);
    Bundle bundle = new Bundle();
    bundle.putBoolean(HIDE_BTN, hideButon);
    BurlakovAuthorFragment burlakovAuthorFragment = new BurlakovAuthorFragment();
    burlakovAuthorFragment.setArguments(bundle);

    getSupportFragmentManager().beginTransaction().replace(R.id.container, burlakovAuthorFragment).commit();
    findViewById(R.id.imageView62).setOnClickListener(v -> onBackPressed());
  }
}
