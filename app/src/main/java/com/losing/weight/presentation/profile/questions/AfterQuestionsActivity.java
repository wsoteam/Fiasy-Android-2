package com.losing.weight.presentation.profile.questions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.EntryPoint.ActivitySplash;
import com.losing.weight.POJOProfile.Profile;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AfterQuestionsActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    CustomViewPager viewPager;
    Profile profile;
    private final int CALCULATE = 0, FEATURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_questions);
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#382c2c2d"));

        //final View touchView = findViewById(R.id.pager);
        //touchView.setOnTouchListener(new View.OnTouchListener() {
        //  @Override
        //  public boolean onTouch(View v, MotionEvent event) {
        //    return true;
        //  }
        //});
        //viewPager.onInterceptTouchEvent(null);
        //viewPager.onTouchEvent(null);
        viewPager.disableScroll(true);
        viewPager.setAdapter(new AfterQuestionsPagerAdapter(getSupportFragmentManager()));
        Events.logMoveQuestions(EventProperties.question_calculate);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1) {
                    //Events.logMoveQuestions(EventProperties.question_feature);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }


    public void nextQuestion() {
        Box box = new Box();
        box.setOpenFromIntrodaction(true);
        box.setOpenFromPremPart(false);
        box.setBuyFrom(EventProperties.trial_from_onboard);
        box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
        startActivity(new Intent(this, ActivitySplash.class));
    }


    public void prevQuestion() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}
