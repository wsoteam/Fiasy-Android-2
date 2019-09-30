package com.wsoteam.diet.presentation.measurment.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.measurment.history.fragment.MeasHistoryFragment;
import com.wsoteam.diet.presentation.measurment.history.fragment.MeasHistoryPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {
    @BindView(R.id.vpHistory) ViewPager vpHistory;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        createFragmentList();
        vpHistory.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return 4;
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
}
