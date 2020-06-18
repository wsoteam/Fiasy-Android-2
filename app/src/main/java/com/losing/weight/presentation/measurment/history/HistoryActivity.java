package com.losing.weight.presentation.measurment.history;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.losing.weight.R;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle;
import com.losing.weight.ads.nativetemplates.TemplateView;
import com.losing.weight.presentation.measurment.history.fragment.MeasHistoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends AppCompatActivity {
    @BindView(R.id.vpHistory) ViewPager vpHistory;
    List<Fragment> fragments;
    @BindView(R.id.rgrpType) RadioGroup rgrpType;
    @BindView(R.id.nativeAd)
    TemplateView nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        createFragmentList();
        bindPagerAndTopTapper();

        FiasyAds.getLiveDataAdView().observe(this, ad -> {
            if (ad != null) {
                nativeAd.setVisibility(View.VISIBLE);
                nativeAd.setStyles(new NativeTemplateStyle.Builder().build());
                nativeAd.setNativeAd(ad);
            }else {
                nativeAd.setVisibility(View.GONE);
            }
        });
    }

    private void createFragmentList() {
        fragments = new ArrayList<>();
        fragments.add(MeasHistoryFragment.newInstance(Config.MEAS_WEIGHT));
        fragments.add(MeasHistoryFragment.newInstance(Config.MEAS_WAIST));
        fragments.add(MeasHistoryFragment.newInstance(Config.MEAS_CHEST));
        fragments.add(MeasHistoryFragment.newInstance(Config.MEAS_HIPS));
    }

    @OnClick({R.id.rbtnWeight, R.id.rbtnWaist, R.id.rbtnChest, R.id.rbtnHips})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rbtnWeight:
                if (checked) {
                    vpHistory.setCurrentItem(Config.MEAS_WEIGHT, true);
                }
                break;
            case R.id.rbtnWaist:
                if (checked) {
                    vpHistory.setCurrentItem(Config.MEAS_WAIST, true);
                }
                break;
            case R.id.rbtnChest:
                if (checked) {
                    vpHistory.setCurrentItem(Config.MEAS_CHEST, true);
                }
                break;
            case R.id.rbtnHips:
                if (checked) {
                    vpHistory.setCurrentItem(Config.MEAS_HIPS, true);
                }
                break;
        }
    }

    private void bindPagerAndTopTapper() {
        vpHistory.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        vpHistory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int id = 0;
                switch (position) {
                    case Config.MEAS_WEIGHT:
                        id = R.id.rbtnWeight;
                        break;
                    case Config.MEAS_WAIST:
                        id = R.id.rbtnWaist;
                        break;
                    case Config.MEAS_CHEST:
                        id = R.id.rbtnChest;
                        break;
                    case Config.MEAS_HIPS:
                        id = R.id.rbtnHips;
                        break;
                }
                rgrpType.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.ibBack)
    public void onViewClicked() {
        onBackPressed();
    }
}
