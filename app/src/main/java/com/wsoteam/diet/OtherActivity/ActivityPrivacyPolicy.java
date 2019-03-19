package com.wsoteam.diet.OtherActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.wsoteam.diet.R;

public class ActivityPrivacyPolicy extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        textView = findViewById(R.id.tvPP);
        textView.setText(Html.fromHtml(getString(R.string.privacy_policy2)));
    }
}
