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

public class FragmentOutlay extends Fragment implements SayForward {

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
    @BindView(R.id.edtProt) EditText edtProt;

    @Override
    public boolean forward() {
        if (isCanForward()){
            setInfo();
            return true;
        }else {
            Toast.makeText(getActivity(), getString(R.string.error_toast), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setInfo() {
        CustomFood customFood = ((ActivityCreateFood) getActivity()).customFood;
        customFood.setCalories(Double.parseDouble(edtKcal.getText().toString()));
        customFood.setFats(Double.parseDouble(edtFats.getText().toString()));
        customFood.setProteins(Double.parseDouble(edtProt.getText().toString()));
        customFood.setCarbohydrates(Double.parseDouble(edtCarbo.getText().toString()));

        if (!edtCell.getText().toString().equals("") && !edtCell.getText().toString().equals(".")){
            customFood.setCellulose(Double.parseDouble(edtCell.getText().toString()));
        }else {
            customFood.setCellulose(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtSugar.getText().toString().equals("") && !edtSugar.getText().toString().equals(".")){
            customFood.setSugar(Double.parseDouble(edtSugar.getText().toString()));
        }else {
            customFood.setSugar(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtSaturatedFats.getText().toString().equals("") && !edtSaturatedFats.getText().toString().equals(".")){
            customFood.setSaturatedFats(Double.parseDouble(edtSaturatedFats.getText().toString()));
        }else {
            customFood.setSaturatedFats(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtMonoUnSaturatedFats.getText().toString().equals("") && !edtMonoUnSaturatedFats.getText().toString().equals(".")){
            customFood.setMonoUnSaturatedFats(Double.parseDouble(edtMonoUnSaturatedFats.getText().toString()));
        }else {
            customFood.setMonoUnSaturatedFats(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtPolyUnSaturatedFats.getText().toString().equals("") && !edtPolyUnSaturatedFats.getText().toString().equals(".")){
            customFood.setPolyUnSaturatedFats(Double.parseDouble(edtPolyUnSaturatedFats.getText().toString()));
        }else {
            customFood.setPolyUnSaturatedFats(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtCholesterol.getText().toString().equals("") && !edtCholesterol.getText().toString().equals(".")){
            customFood.setCholesterol(Double.parseDouble(edtCholesterol.getText().toString()));
        }else {
            customFood.setCholesterol(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtSodium.getText().toString().equals("") && !edtSodium.getText().toString().equals(".")){
            customFood.setSodium(Double.parseDouble(edtSodium.getText().toString()));
        }else {
            customFood.setSodium(Double.parseDouble(getString(R.string.empty_field_food)));
        }

        if (!edtPottassium.getText().toString().equals("") && !edtPottassium.getText().toString().equals(".")){
            customFood.setPottassium(Double.parseDouble(edtPottassium.getText().toString()));
        }else {
            customFood.setPottassium(Double.parseDouble(getString(R.string.empty_field_food)));
        }
    }

    private boolean isCanForward() {
        if (!edtKcal.getText().toString().equals("")
                && !edtFats.getText().toString().equals("")
                && !edtCarbo.getText().toString().equals("")
                && !edtProt.getText().toString().equals("")
                && !edtKcal.getText().toString().equals(".")
                && !edtFats.getText().toString().equals(".")
                && !edtCarbo.getText().toString().equals(".")
                && !edtProt.getText().toString().equals(".")){
            return true;
        }else {
            return false;
        }
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
