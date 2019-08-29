package com.wsoteam.diet.presentation.profile.norm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.norm.choise.activity.ActivActivity;
import com.wsoteam.diet.presentation.profile.norm.choise.goal.GoalActivity;

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
    @BindView(R.id.edtSex) EditText edtSex;
    @BindView(R.id.edtActivity) EditText edtActivity;
    @BindView(R.id.edtGoal) EditText edtGoal;
    private ChangeNormPresenter presenter;

    @Override
    public void bindFields(Profile profile, String goal, String activity) {
        edtAge.setText(String.valueOf(profile.getAge()));
        edtWeight.setText(String.valueOf(profile.getWeight()));
        edtHeight.setText(String.valueOf(profile.getHeight()));
        if (profile.isFemale()){
            edtSex.setText(getResources().getString(R.string.profile_female));
        }else {
            getResources().getString(R.string.profile_male);
        }
        edtActivity.setText(activity);
        edtGoal.setText(goal);
    }

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
                if (isNoError()) {
                    if (presenter.calculateAndSave(edtHeight.getText().toString(), edtWeight.getText().toString(),
                            edtAge.getText().toString(), edtSex.getText().toString(), edtActivity.getText().toString(), edtGoal.getText().toString())) {
                        Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
            case R.id.ibBack:
                onBackPressed();
                break;
            case R.id.edtSex:
                break;
            case R.id.edtActivity:
                startActivity(new Intent(this, ActivActivity.class).putExtra(Config.ACTIVITY, edtActivity.getText().toString()));
                break;
            case R.id.edtGoal:
                startActivity(new Intent(this, GoalActivity.class).putExtra(Config.GOAL, edtGoal.getText().toString()));
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
