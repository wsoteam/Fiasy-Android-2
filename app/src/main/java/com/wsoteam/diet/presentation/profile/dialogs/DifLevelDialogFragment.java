package com.wsoteam.diet.presentation.profile.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wsoteam.diet.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DifLevelDialogFragment extends DialogFragment {

    private OnDifLevelClickListener mListener;
    private Unbinder unbinder;

    public static DifLevelDialogFragment newInstance() {
        return new DifLevelDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_choise_difficulty_level, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.cvADChoiseDiffLevelEasy, R.id.cvADChoiseDiffLevelNormal, R.id.cvADChoiseDiffLevelHard})
    void levelClick(View view) {
        switch (view.getId()) {
            case R.id.cvADChoiseDiffLevelEasy:
                mListener.onEasyClicked();
                break;
            case R.id.cvADChoiseDiffLevelNormal:
                mListener.onNormalClicked();
                break;
            case R.id.cvADChoiseDiffLevelHard:
                mListener.onHardClicked();
                break;
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnDifLevelClickListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnDifLevelClickListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public interface OnDifLevelClickListener {
        void onEasyClicked();

        void onNormalClicked();

        void onHardClicked();
    }
}
