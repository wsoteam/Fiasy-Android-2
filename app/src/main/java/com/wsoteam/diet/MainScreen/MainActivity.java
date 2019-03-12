package com.wsoteam.diet.MainScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.wsoteam.diet.Authenticate.ActivityAuth;


import com.wsoteam.diet.Authenticate.ActivityAuthenticate;
import com.wsoteam.diet.BranchOfCalculating.ActivityListOfCalculating;
import com.wsoteam.diet.BranchOfDiary.ActivityListOfDiary;
import com.wsoteam.diet.BranchOfMonoDiets.ActivityMonoDiet;
import com.wsoteam.diet.BranchOfNotifications.ActivityListOfNotifications;
import com.wsoteam.diet.BranchOfRecipes.ActivityGroupsOfRecipes;
import com.wsoteam.diet.BranchProfile.ActivityEditProfile;
import com.wsoteam.diet.BranchProfile.ActivityProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.Support.AlertDialogChoiseEating;
import com.wsoteam.diet.MainScreen.Controller.EatingAdapter;
import com.wsoteam.diet.MainScreen.Fragments.FragmentEatingScroll;
import com.wsoteam.diet.OtherActivity.ActivitySettings;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.POJOsCircleProgress.Water;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.tvCircleProgressProt) TextView tvCircleProgressProt;
    @BindView(R.id.apCollapsingKcal) ArcProgress apCollapsingKcal;
    @BindView(R.id.apCollapsingProt) ArcProgress apCollapsingProt;
    @BindView(R.id.apCollapsingCarbo) ArcProgress apCollapsingCarbo;
    @BindView(R.id.apCollapsingFat) ArcProgress apCollapsingFat;
    @BindView(R.id.ivCollapsingMainCompleteWater) ImageView ivCollapsingMainCompleteWater;
    @BindView(R.id.fabAddEating) FloatingActionButton fabAddEating;
    @BindView(R.id.tvCircleProgressCarbo) TextView tvCircleProgressCarbo;
    @BindView(R.id.tvCircleProgressFat) TextView tvCircleProgressFat;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.mainappbar) AppBarLayout mainappbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.nav_view_g) NavigationView navViewG;
    @BindView(R.id.vpEatingTimeLine) ViewPager vpEatingTimeLine;
    @BindView(R.id.tvDateForMainScreen) TextView tvDateForMainScreen;
    private TextView tvLeftNBName;
    private CircleImageView ivLeftNBAvatar;
    private EatingAdapter eatingAdapter;

    private AnimatedVectorDrawable animatedVectorDrawable;
    private Animation animChangeScale, animRotateCancelWater, animWaterComplete;
    private SoundPool soundPool;
    private int soundIDdBubble;

    private Water water;
    private Profile profile;

    private int COUNT_OF_RUN = 0;
    private final String TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG = "COUNT_OF_RUN";
    private SharedPreferences countOfRun;
    private boolean isFiveStarSend = false;
    private boolean isFullWater;





    @Override
    protected void onResume() {
        super.onResume();

        if (Profile.count(Profile.class) == 1) {
            profile = Profile.last(Profile.class);
            tvLeftNBName.setText(profile.getFirstName() + " " + profile.getLastName());
            tvLeftNBName.setTextSize(17);
            if (!profile.getPhotoUrl().equals("default")) {
                Uri uri = Uri.parse(profile.getPhotoUrl());
                Glide.with(MainActivity.this).load(uri).into(ivLeftNBAvatar);
            }
        }

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Handler bindHandler = new Handler(Looper.getMainLooper());
        bindHandler.post(new Runnable() {
            @Override
            public void run() {
                setMaxParamsInProgressBars(day, month, year, profile);
            }
        });

        ivLeftNBAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profile == null) {
                    Intent intent = new Intent(MainActivity.this, ActivityEditProfile.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, ActivityProfile.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void showThankToast() {
        if (isFiveStarSend) {
            isFiveStarSend = false;
            TextView tvToastCompleteGift;
            ImageView ivToastCompleteGift;
            LayoutInflater toastInflater = getLayoutInflater();
            View toastLayout = toastInflater.inflate(R.layout.toast_complete_gift, null, false);
            tvToastCompleteGift = toastLayout.findViewById(R.id.tvToastCompleteGift);
            ivToastCompleteGift = toastLayout.findViewById(R.id.ivToastCompleteGift);
            tvToastCompleteGift.setText("Спасибо за отзыв!");

            Glide.with(this).load(R.drawable.icon_toast_thank_for_grade).into(ivToastCompleteGift);

            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        MobileAds.initialize(this, Config.ADMOB_ID);
        setSupportActionBar(toolbar);
        setTitle("");

        mainappbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.main_menu));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        loadSound();
        bindDrawer();
        showThankToast();
        additionOneToSharedPreference();
        checkFirstRun();
        bindViewPager();

        fabAddEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogChoiseEating.createChoiseEatingAlertDialog(MainActivity.this,
                        tvDateForMainScreen.getText().toString()).show();
            }
        });



    }

    private void bindViewPager() {
        vpEatingTimeLine.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentEatingScroll.newInstance(position);
            }

            @Override
            public int getCount() {
                return Config.COUNT_PAGE + 1;
            }
        });
        vpEatingTimeLine.setCurrentItem(Config.COUNT_PAGE);
    }


    private void bindDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navViewG.setNavigationItemSelectedListener(this);
        animChangeScale = AnimationUtils.loadAnimation(this, R.anim.anim_change_scale);
        animWaterComplete = AnimationUtils.loadAnimation(this, R.anim.anim_water_complete_tick);
        animRotateCancelWater = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_cancel_water);

        View view = navViewG.getHeaderView(0);
        tvLeftNBName = view.findViewById(R.id.tvLeftNBName);
        ivLeftNBAvatar = view.findViewById(R.id.ivLeftNBAvatar);
    }



    private void loadSound() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        try {
            soundIDdBubble = soundPool.load(getAssets().openFd("buble.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setMaxParamsInProgressBars(int day, int month, int year, @Nullable Profile profile) {
        if (profile != null) {
            apCollapsingKcal.setMax(profile.getMaxKcal());
            apCollapsingProt.setMax(profile.getMaxProt());
            apCollapsingCarbo.setMax(profile.getMaxCarbo());
            apCollapsingFat.setMax(profile.getMaxFat());
        } else {
            apCollapsingKcal.setMax(2000);
            apCollapsingProt.setMax(100);
            apCollapsingCarbo.setMax(100);
            apCollapsingFat.setMax(100);
        }

    }

    private void additionOneToSharedPreference() {
        countOfRun = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = countOfRun.edit();
        editor.putInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, countOfRun.getInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, COUNT_OF_RUN) + 1);
        editor.commit();

    }


    private void checkFirstRun() {
        if (countOfRun.getInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, COUNT_OF_RUN) == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alertDialog = builder.create();
            View view = getLayoutInflater().inflate(R.layout.alert_dialog_grade, null);

            Animation movFromLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_moving_from_left);
            Animation movOutToRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_moving_out_to_right);
            Animation movFromRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.moving_from_right);

            RatingBar ratingBar = view.findViewById(R.id.ratingBar);
            EditText edtReport = view.findViewById(R.id.edtRatingReport);
            TextView tvThank = view.findViewById(R.id.tvForGrade);
            Button btnGradeClose = view.findViewById(R.id.btnGradeClose);
            Button btnGradeLate = view.findViewById(R.id.btnGradeLate);
            Button btnGradeSend = view.findViewById(R.id.btnGradeSend);

            btnGradeClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YandexMetrica.reportEvent("Отказ в оценке");
                    alertDialog.cancel();
                }
            });

            btnGradeLate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countOfRun = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = countOfRun.edit();
                    editor.putInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, 0);
                    editor.commit();
                    YandexMetrica.reportEvent("Оценка отложена");
                    alertDialog.cancel();
                }
            });

            btnGradeSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sav@wsoteam.com", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв");
                    intent.putExtra(Intent.EXTRA_TEXT, edtReport.getText().toString());
                    startActivity(Intent.createChooser(intent, "Send Email"));
                    alertDialog.cancel();
                }
            });

            btnGradeSend.setVisibility(View.GONE);
            tvThank.setVisibility(View.GONE);
            edtReport.setVisibility(View.GONE);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    if (v >= 4) {
                        YandexMetrica.reportEvent("Переход в ГП для оценки");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + MainActivity.this.getPackageName()));
                        startActivity(intent);
                        alertDialog.cancel();
                        isFiveStarSend = true;
                    } else {
                        if (edtReport.getVisibility() == View.GONE) {
                            edtReport.setVisibility(View.VISIBLE);
                            edtReport.startAnimation(movFromLeft);
                            ratingBar.startAnimation(movOutToRight);
                            ratingBar.setVisibility(View.GONE);
                            tvThank.setVisibility(View.VISIBLE);
                            tvThank.startAnimation(movFromLeft);
                            btnGradeSend.setVisibility(View.VISIBLE);
                            btnGradeSend.startAnimation(movFromRight);
                        }
                    }
                }
            });
            alertDialog.setView(view);
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private void signOut() {

        Log.d("Authenticate", "signOut: in MainActivity ");

        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
        //Facebook signOut
        LoginManager.getInstance().logOut();

//        startActivity(new Intent(this, ActivityAuth.class));
//        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();

        switch (id) {
            case R.id.nav_diary:
                intent = new Intent(MainActivity.this, ActivityListOfDiary.class);
                break;
            case R.id.nav_diets:
                intent = new Intent(MainActivity.this, ActivityMonoDiet.class);
                break;
            case R.id.nav_recipes:
                intent = new Intent(MainActivity.this, ActivityGroupsOfRecipes.class);
                break;
            case R.id.nav_notifications:
                intent = new Intent(MainActivity.this, ActivityListOfNotifications.class);
                break;
            case R.id.nav_fitness:
                intent = new Intent(MainActivity.this, com.wsoteam.diet.BranchOfExercises.Activities.MainActivity.class);
                break;
            case R.id.nav_calculating:
                intent = new Intent(MainActivity.this, ActivityListOfCalculating.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, ActivitySettings.class);
                break;
            case R.id.nav_exit:
                intent = new Intent(MainActivity.this, ActivityAuth.class);
                signOut();
                break;
        }
        startActivity(intent);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }


        return true;
    }


}
