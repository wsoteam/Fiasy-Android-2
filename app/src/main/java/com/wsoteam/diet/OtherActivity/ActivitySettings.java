package com.wsoteam.diet.OtherActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.yandex.metrica.YandexMetrica;

public class ActivitySettings extends AppCompatActivity {
    private CardView cvRate, cvPrivacy, cvShare, cvLogout;
    private Switch switchRewrite;
    private SharedPreferences isRewriteProfileData;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cvPrivacy = findViewById(R.id.cvPrivacy);
        cvRate = findViewById(R.id.cvRate);
        cvShare = findViewById(R.id.cvShare);
        cvLogout = findViewById(R.id.cvLogOut);
        switchRewrite = findViewById(R.id.switchRewrite);
        ivBack = findViewById(R.id.ivBack);
        handleSwitch();

        Adjust.trackEvent(new AdjustEvent(EventsAdjust.view_settings));
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_settings);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                UserDataHolder.clearObject();
                finish();
                startActivity(new Intent(ActivitySettings.this, ActivitySplash.class).
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
            }
        });

        cvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.url_gdpr)));
                startActivity(intent);
            }
        });

        cvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adjust.trackEvent(new AdjustEvent(EventsAdjust.share));
                Amplitude.getInstance().logEvent(AmplitudaEvents.share);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.accompanying_text)
                        + "\n"
                        + "https://play.google.com/store/apps/details?id="
                        + getPackageName());
                startActivity(intent);
            }
        });

        YandexMetrica.reportEvent("Открыт фрагмент: Настройки");

    }

    private void handleSwitch() {
        isRewriteProfileData = getSharedPreferences(Config.TAG_OF_REWRITE, Context.MODE_PRIVATE);

        if (isRewriteProfileData.getInt(Config.TAG_OF_REWRITE, Config.NOT_ENTER_EARLY) == Config.REWRITE_PROFILE) {
            switchRewrite.setChecked(true);
        }
        SharedPreferences.Editor editor = isRewriteProfileData.edit();

        switchRewrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putInt(Config.TAG_OF_REWRITE, Config.REWRITE_PROFILE).commit();
                } else {
                    editor.putInt(Config.TAG_OF_REWRITE, Config.NOT_REWRITE_PROFILE).commit();
                }
            }
        });
    }


}
