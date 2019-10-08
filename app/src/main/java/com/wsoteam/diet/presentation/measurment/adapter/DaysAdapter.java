package com.wsoteam.diet.presentation.measurment.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.wsoteam.diet.presentation.measurment.days.DaysFragment;
import java.util.List;

public class DaysAdapter extends FragmentStatePagerAdapter {
  private final int REFRESH_TIME_LIMIT = 7;
  private final int SIZE_DATE_LINE = 4001;
  private final int MEDIUM_DATE_LINE = 2001;


  public DaysAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    return DaysFragment.newInstance(i - MEDIUM_DATE_LINE);
  }

  @Override
  public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    super.setPrimaryItem(container, position, object);
  }

  @Override
  public int getCount() {
    return SIZE_DATE_LINE;
  }
}
