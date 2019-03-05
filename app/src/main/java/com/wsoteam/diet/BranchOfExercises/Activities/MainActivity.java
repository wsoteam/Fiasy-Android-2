package com.wsoteam.diet.BranchOfExercises.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen.FragmentFavorites;
import com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen.FragmentPartsOfBody;
import com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen.FragmentProgramms;
import com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen.FragmentsArticles;
import com.wsoteam.diet.BranchOfExercises.ObjectHolder;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOSExercises.GlobalObject;
import com.wsoteam.diet.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageView loadingBar;
    private Animation animationRotate;

    private final String WorkOutApplicationId = "AIzaSyAVcr1U0xbS3Bf0DzWOt7tzdLpxB-nP1yw";
    private final String WorkOutApiKey = "1:584294646007:android:9e20405dfc5e7aea";
    private final String WorkOutDatabaseUrl = "https://workout-f67d0.firebaseio.com/";
    private boolean isLoadedEarly = false;
    private final String firebaseAppName = "secondary";


    private ArrayList<Fragment> listOfFragments;
    private ViewPager mViewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                /*case R.id.navigation_ex:
                    mViewPager.setCurrentItem(1);
                    return true;*/
                case R.id.navigation_favorites:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_articles:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_main);

        AdRequest request = new AdRequest.Builder().build();

        loadingBar = findViewById(R.id.ex_ivLoadingCircle);
        animationRotate = AnimationUtils.loadAnimation(this, R.anim.animation_rotate);
        loadingBar.startAnimation(animationRotate);

        if (!hasConnection(this)) {
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        }

        final BottomNavigationView navigation = findViewById(R.id.ex_navigation);
        mViewPager = findViewById(R.id.ex_vpMainActivity);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadDataFromFireBase();

    }

    private void loadDataFromFireBase() {

        //check early load
        for (int i = 0; i < FirebaseApp.getApps(this).size(); i++) {
            if (FirebaseApp.getApps(this).get(i).getName().equals(firebaseAppName)) {
                isLoadedEarly = true;
            }
        }

        if (!isLoadedEarly) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId(WorkOutApplicationId)
                    .setApiKey(WorkOutApiKey)
                    .setDatabaseUrl(WorkOutDatabaseUrl)
                    .build();
            FirebaseApp.initializeApp(this, options, firebaseAppName);
        }

        FirebaseApp app = FirebaseApp.getInstance(firebaseAppName);
        FirebaseDatabase database = FirebaseDatabase.getInstance(app);

        DatabaseReference myRef = database.getReference(Config.EXERCISES_NAME_OF_ENTITY_FOR_DB);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ObjectHolder objectHolder = new ObjectHolder();
                objectHolder.createObjFromGirebase(dataSnapshot.getValue(GlobalObject.class));
                listOfFragments = new ArrayList<>();
                listOfFragments.add(new FragmentProgramms());
                /*listOfFragments.add(new FragmentPartsOfBody());*/
                listOfFragments.add(new FragmentFavorites());
                listOfFragments.add(new FragmentsArticles());

                mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return listOfFragments.get(position);
                    }

                    @Override
                    public int getCount() {
                        return listOfFragments.size();
                    }
                });
                loadingBar.clearAnimation();
                loadingBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static boolean hasConnection(final Context context) {
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
