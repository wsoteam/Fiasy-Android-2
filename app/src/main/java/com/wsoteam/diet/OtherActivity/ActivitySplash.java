package com.wsoteam.diet.OtherActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Authenticate.ActivityAuth;
import com.wsoteam.diet.Authenticate.ActivityAuthenticate;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.GlobalObject;
import com.wsoteam.diet.R;

public class ActivitySplash extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivLoading;
    private Animation animationRotate;
    private FirebaseUser user;
    //e
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        tvTitle = findViewById(R.id.tvTitleSplashScreen);
        ivLoading = findViewById(R.id.ivLoadingIcon);
        tvTitle.setTypeface(Typeface.createFromAsset(getAssets(), "main_font.ttf"));
        animationRotate = AnimationUtils.loadAnimation(this, R.anim.animation_rotate);
        ivLoading.startAnimation(animationRotate);


        if (!hasConnection(this)) {
            Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_ENTITY_FOR_DB);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObjectHolder objectHolder = new ObjectHolder();
                objectHolder.bindObjectWithHolder(dataSnapshot.getValue(GlobalObject.class));


                if (user == null) {
                    startActivity(new Intent(ActivitySplash.this, ActivityAuth.class));
                    finish();
                } else {
                    startActivity(new Intent(ActivitySplash.this, MainActivity.class));
                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }






}
