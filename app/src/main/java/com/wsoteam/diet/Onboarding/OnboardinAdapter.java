package com.wsoteam.diet.Onboarding;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OnboardinAdapter extends PagerAdapter {


    private List<View> viewList = null;

    public OnboardinAdapter(List<View> list){
        this.viewList = list;

    }

    @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {


        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
