package com.wsoteam.diet.presentation.measurment.history.fragment;

import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.measurment.history.fragment.controller.HistoryListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeasHistoryFragment extends MvpAppCompatFragment implements MeasHistoryView {
    MeasHistoryPresenter presenter;
    public static final String TYPE = "TYPE";
    @BindView(R.id.rvHistoryList) RecyclerView rvHistoryList;
    @BindView(R.id.ivEmptyState) ImageView ivEmptyState;
    @BindView(R.id.tvEmptyState) TextView tvEmptyState;
    private int type;
    Unbinder unbinder;
    HistoryListAdapter adapter;

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
        unbinder = ButterKnife.bind(this, view);
        presenter = new MeasHistoryPresenter(getActivity());
        presenter.attachView(this);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        type = getArguments().getInt(TYPE);
        presenter.getHistoryList(type);
        return view;
    }

    @Override
    public void updateUI(List<String> dates, List<Spannable> values) {
        if (dates.size() > 0) {
            adapter = new HistoryListAdapter(dates, values, getActivity());
            rvHistoryList.setAdapter(adapter);
        } else {
            showEmptyState();
        }
    }

    private void showEmptyState() {
        ivEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
