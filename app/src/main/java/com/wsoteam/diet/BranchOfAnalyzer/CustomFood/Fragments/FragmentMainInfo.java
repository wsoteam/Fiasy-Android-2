package com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.CustomFood;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.SayForward;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentMainInfo extends Fragment implements SayForward {

    @BindView(R.id.edtBrand) EditText edtBrand;
    @BindView(R.id.edtName) EditText edtName;
    @BindView(R.id.edtBarcode) EditText edtBarcode;
    Unbinder unbinder;
    @BindView(R.id.swtShare) Switch swtShare;


    @Override
    public boolean forward() {
        if (!edtName.getText().toString().equals("")
                && !edtName.getText().toString().equals(" ")
                && !edtName.getText().toString().replaceAll("\\s+", " ").equals(" ")) {
            setInfo();
            return true;
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_toast), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setInfo() {
        CustomFood customFood = ((ActivityCreateFood) getActivity()).customFood;
        customFood.setBrand(edtBrand.getText().toString());
        customFood.setName(edtName.getText().toString());
        customFood.setBarcode(edtBarcode.getText().toString());
        (((ActivityCreateFood) getActivity()).isPublicFood) = swtShare.isChecked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ibBarcode)
    public void onViewClicked() {
    }
}
