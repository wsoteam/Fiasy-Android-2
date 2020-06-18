package com.losing.weight.presentation.intro_tut;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.losing.weight.R;
import java.util.Arrays;
import java.util.List;

public class IntroSlidesAdapter extends FragmentPagerAdapter {
  private List<Slide> slides = Arrays.asList(
      new Slide(R.drawable.ic_intro_1, R.string.unboarding_first_screen),
      new Slide(R.drawable.ic_intro_2, R.string.unboarding_second_screen),
      new Slide(R.drawable.ic_intro_3, R.string.intro_text_3)
  );

  public IntroSlidesAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    return IntroSlideFragment.newInstance(getSlide(position));
  }

  @Override
  public int getCount() {
    return slides.size();
  }

  public Slide getSlide(int position) {
    return slides.get(position);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return "";
  }

  public static class Slide implements Parcelable {
    private final int imageResId;
    private final int titleResId;

    public Slide(int imageResId, int titleResId) {
      this.imageResId = imageResId;
      this.titleResId = titleResId;
    }

    public int image() {
      return imageResId;
    }

    public int title() {
      return titleResId;
    }

    protected Slide(Parcel in) {
      imageResId = in.readInt();
      titleResId = in.readInt();
    }

    public static final Creator<Slide> CREATOR = new Creator<Slide>() {
      @Override
      public Slide createFromParcel(Parcel in) {
        return new Slide(in);
      }

      @Override
      public Slide[] newArray(int size) {
        return new Slide[size];
      }
    };

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(imageResId);
      dest.writeInt(titleResId);
    }
  }
}