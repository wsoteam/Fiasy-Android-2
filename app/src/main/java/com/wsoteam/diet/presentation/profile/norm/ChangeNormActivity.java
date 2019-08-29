package com.wsoteam.diet.presentation.profile.norm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChangeNormActivity extends MvpAppCompatActivity implements ChangeNormView {

    @BindView(R.id.edtHeight) EditText edtHeight;
    @BindView(R.id.tilHeight) TextInputLayout tilHeight;
    @BindView(R.id.edtWeight) EditText edtWeight;
    @BindView(R.id.tilWeight) TextInputLayout tilWeight;
    @BindView(R.id.edtAge) EditText edtAge;
    @BindView(R.id.tilAge) TextInputLayout tilAge;
    private ChangeNormPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_norm);
        ButterKnife.bind(this);

        presenter = new ChangeNormPresenter(this);
        presenter.attachView(this);
    }

    @OnTextChanged(value = R.id.edtHeight, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void heightChanged(CharSequence text) {
        checkTextInputLayout(tilHeight);
    }

    @OnTextChanged(value = R.id.edtWeight, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void weightChanged(CharSequence text) {
        checkTextInputLayout(tilWeight);
    }

    @OnTextChanged(value = R.id.edtAge, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void ageChanged(CharSequence text) {
        checkTextInputLayout(tilAge);
    }

    private void checkTextInputLayout(TextInputLayout currentTextInputLayout) {
        if (currentTextInputLayout.getError() != null) {
            currentTextInputLayout.setError("");
        }
    }

    @OnClick({R.id.ibSave, R.id.ibBack, R.id.edtSex, R.id.edtActivity, R.id.edtGoal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSave:
                break;
            case R.id.ibBack:
                break;
            case R.id.edtSex:
                break;
            case R.id.edtActivity:
                break;
            case R.id.edtGoal:
                break;
        }
    }

    private boolean isNoError() {
        if (!edtAge.getText().toString().equals("")
                && Integer.parseInt(edtAge.getText().toString()) >= 9
                && Integer.parseInt(edtAge.getText().toString()) <= 200) {
            if (!edtHeight.getText().toString().equals("")
                    && Integer.parseInt(edtHeight.getText().toString()) >= 100
                    && Integer.parseInt(edtHeight.getText().toString()) <= 300) {
                if (!edtWeight.getText().toString().equals("")
                        && Double.parseDouble(edtWeight.getText().toString()) >= 30
                        && Double.parseDouble(edtWeight.getText().toString()) <= 300) {
                    return true;
                } else {
                    tilWeight.setError(getString(R.string.spk_check_weight));
                    return false;
                }
            } else {
                tilHeight.setError(getString(R.string.spk_check_your_height));
                return false;
            }
        } else {
            tilAge.setError(getString(R.string.spk_check_your_age));
            return false;
        }
    }
}
