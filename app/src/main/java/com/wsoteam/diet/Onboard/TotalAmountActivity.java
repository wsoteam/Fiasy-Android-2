package com.wsoteam.diet.Onboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.EditProfileIntrodaction;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TotalAmountActivity extends AppCompatActivity {

    @BindView(R.id.tvKcalCount) TextView tvKcalCount;
    @BindView(R.id.tvProtCount) TextView tvProtCount;
    @BindView(R.id.tvFatCount) TextView tvFatCount;
    @BindView(R.id.tvCarboCount) TextView tvCarboCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_amount);
        ButterKnife.bind(this);
        bindValues(UserDataHolder.getUserData().getProfile());
    }

    private void bindValues(Profile profile) {
        tvKcalCount.setText(String.valueOf(profile.getMaxKcal()));
        tvProtCount.setText(String.valueOf(profile.getMaxProt()) + " г");
        tvFatCount.setText(String.valueOf(profile.getMaxFat()) + " г");
        tvCarboCount.setText(String.valueOf(profile.getMaxCarbo()) + " г");
    }

    @OnClick(R.id.btnNext)
    public void onViewClicked() {
        startActivity(new Intent(TotalAmountActivity.this, ActivitySubscription.class)
                .putExtra(Config.AMPLITUDE_COME_FROM, AmplitudaEvents.view_prem_free_onboard)
                .putExtra(Config.AMPLITUDE_BUY_FROM, AmplitudaEvents.buy_prem_free_onboard)
                .putExtra(Config.OPEN_PREM_FROM_INTRODACTION, true));
        finish();
    }
}
