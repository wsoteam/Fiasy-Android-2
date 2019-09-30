package com.wsoteam.diet.presentation.measurment.history.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.R;

public class MeasHistoryFragment extends MvpAppCompatFragment implements MeasHistoryView {
    MeasHistoryPresenter presenter;
    public static final String TYPE = "TYPE";
    private int type;

    public static MeasHistoryFragment newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, position);
        MeasHistoryFragment measHistoryFragment = new MeasHistoryFragment();
        measHistoryFragment.setArguments(bundle);
        return measHistoryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meas_history, container, false);
        presenter = new MeasHistoryPresenter();
        presenter.attachView(this);

        type = getArguments().getInt(TYPE);
        presenter.getHistoryList(type);
        return view;
    }
}
