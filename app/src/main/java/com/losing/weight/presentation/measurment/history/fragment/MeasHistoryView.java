package com.losing.weight.presentation.measurment.history.fragment;

import android.text.Spannable;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface MeasHistoryView extends MvpView {
    void updateUI(List<String> dates, List<Spannable> values);
}
