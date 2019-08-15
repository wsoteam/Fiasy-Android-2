package com.wsoteam.diet.presentation.profile.help.detail;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.help.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvText) TextView tvText;
    private int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra(Config.POSITION_HELP, 0);
        tvTitle.setText(getResources().getStringArray(R.array.names_items_help)[position]);
        tvText.setText(getResources().getStringArray(R.array.texts_items_help)[position]);
    }

    @OnClick(R.id.ibBack)
    public void onViewClicked() {
        onBackPressed();
    }
}
