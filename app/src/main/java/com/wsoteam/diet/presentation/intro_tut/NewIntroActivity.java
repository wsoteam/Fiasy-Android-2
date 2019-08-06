package com.wsoteam.diet.presentation.intro_tut;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.auth.MainAuthNewActivity;
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
                    //startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
                {
                    Intent intent = new Intent(this, MainAuthNewActivity.class);
                    Box box = new Box();
                    box.setBuyFrom(AmplitudaEvents.buy_prem_onboarding);
                    box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
                    box.setOpenFromIntrodaction(true);
                    box.setOpenFromPremPart(false);
                    intent.putExtra(Config.CREATE_PROFILE, true)
                        .putExtra(Config.INTENT_PROFILE, new Profile());
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.btnSkip:
                //startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
            {
                Intent intent = new Intent(this, MainAuthNewActivity.class);
                Box box = new Box();
                box.setBuyFrom(AmplitudaEvents.buy_prem_onboarding);
                box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
                box.setOpenFromIntrodaction(true);
                box.setOpenFromPremPart(false);
                intent.putExtra(Config.CREATE_PROFILE, true)
                    .putExtra(Config.INTENT_PROFILE, new Profile());
                startActivity(intent);
                finish();
            }
                break;
        }
    }
}
