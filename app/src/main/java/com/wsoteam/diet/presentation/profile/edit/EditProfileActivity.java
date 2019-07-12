package com.wsoteam.diet.presentation.profile.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.alterac.blurkit.BlurLayout;
import io.reactivex.disposables.Disposable;

public class EditProfileActivity extends BaseActivity implements EditProfileView {

    private static final String TAG = "EditProfile";
    @Inject
    @InjectPresenter
    EditProfilePresenter presenter;

    @BindView(R.id.ivMain) ImageView ivMain;
    @BindView(R.id.blurLayout) BlurLayout blurLayout;
    @BindView(R.id.btnNext) TextView btnNext;

    @BindView(R.id.tvTitleName) AppCompatCheckedTextView tvTitleName;
    @BindView(R.id.dot1) AppCompatCheckedTextView dot1;
    @BindView(R.id.line1) View line1;
    @BindView(R.id.edName) EditText edName;

    @BindView(R.id.tvAge) AppCompatCheckedTextView tvAge;
    @BindView(R.id.dot2) AppCompatCheckedTextView dot2;
    @BindView(R.id.line2) View line2;
    @BindView(R.id.edAge) EditText edAge;

    @BindView(R.id.tvSex) AppCompatCheckedTextView tvSex;
    @BindView(R.id.edSex) AppCompatCheckedTextView edSex;
    @BindView(R.id.dot3) AppCompatCheckedTextView dot3;
    @BindView(R.id.line3) View line3;

    @BindView(R.id.tvHeight) AppCompatCheckedTextView tvHeight;
    @BindView(R.id.chHeight) AppCompatCheckedTextView chHeight;
    @BindView(R.id.dot4) AppCompatCheckedTextView dot4;
    @BindView(R.id.line4) View line4;
    @BindView(R.id.edHeight) EditText edHeight;

    @BindView(R.id.tvDiff) AppCompatCheckedTextView tvDiff;
    @BindView(R.id.edDiff) AppCompatCheckedTextView edDiff;
    @BindView(R.id.dot5) AppCompatCheckedTextView dot5;
    @BindView(R.id.line5) View line5;

    @BindView(R.id.tvActivity) AppCompatCheckedTextView tvActivity;
    @BindView(R.id.edActivity) AppCompatCheckedTextView edActivity;
    @BindView(R.id.dot6) AppCompatCheckedTextView dot6;
    @BindView(R.id.line6) View line6;

    @BindView(R.id.tvWeight) AppCompatCheckedTextView tvWeight;
    @BindView(R.id.chWeight) AppCompatCheckedTextView chWeight;
    @BindView(R.id.dot7) AppCompatCheckedTextView dot7;
    @BindView(R.id.edWeight) EditText edWeight;
    Disposable dName, dAge, dHeight, dWeight;
    boolean isFemale = false, createUser;
    private boolean isNeedShowOnboard = false;

    @ProvidePresenter
    EditProfilePresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_new);
        ButterKnife.bind(this);

        if (getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).getBoolean(Config.IS_NEED_SHOW_ONBOARD, false)) {
            isNeedShowOnboard = true;
            getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).edit().putBoolean(Config.IS_NEED_SHOW_ONBOARD, false).apply();
        }

        createUser = getIntent().getBooleanExtra(Config.CREATE_PROFILE, false);

        dName = RxTextView.textChanges(edName).subscribe(charSequence -> {
            boolean isCheck = !TextUtils.isEmpty(charSequence);
            presenter.changeState(isCheck, tvTitleName, dot1, line1);
        });

        dAge = RxTextView.textChanges(edAge).subscribe(charSequence -> {
            boolean isCheck = !TextUtils.isEmpty(charSequence);
            presenter.changeState(isCheck, tvAge, dot2, line2);
        });

        dHeight = RxTextView.textChanges(edHeight).subscribe(charSequence -> {
            boolean isCheck = !TextUtils.isEmpty(charSequence);
            chHeight.setChecked(isCheck);
            presenter.changeState(isCheck, tvHeight, dot4, line4);
        });

        dWeight = RxTextView.textChanges(edWeight).subscribe(charSequence -> {
            boolean isCheck = !TextUtils.isEmpty(charSequence);
            chWeight.setChecked(isCheck);
            presenter.changeState(isCheck, tvWeight, dot7, null);
        });

    }

    @OnClick({R.id.btnNext, R.id.chHeight, R.id.chWeight, R.id.edSex, R.id.edDiff, R.id.edActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                presenter.calculate(edName.getText().toString(), edAge.getText().toString(), isFemale,
                        edHeight.getText().toString(), edDiff.getText().toString(), edActivity.getText().toString(),
                        edWeight.getText().toString());
                break;
                
//            case R.id.chHeight:
//                attachDimensionPopup((AppCompatCheckedTextView) view);
//                break;
//            case R.id.chWeight:
//                attachWeightPopup((AppCompatCheckedTextView) view);
//                break;
            case R.id.edSex:
                attachSexPopup((AppCompatCheckedTextView) view);
                break;
            case R.id.edDiff:
                attachDiffPopup((AppCompatCheckedTextView) view);
                break;
            case R.id.edActivity:
                attachActivityPopup((AppCompatCheckedTextView) view);
                break;
        }
    }

    private void attachDimensionPopup(AppCompatCheckedTextView view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_popup_dimension);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.dimen_cm:
                    view.setText(getString(R.string.cm));
                    break;
            }
            view.setChecked(true);
            presenter.changeState(!TextUtils.isEmpty(edHeight.getText().toString()), tvHeight, dot4, line4);
            return true;
        });
        popupMenu.show();
    }

    private void attachWeightPopup(AppCompatCheckedTextView view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_popup_weight);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.dimen_kg:
                    view.setText(getString(R.string.kg));
                    break;
            }
            view.setChecked(true);
            presenter.changeState(!TextUtils.isEmpty(edWeight.getText().toString()), tvWeight, dot7, null);
            return true;
        });
        popupMenu.show();
    }

    private void attachSexPopup(AppCompatCheckedTextView view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_popup_sex);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.sex_female:
                    view.setText(getString(R.string.sex_female).toUpperCase());
                    isFemale = true;
                    break;
                case R.id.sex_male:
                    view.setText(getString(R.string.sex_male).toUpperCase());
                    isFemale = false;
                    break;
            }
            view.setChecked(true);
            presenter.changeState(true, tvSex, dot3, line3);
            return true;
        });
        popupMenu.show();
    }

    private void attachDiffPopup(AppCompatCheckedTextView view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_popup_diff);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.diff_easy:
                    view.setText(getString(R.string.dif_level_easy).toUpperCase());
                    break;
                case R.id.diff_normal:
                    view.setText(getString(R.string.dif_level_normal).toUpperCase());
                    break;
                case R.id.diff_hard:
                    view.setText(getString(R.string.dif_level_hard).toUpperCase());
                    break;
            }
            view.setChecked(true);
            presenter.changeState(true, tvDiff, dot5, line5);
            return true;
        });
        popupMenu.show();
    }

    private void attachActivityPopup(AppCompatCheckedTextView view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_popup_activity);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.activity_none:
                    view.setText(getString(R.string.level_none).toUpperCase());
                    break;
                case R.id.activity_easy:
                    view.setText(getString(R.string.level_easy).toUpperCase());
                    break;
                case R.id.activity_medium:
                    view.setText(getString(R.string.level_medium).toUpperCase());
                    break;
                case R.id.activity_hard:
                    view.setText(getString(R.string.level_hard).toUpperCase());
                    break;
                case R.id.activity_up_hard:
                    view.setText(getString(R.string.level_up_hard).toUpperCase());
                    break;
                case R.id.activity_super:
                    view.setText(getString(R.string.level_super).toUpperCase());
                    break;
                case R.id.activity_up_super:
                    view.setText(getString(R.string.level_up_super).toUpperCase());
                    break;
            }
            view.setChecked(true);
            presenter.changeState(true, tvActivity, dot6, line6);
            return true;
        });
        popupMenu.show();
    }

    @Override
    public void saveProfile(Profile profile) {
        presenter.saveProfile(isNeedShowOnboard, profile, createUser);
    }

    @Override
    public void changeNextState() {
        btnNext.setEnabled(dot1.isChecked() && dot2.isChecked() && dot3.isChecked()
                && dot4.isChecked() && dot5.isChecked()
                && dot6.isChecked() && dot7.isChecked());
    }

    @Override
    public void showProgress(boolean show) {
        showProgressDialog(show);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
        dName.dispose();
        dAge.dispose();
        dHeight.dispose();
        dWeight.dispose();
    }
}
