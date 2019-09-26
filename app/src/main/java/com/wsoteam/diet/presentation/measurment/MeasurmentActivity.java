package com.wsoteam.diet.presentation.measurment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.measurment.days.DaysFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeasurmentActivity extends MvpAppCompatActivity implements MeasurmentView {
    @InjectPresenter
    MeasurmentPresenter measurmentPresenter;
    @BindView(R.id.vpDays) ViewPager vpDays;
    private final int SIZE_DATE_LINE = 4001;
    private final int MEDIUM_DATE_LINE = 2001;
    @BindView(R.id.tvMediumWeight) TextView tvMediumWeight;
    @BindView(R.id.tvChestValue) TextView tvChestValue;
    @BindView(R.id.tvWaistValue) TextView tvWaistValue;
    @BindView(R.id.tvHipsValue) TextView tvHipsValue;
    @BindView(R.id.btnPremChest) Button btnPremChest;
    @BindView(R.id.btnPremWaist) Button btnPremWaist;
    @BindView(R.id.btnPremHips) Button btnPremHips;
    private int position = 0;

    private ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measurment_activity);
        ButterKnife.bind(this);
        bindViewPager();
        setUIAccordingPrem();

    }

    private void setUIAccordingPrem() {
        if(isPremiumUser()){
            turnOnUIPremMode();
        }else {
            turnOffUIPremMode();
        }
    }

    private void turnOffUIPremMode() {
        btnPremChest.setVisibility(View.VISIBLE);
        btnPremWaist.setVisibility(View.VISIBLE);
        btnPremHips.setVisibility(View.VISIBLE);
    }

    private void turnOnUIPremMode() {
        btnPremChest.setVisibility(View.INVISIBLE);
        btnPremWaist.setVisibility(View.INVISIBLE);
        btnPremHips.setVisibility(View.INVISIBLE);
    }

    private boolean isPremiumUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.STATE_BILLING, false);
    }


    private void bindViewPager() {
        vpDays.addOnPageChangeListener(viewPagerListener);
        vpDays.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return DaysFragment.newInstance(position - MEDIUM_DATE_LINE);
            }

            @Override
            public int getCount() {
                return SIZE_DATE_LINE;
            }
        });
        vpDays.setCurrentItem(MEDIUM_DATE_LINE);
    }


    @OnClick({R.id.ibGraphs, R.id.ibBack, R.id.tvMediumWeight, R.id.imbtnLeft, R.id.imbtnRight, R.id.tvChestValue, R.id.tvWaistValue, R.id.tvHipsValue, R.id.btnPremChest, R.id.btnPremWaist, R.id.btnPremHips})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibGraphs:
                break;
            case R.id.ibBack:
                break;
            case R.id.tvMediumWeight:
                break;
            case R.id.imbtnLeft:
                vpDays.setCurrentItem(vpDays.getCurrentItem() - 1, true);
                break;
            case R.id.imbtnRight:
                vpDays.setCurrentItem(vpDays.getCurrentItem() + 1, true);
                break;
            case R.id.tvChestValue:
                break;
            case R.id.tvWaistValue:
                break;
            case R.id.tvHipsValue:
                break;
            case R.id.btnPremChest:
            case R.id.btnPremHips:
            case R.id.btnPremWaist:
                openPremScreen();
                break;
        }
    }

    private void openPremScreen() {
        Box box = new Box();
        box.setOpenFromIntrodaction(false);
        box.setOpenFromPremPart(true);
        startActivity(new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box));
    }
}
