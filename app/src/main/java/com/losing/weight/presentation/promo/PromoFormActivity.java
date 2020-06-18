package com.losing.weight.presentation.promo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.losing.weight.R;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle;
import com.losing.weight.ads.nativetemplates.TemplateView;
import com.losing.weight.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class PromoFormActivity extends MvpAppCompatActivity implements PromoFormView {
    @BindView(R.id.btnSendPromo) Button btnSendPromo;
    @BindView(R.id.edtPromo) EditText edtPromo;
    @BindView(R.id.tilPromo) TextInputLayout tilPromo;
    @BindView(R.id.nativeAd) TemplateView nativeAd;
    private PromoFormPresenter presenter;
    private boolean isError = false;

    @Override
    public void resultCheckedPromo(boolean isSuccess) {
        if (isSuccess){
            showSuccesDialog();
        }else {
            showError();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_form);
        ButterKnife.bind(this);

        presenter = new PromoFormPresenter(this);
        presenter.attachView(this);
        tilPromo.setError(getString(R.string.enter_your_promotional_code));
        tilPromo.setErrorTextColor(getResources().getColorStateList(R.color.hint_promo));

        FiasyAds.getLiveDataAdView().observe(this, ad -> {
            if (ad != null) {
                nativeAd.setVisibility(View.VISIBLE);
                nativeAd.setStyles(new NativeTemplateStyle.Builder().build());
                nativeAd.setNativeAd(ad);
            }else {
                nativeAd.setVisibility(View.GONE);
            }
        });
    }

    @OnTextChanged(value = R.id.edtPromo, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void promoChanged(CharSequence text) {
        if (text.length() > 0) {
            onSendMode();
        } else {
            offSendMode();
        }
        if (isError) {
            hideError();
        }
    }

    private void onSendMode() {
        if (!btnSendPromo.isEnabled()) {
            btnSendPromo.setEnabled(true);
            btnSendPromo.setBackgroundTintList(getResources().getColorStateList(R.color.active_promo_btn));
        }
    }

    private void offSendMode() {
        if (btnSendPromo.isEnabled()) {
            btnSendPromo.setEnabled(false);
            btnSendPromo.setBackgroundTintList(getResources().getColorStateList(R.color.unactive_promo_btn));
        }
    }

    private void showError() {
        isError = true;
        tilPromo.setErrorEnabled(true);
        tilPromo.setErrorTextColor(getResources().getColorStateList(R.color.hint_error));
        tilPromo.setError(getString(R.string.invalid_promotional_code_error));
    }

    private void hideError() {
        isError = false;
        tilPromo.setErrorTextColor(getResources().getColorStateList(R.color.hint_promo));
        tilPromo.setError(getString(R.string.enter_your_promotional_code));
    }

    @OnClick({R.id.ibBack, R.id.btnSendPromo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                onBackPressed();
                break;
            case R.id.btnSendPromo:
                presenter.checkPromo(edtPromo.getText().toString());
                break;
        }
    }

    private void showSuccesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_promo_success, null);
        Button btnOk = view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openMainActivity(PromoFormActivity.this);
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }
}
