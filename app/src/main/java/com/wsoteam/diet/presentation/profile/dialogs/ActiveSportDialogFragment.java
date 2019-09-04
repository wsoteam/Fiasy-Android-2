package com.wsoteam.diet.presentation.profile.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.common.SportActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActiveSportDialogFragment extends DialogFragment {

    private static final String BUNDLE_KEY = "key";
    @BindView(R.id.rgLevelLoad) RadioGroup rgLevelLoad;
    @BindView(R.id.rbLevelLoadNone) RadioButton rbLevelLoadNone;
    @BindView(R.id.rbLevelLoadEasy) RadioButton rbLevelLoadEasy;
    @BindView(R.id.rbLevelLoadMedium) RadioButton rbLevelLoadMedium;
    @BindView(R.id.rbLevelLoadHard) RadioButton rbLevelLoadHard;
    @BindView(R.id.rbLevelLoadUpHard) RadioButton rbLevelLoadUpHard;
    @BindView(R.id.rbLevelLoadSuper) RadioButton rbLevelLoadSuper;
    @BindView(R.id.rbLevelLoadUpSuper) RadioButton rbLevelLoadUpSuper;
    private OnSportActivityClickListener mListener;
    private SportActivity sportActivity;
    private Unbinder unbinder;

    public static ActiveSportDialogFragment newInstance(SportActivity sportActivity) {
        ActiveSportDialogFragment f = new ActiveSportDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY, sportActivity);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sportActivity = (SportActivity) getArguments().get(BUNDLE_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_level, container, false);
        unbinder = ButterKnife.bind(this, view);
        switch (sportActivity) {
            case NONE:
                rbLevelLoadNone.setChecked(true);
                break;
            case EASY:
                rbLevelLoadEasy.setChecked(true);
                break;
            case MEDIUM:
                rbLevelLoadMedium.setChecked(true);
                break;
            case HARD:
                rbLevelLoadHard.setChecked(true);
                break;
            case HARD_UP:
                rbLevelLoadUpHard.setChecked(true);
                break;
            case SUPER:
                rbLevelLoadSuper.setChecked(true);
                break;
            case SUPER_UP:
                rbLevelLoadUpSuper.setChecked(true);
                break;
        }
        return view;
    }

    @OnClick({R.id.rbLevelLoadNone, R.id.rbLevelLoadEasy, R.id.rbLevelLoadMedium, R.id.rbLevelLoadHard
            , R.id.rbLevelLoadUpHard, R.id.rbLevelLoadSuper, R.id.rbLevelLoadUpSuper})
    void activityClick(RadioButton view) {
        switch (view.getId()) {
            case R.id.rbLevelLoadNone:
                sportActivity = SportActivity.NONE;
                break;
            case R.id.rbLevelLoadEasy:
                sportActivity = SportActivity.EASY;
                break;
            case R.id.rbLevelLoadMedium:
                sportActivity = SportActivity.MEDIUM;
                break;
            case R.id.rbLevelLoadHard:
                sportActivity = SportActivity.HARD;
                break;
            case R.id.rbLevelLoadUpHard:
                sportActivity = SportActivity.HARD_UP;
                break;
            case R.id.rbLevelLoadSuper:
                sportActivity = SportActivity.SUPER;
                break;
            case R.id.rbLevelLoadUpSuper:
                sportActivity = SportActivity.SUPER_UP;
                break;
        }
        mListener.onSportActivity(sportActivity, view.getText().toString());
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
            this.mListener = (OnSportActivityClickListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnSportActivityClickListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public interface OnSportActivityClickListener {
        void onSportActivity(SportActivity sportActivity, String activity);
    }
}
