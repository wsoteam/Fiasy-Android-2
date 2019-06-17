package com.wsoteam.diet.presentation.profile.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.SportActivity;
import com.wsoteam.diet.presentation.global.BaseActivity;
import com.wsoteam.diet.presentation.profile.dialogs.ActiveSportDialogFragment;
import com.wsoteam.diet.presentation.profile.dialogs.DifLevelDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class EditProfileActivity extends BaseActivity implements EditProfileView, DifLevelDialogFragment.OnDifLevelClickListener, ActiveSportDialogFragment.OnSportActivityClickListener {

    private static final String TAG = "EditProfile";
    @Inject
    @InjectPresenter
    EditProfilePresenter presenter;

    @BindView(R.id.edtSpkGrowth) EditText edtHeight;
    @BindView(R.id.edtSpkWeight) EditText edtWeight;
    @BindView(R.id.edtSpkAge) EditText edtAge;
    @BindView(R.id.btnSpkChoiseDif) Button btnDifLevel;
    @BindView(R.id.btnSpkChoiseLevel) Button btnChoiseLevel;
    @BindView(R.id.rgFemaleOrMaleSpk) RadioGroup rgFemaleOrMale;
    private SportActivity sportActivity = SportActivity.NONE;
    private boolean isNeedShowOnboard = false;

    @ProvidePresenter
    EditProfilePresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        if (getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).getBoolean(Config.IS_NEED_SHOW_ONBOARD, false)) {
            isNeedShowOnboard = true;
            getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).edit().putBoolean(Config.IS_NEED_SHOW_ONBOARD, false).apply();
        }

        onEasyClicked();
    }

    @OnClick({R.id.btnNext, R.id.btnSpkChoiseLevel, R.id.btnSpkChoiseDif, R.id.ivHelpEditProfile})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                presenter.calculate(rgFemaleOrMale.getCheckedRadioButtonId(), edtAge.getText().toString(),
                        edtHeight.getText().toString(), edtWeight.getText().toString(),
                        btnChoiseLevel.getText().toString(), btnDifLevel.getText().toString());
                break;
            case R.id.btnSpkChoiseLevel:
                createAlertDialogLevelLoad();
                break;
            case R.id.btnSpkChoiseDif:
                selectDifLevel();
                break;
            case R.id.ivHelpEditProfile:
                presenter.onHelpClicked();
                break;
        }
    }

    private void selectDifLevel() {
        DialogFragment newFragment = DifLevelDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onEasyClicked() {
        btnDifLevel.setText(R.string.dif_level_easy);
    }

    @Override
    public void onNormalClicked() {
        btnDifLevel.setText(R.string.dif_level_normal);
    }

    @Override
    public void onHardClicked() {
        btnDifLevel.setText(R.string.dif_level_hard);
    }

    @Override
    public void saveProfile(Profile profile) {
        presenter.saveProfile(isNeedShowOnboard, profile);
    }

    private void createAlertDialogLevelLoad() {
        DialogFragment newFragment = ActiveSportDialogFragment.newInstance(sportActivity);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onSportActivity(SportActivity sportActivity, String activity) {
        this.sportActivity = sportActivity;
        btnChoiseLevel.setText(activity);
    }

    @Override
    public void showProgress(boolean show) {
        showProgressDialog(show);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

}
