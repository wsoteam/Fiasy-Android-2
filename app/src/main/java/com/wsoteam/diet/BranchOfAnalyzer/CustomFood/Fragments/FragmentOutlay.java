package com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.CustomFood;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentOutlay extends Fragment {

    @BindView(R.id.edtKcal) EditText edtKcal;
    @BindView(R.id.edtFats) EditText edtFats;
    @BindView(R.id.edtCarbo) EditText edtCarbo;
    @BindView(R.id.edtCell) EditText edtCell;
    @BindView(R.id.edtSugar) EditText edtSugar;
    @BindView(R.id.edtSaturatedFats) EditText edtSaturatedFats;
    @BindView(R.id.edtMonoUnSaturatedFats) EditText edtMonoUnSaturatedFats;
    @BindView(R.id.edtPolyUnSaturatedFats) EditText edtPolyUnSaturatedFats;
    @BindView(R.id.edtCholesterol) EditText edtCholesterol;
    @BindView(R.id.edtSodium) EditText edtSodium;
    @BindView(R.id.edtPottassium) EditText edtPottassium;
    Unbinder unbinder;
    private static final String TAG = "FragmentOutlay";

    public static FragmentOutlay newInstance(CustomFood customFood) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, customFood);
        FragmentOutlay fragmentOutlay = new FragmentOutlay();
        fragmentOutlay.setArguments(bundle);
        return fragmentOutlay;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outlay, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
