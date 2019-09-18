package com.wsoteam.diet.presentation.promo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class PromoFormActivity extends MvpAppCompatActivity implements PromoFormView {
    @BindView(R.id.btnSendPromo) Button btnSendPromo;
    @BindView(R.id.edtPromo) EditText edtPromo;
    @BindView(R.id.tilPromo) TextInputLayout tilPromo;
    private PromoFormPresenter presenter;
    private boolean isError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_form);
        ButterKnife.bind(this);

        presenter = new PromoFormPresenter(this);
        presenter.attachView(this);
        tilPromo.setError("Введите промокод");
        tilPromo.setErrorTextColor(getResources().getColorStateList(R.color.hint_promo));
    }

    @OnTextChanged(value = R.id.edtPromo, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void promoChanged(CharSequence text) {
        if (text.length() > 0){
            onSendMode();
        }else {
            offSendMode();
        }
        if (isError){
            hideError();
        }
    }

    private void onSendMode(){
        if (!btnSendPromo.isEnabled()) {
            btnSendPromo.setEnabled(true);
            btnSendPromo.setBackgroundTintList(getResources().getColorStateList(R.color.active_promo_btn));
        }
    }

    private void offSendMode(){
        if (btnSendPromo.isEnabled()) {
            btnSendPromo.setEnabled(false);
            btnSendPromo.setBackgroundTintList(getResources().getColorStateList(R.color.unactive_promo_btn));
        }
    }

    private void showError(){
        isError = true;
        edtPromo.setBackgroundColor(getResources().getColor(R.color.hint_error));
        tilPromo.setError("Промокод введен не верно");
        tilPromo.setErrorTextColor(getResources().getColorStateList(R.color.hint_error));
    }

    private void hideError(){
        isError = false;
        edtPromo.setBackgroundColor(getResources().getColor(R.color.hint_no_error_back));
        tilPromo.setError("Введите промокод");
        tilPromo.setErrorTextColor(getResources().getColorStateList(R.color.hint_promo));
    }

    @OnClick({R.id.ibBack, R.id.btnSendPromo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                onBackPressed();
                break;
            case R.id.btnSendPromo:
                showError();
                break;
        }
    }
}
