package com.wsoteam.diet.presentation.intro_tut;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewIntroActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tabDots)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_intro);
        ButterKnife.bind(this);

        viewPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager, true);

    }

    @OnClick({R.id.btnNext, R.id.btnSkip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (viewPager.getCurrentItem() <= viewPager.getChildCount() - 1)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                else
                    startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
                break;
            case R.id.btnSkip:
                startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
                break;
        }
    }
}
