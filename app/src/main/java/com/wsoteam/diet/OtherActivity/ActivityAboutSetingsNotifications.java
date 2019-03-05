package com.wsoteam.diet.OtherActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wsoteam.diet.R;

public class ActivityAboutSetingsNotifications extends AppCompatActivity {
    private Button btnOpenAutoLaunch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_setings_notifications);
        btnOpenAutoLaunch = findViewById(R.id.btnOpenSettingsAutoLaunch);

        btnOpenAutoLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String manufacturer = "xiaomi";
                    if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                        //this will open auto start screen where user can enable permission for your app
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Log.e("Error", "ActivityAboutSetingsNotifications");
                }
            }
        });
    }
}
