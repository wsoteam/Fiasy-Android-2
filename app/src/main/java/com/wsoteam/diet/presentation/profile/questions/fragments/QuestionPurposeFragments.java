package com.wsoteam.diet.presentation.profile.questions.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionPurposeFragments extends Fragment {
    @BindView(R.id.cbLooseWeight) RadioButton cbLooseWeight;
    @BindView(R.id.cbNormal) RadioButton cbNormal;
    @BindView(R.id.cbMuscle) RadioButton cbMuscle;
    @BindView(R.id.cbSave) RadioButton cbSave;
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static QuestionPurposeFragments newInstance(int page, String title) {
        QuestionPurposeFragments fragmentFirst = new QuestionPurposeFragments();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_purpose, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.cbNormal, R.id.cbLooseWeight, R.id.cbMuscle, R.id.cbSave})
    public void onClick(View view) {
        cbNormal.setChecked(false);
        cbLooseWeight.setChecked(false);
        cbMuscle.setChecked(false);
        cbSave.setChecked(false);
        switch (view.getId()) {
            case R.id.cbNormal:
                cbNormal.setChecked(true);
                break;
            case R.id.cbLooseWeight:
                cbLooseWeight.setChecked(true);
                break;
            case R.id.cbMuscle:
                cbMuscle.setChecked(true);
                break;
            case R.id.cbSave:
                cbSave.setChecked(true);
                break;
        }
    }
}