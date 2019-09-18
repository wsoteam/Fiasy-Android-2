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

public class PromoFormActivity extends MvpAppCompatActivity implements PromoFormView {
    @BindView(R.id.btnSendPromo) Button btnSendPromo;
    @BindView(R.id.edtPromo) EditText edtPromo;
    @BindView(R.id.tilPromo) TextInputLayout tilPromo;
    private PromoFormPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_form);
        ButterKnife.bind(this);

        presenter = new PromoFormPresenter(this);
        presenter.attachView(this);
    }

    @OnClick({R.id.ibBack, R.id.btnSendPromo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                break;
            case R.id.btnSendPromo:
                break;
        }
    }
}
