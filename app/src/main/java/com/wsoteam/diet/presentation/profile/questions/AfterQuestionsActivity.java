package com.wsoteam.diet.presentation.profile.questions;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;

public class AfterQuestionsActivity extends AppCompatActivity {

  @BindView(R.id.pager) ViewPager viewPager;

  Profile profile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_after_questions);
    ButterKnife.bind(this);


    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    getWindow().setStatusBarColor(Color.parseColor("#382c2c2d"));

    //viewPager.onInterceptTouchEvent(null);
    //viewPager.onTouchEvent(null);
    viewPager.setAdapter(new AfterQuestionsPagerAdapter(getSupportFragmentManager()));

  }

  public void nextQuestion() {
    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
  }

  public void prevQuestion() {
    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
  }
}
