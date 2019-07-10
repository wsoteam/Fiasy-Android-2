package com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.CustomFood;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.SayForward;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentBonusOutlay extends Fragment implements SayForward {
    @BindView(R.id.edtCell) EditText edtCell;
    @BindView(R.id.edtSugar) EditText edtSugar;
    @BindView(R.id.edtSaturatedFats) EditText edtSaturatedFats;
    @BindView(R.id.edtMonoUnSaturatedFats) EditText edtMonoUnSaturatedFats;
    @BindView(R.id.edtPolyUnSaturatedFats) EditText edtPolyUnSaturatedFats;
    @BindView(R.id.edtCholesterol) EditText edtCholesterol;
    @BindView(R.id.edtSodium) EditText edtSodium;
    @BindView(R.id.edtPottassium) EditText edtPottassium;
    Unbinder unbinder;
    private final double EMPTY_PARAM = -1.0;

    @Override
    public boolean forward() {
        setInfo();
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bonus_outlay, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void setInfo() {
        CustomFood customFood = ((ActivityCreateFood) getActivity()).customFood;

        if (!edtCell.getText().toString().equals("") && !edtCell.getText().toString().equals(".")) {
            customFood.setCellulose(Double.parseDouble(edtCell.getText().toString()));
        } else {
            customFood.setCellulose(EMPTY_PARAM);
        }

        if (!edtSugar.getText().toString().equals("") && !edtSugar.getText().toString().equals(".")) {
            customFood.setSugar(Double.parseDouble(edtSugar.getText().toString()));
        } else {
            customFood.setSugar(EMPTY_PARAM);
        }

        if (!edtSaturatedFats.getText().toString().equals("") && !edtSaturatedFats.getText().toString().equals(".")) {
            customFood.setSaturatedFats(Double.parseDouble(edtSaturatedFats.getText().toString()));
        } else {
            customFood.setSaturatedFats(EMPTY_PARAM);
        }

        if (!edtMonoUnSaturatedFats.getText().toString().equals("") && !edtMonoUnSaturatedFats.getText().toString().equals(".")) {
            customFood.setMonoUnSaturatedFats(Double.parseDouble(edtMonoUnSaturatedFats.getText().toString()));
        } else {
            customFood.setMonoUnSaturatedFats(EMPTY_PARAM);
        }

        if (!edtPolyUnSaturatedFats.getText().toString().equals("") && !edtPolyUnSaturatedFats.getText().toString().equals(".")) {
            customFood.setPolyUnSaturatedFats(Double.parseDouble(edtPolyUnSaturatedFats.getText().toString()));
        } else {
            customFood.setPolyUnSaturatedFats(EMPTY_PARAM);
        }

        if (!edtCholesterol.getText().toString().equals("") && !edtCholesterol.getText().toString().equals(".")) {
            customFood.setCholesterol(Double.parseDouble(edtCholesterol.getText().toString()));
        } else {
            customFood.setCholesterol(EMPTY_PARAM);
        }

        if (!edtSodium.getText().toString().equals("") && !edtSodium.getText().toString().equals(".")) {
            customFood.setSodium(Double.parseDouble(edtSodium.getText().toString()));
        } else {
            customFood.setSodium(EMPTY_PARAM);
        }

        if (!edtPottassium.getText().toString().equals("") && !edtPottassium.getText().toString().equals(".")) {
            customFood.setPottassium(Double.parseDouble(edtPottassium.getText().toString()));
        } else {
            customFood.setPottassium(EMPTY_PARAM);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
