package com.losing.weight.BranchOfAnalyzer.CustomFood.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.losing.weight.BarcodeScanner.BaseScanner;
import com.losing.weight.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.losing.weight.BranchOfAnalyzer.CustomFood.CustomFood;
import com.losing.weight.BranchOfAnalyzer.CustomFood.SayForward;
import com.losing.weight.Config;
import com.losing.weight.R;

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
    private final static String TAG = "FragmentMainInfo";
    @BindView(R.id.tvTitleShare) TextView tvTitleShare;
    @BindView(R.id.tvDescriptionShare) TextView tvDescriptionShare;

    public static FragmentMainInfo newInstance(CustomFood customFood) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, customFood);
        FragmentMainInfo fragmentMainInfo = new FragmentMainInfo();
        fragmentMainInfo.setArguments(bundle);
        return fragmentMainInfo;
    }


    @Override
    public boolean forward() {
        if (!edtName.getText().toString().equals("")
                && !edtName.getText().toString().equals(" ")
                && !edtName.getText().toString().replaceAll("\\s+", " ").equals(" ")) {
            setInfo();
            return true;
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_name), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setInfo() {
        String brand = edtBrand.getText().toString().replaceAll("\\s+", " ").trim();
        String name = edtName.getText().toString().replaceAll("\\s+", " ").trim();
        CustomFood customFood = ((ActivityCreateFood) getActivity()).customFood;
        customFood.setBrand(brand);
        customFood.setName(name);
        customFood.setBarcode(edtBarcode.getText().toString());
        (((ActivityCreateFood) getActivity()).isPublicFood) = swtShare.isChecked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (((ActivityCreateFood) getActivity()).isEdit) {
            bindFields((CustomFood) getArguments().getSerializable(TAG));
        }
        return view;
    }

    private void bindFields(CustomFood customFood) {
        edtName.setText(customFood.getName());
        edtBrand.setText(customFood.getBrand());
        edtBarcode.setText(customFood.getBarcode());
        swtShare.setVisibility(View.GONE);
        tvTitleShare.setVisibility(View.GONE);
        tvDescriptionShare.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ibBarcode)
    public void onViewClicked() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            startActivityForResult(new Intent(getActivity(), BaseScanner.class), 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        edtBarcode.setText(data.getStringExtra(Config.BARCODE_STRING_NAME));
    }
}