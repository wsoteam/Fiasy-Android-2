package com.wsoteam.diet.presentation.profile.norm.choise.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionActivityFragments;

import butterknife.BindView;

public class ActivFragment extends QuestionActivityFragments {
    public static final String ACTIVITY_TAG = "ACTIVITY_TAG";
    private String activity;
    private TextView tvActivity;
    private Button btnSave;

    public static ActivFragment newInstance(String activity) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TAG, activity);
        ActivFragment activFragment = new ActivFragment();
        activFragment.setArguments(bundle);
        return activFragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        activity = getArguments().getString(ACTIVITY_TAG);
        changeProgress(choisePosition(activity));
        changeButtonText(getActivity().getResources().getString(R.string.activity_save));
        tvActivity = getView().findViewById(R.id.tvActivity);
        btnSave = getView().findViewById(R.id.btnNext);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("LOL", tvActivity.getText().toString());
            }
        });
    }

    private int choisePosition(String activity) {
        int position = 0;
        String[] activities = getActivity().getResources().getStringArray(R.array.activities);
        for (int i = 0; i < activities.length; i++) {
            if (activity.equalsIgnoreCase(activities[i])) {
                position = i;
                break;
            }
        }
        return position;
    }
}
