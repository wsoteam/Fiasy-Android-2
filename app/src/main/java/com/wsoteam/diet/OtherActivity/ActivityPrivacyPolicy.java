package com.wsoteam.diet.OtherActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.wsoteam.diet.R;

public class ActivityPrivacyPolicy extends AppCompatActivity {

    TextView fTextView;
    Button  backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        backButton = findViewById(R.id.policy_btn_back);
        backButton.setOnClickListener(view -> onBackPressed());
        fTextView = findViewById(R.id.policy_tv1);
        fTextView.setText(Html.fromHtml(getString(R.string.privacy_policy2)));
    }
}
