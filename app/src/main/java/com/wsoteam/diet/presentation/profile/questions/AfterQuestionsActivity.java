package com.wsoteam.diet.presentation.profile.questions;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;

public class AfterQuestionsActivity extends AppCompatActivity {

  @BindView(R.id.pager) ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_after_questions);
    ButterKnife.bind(this);

    viewPager.setAdapter(new AfterQuestionsPagerAdapter(getSupportFragmentManager()));
  }
}
