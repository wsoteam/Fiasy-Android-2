package com.wsoteam.diet.presentation.measurment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.Fragments.FragmentEatingScroll;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;
import com.wsoteam.diet.presentation.measurment.days.DaysFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurmentActivity extends MvpAppCompatActivity implements MeasurmentView {
    @InjectPresenter
    MeasurmentPresenter measurmentPresenter;
    @BindView(R.id.vpDays) ViewPager vpDays;
    private final int SIZE_DATE_LINE = 4001;
    private final int MEDIUM_DATE_LINE = 2001;
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
}
