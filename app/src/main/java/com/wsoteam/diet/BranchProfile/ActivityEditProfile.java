package com.wsoteam.diet.BranchProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.ActivityAuthMain;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.yandex.metrica.YandexMetrica;

import java.util.Calendar;

public class ActivityEditProfile extends AppCompatActivity {

    private String dif_level;

    private EditText edtHeight, edtAge, edtWeight;
    private String SpkName = "default";
    private String SpkSecondName = "default";
    private Button btnDifLevel;
    private Button btnChoiseLevel;
    private RadioGroup rgFemaleOrMale;
//    private CircleImageView civEditProfile;
//    private FloatingActionButton fabEditProfile;
    private ImageView ivHelpEditProfile;
    private Button nextButton;

    private InterstitialAd interstitialAd;

    private boolean registration;

    private final String DEFAULT_AVATAR = "default";
    private final int WATER_ON_KG_FEMALE = 30;
    private final int WATER_ON_KG_MALE = 40;
    private String urlOfPhoto = "default";
    private int day, month, year;

    private boolean isFemale = true;
    private double SPK = 0, upLineSPK = 0, downLineSPK = 0;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        edtHeight = findViewById(R.id.edtSpkGrowth);
        edtAge = findViewById(R.id.edtSpkAge);
        edtWeight = findViewById(R.id.edtSpkWeight);
        btnDifLevel = findViewById(R.id.btnSpkChoiseDif);
        btnChoiseLevel = findViewById(R.id.btnSpkChoiseLevel);
        rgFemaleOrMale = findViewById(R.id.rgFemaleOrMaleSpk);
        nextButton = findViewById(R.id.rectangle_8);
        ivHelpEditProfile = findViewById(R.id.ivHelpEditProfile);

        if (dif_level == null){
            dif_level = getString(R.string.dif_level_easy);
            btnDifLevel.setText(R.string.dif_level_easy);
        }

        btnDifLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDifLevel();
            }
        });

        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            fillViewsIfProfileNotNull();
        }

        registration = getIntent().getBooleanExtra("registration",false);


        ivHelpEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditProfile.this, ActivityHelp.class);
                startActivity(intent);
            }
        });

        btnChoiseLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogAboutLevelLoad();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputData()) {
                    calculate();
                }
            }
        });


        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Редактировать профиль");


    }

    private void fillViewsIfProfileNotNull() {


        Profile profile = UserDataHolder.getUserData().getProfile();

        edtHeight.setText(String.valueOf(profile.getHeight()));
        edtAge.setText(String.valueOf(profile.getAge()));
        edtWeight.setText(String.valueOf(profile.getWeight()));
        btnDifLevel.setText(profile.getExerciseStress());
//        edtSpkName.setText(profile.getFirstName());
//        edtSpkSecondName.setText(profile.getLastName());
        if (profile.isFemale()) {
            rgFemaleOrMale.check(R.id.rdSpkFemale);
        } else {
            rgFemaleOrMale.check(R.id.rdSpkMale);
        }
        if (!profile.getPhotoUrl().equals(DEFAULT_AVATAR)){
            urlOfPhoto = profile.getPhotoUrl();
            Uri uri = Uri.parse(urlOfPhoto);
//            Glide.with(this).load(uri).into(civEditProfile);
        }

    }

    private boolean checkInputData() {
                if (rgFemaleOrMale.getCheckedRadioButtonId() != -1) {
                    if (!edtAge.getText().toString().equals("")
                            && Integer.parseInt(edtAge.getText().toString()) >= 18
                            && Integer.parseInt(edtAge.getText().toString()) <= 200) {
                        if (!edtHeight.getText().toString().equals("")
                                && Integer.parseInt(edtHeight.getText().toString()) >= 100
                                && Integer.parseInt(edtHeight.getText().toString()) <= 300) {
                            if (!edtWeight.getText().toString().equals("")
                                    && Double.parseDouble(edtWeight.getText().toString()) >= 30
                                    && Double.parseDouble(edtWeight.getText().toString()) <= 300) {
                                return true;
                            } else {
                                Toast.makeText(ActivityEditProfile.this, R.string.spk_check_weight, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        } else {
                            Toast.makeText(ActivityEditProfile.this, R.string.spk_check_your_height, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(ActivityEditProfile.this, R.string.spk_check_your_age, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ActivityEditProfile.this, R.string.spk_choise_your_gender, Toast.LENGTH_SHORT).show();
                    return false;
                }

    }

    private void selectDifLevel(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.alert_dialog_choise_difficulty_level, null);
        CardView cvADChoiseDiffLevelHard = view.findViewById(R.id.cvADChoiseDiffLevelHard);
        CardView cvADChoiseDiffLevelNormal = view.findViewById(R.id.cvADChoiseDiffLevelNormal);
        CardView cvADChoiseDiffLevelEasy = view.findViewById(R.id.cvADChoiseDiffLevelEasy);

        cvADChoiseDiffLevelEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dif_level = getString(R.string.dif_level_easy);
                btnDifLevel.setText(R.string.dif_level_easy);
                alertDialog.cancel();
            }
        });
        cvADChoiseDiffLevelNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dif_level = getString(R.string.dif_level_normal);
                btnDifLevel.setText(R.string.dif_level_normal);
                alertDialog.cancel();
            }
        });
        cvADChoiseDiffLevelHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dif_level = getString(R.string.dif_level_hard);
                btnDifLevel.setText(R.string.dif_level_hard);
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();


    }


    /*Минимальные нагрузки (сидячая работа) - К=1.2
    Немного дневной активности и легкие упражнения 1-3 раза в неделю - К=1.375
    Тренировки 4-5 раз в неделю (или работа средней тяжести) - К= 1.4625
    Интенсивные тренировки 4-5 раз в неделю - К=1.550
    Ежедневные тренировки - К=1.6375
    Ежедневные интенсивные тренировки или тренировки 2 раза в день - К=1.725
    Тяжелая физическая работа или интенсивные тренировки 2 раза в день - К=1.9*/

    private void calculate() {

        String levelNone = getString(R.string.level_none);
        double BOO = 0, SDD = 0.1;
        double rateNone = 1.2, rateEasy = 1.375, rateMedium = 1.4625, rateHard = 1.55,
                rateUpHard = 1.6375, rateSuper = 1.725, rateUpSuper = 1.9;
        double weight = Double.parseDouble(edtWeight.getText().toString()), height = Double.parseDouble(edtHeight.getText().toString());
        int age = Integer.parseInt(edtAge.getText().toString()), maxWater;
        double forCountUpLine = 300, forCountDownLine = 500;
        double fat, protein, carbohydrate;


        switch (rgFemaleOrMale.getCheckedRadioButtonId()) {
            case R.id.rdSpkFemale:
                BOO = (9.99 * weight + 6.25 * height - 4.92 * age - 161) * 1.1;
                isFemale = true;
                break;
            case R.id.rdSpkMale:
                BOO = (9.99 * weight + 6.25 * height - 4.92 * age + 5) * 1.1;
                isFemale = false;
                break;
        }

        /*Check level load*/
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_none))) {
            SPK = BOO * rateNone;
        }
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_easy))) {
            SPK = BOO * rateEasy;
        }
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_medium))) {
            SPK = BOO * rateMedium;
        }
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_hard))) {
            SPK = BOO * rateHard;
        }
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_up_hard))) {
            SPK = BOO * rateUpHard;
        }
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_super))) {
            SPK = BOO * rateSuper;
        }
        if (btnDifLevel.getText().toString().equals(getString(R.string.level_up_super))) {
            SPK = BOO * rateUpSuper;
        }

        upLineSPK = SPK - forCountUpLine;
        downLineSPK = SPK - forCountDownLine;

        fat = upLineSPK * 0.2 / 9;
        protein = upLineSPK * 0.3 / 4;
        carbohydrate = upLineSPK * 0.5 / 3.75;

        if (isFemale) {
            maxWater = WATER_ON_KG_FEMALE * (int) weight;
        } else {
            maxWater = WATER_ON_KG_MALE * (int) weight;
        }

        Log.e("LOl", String.valueOf(SPK));


        Profile profile = new Profile(SpkName, SpkSecondName,
                isFemale, age, Integer.parseInt(edtHeight.getText().toString()), weight, 0,
                btnDifLevel.getText().toString(),urlOfPhoto, maxWater, 0, (int) protein,
                (int) fat, (int) carbohydrate, dif_level, day, month, year);


        if (dif_level.equals(getString(R.string.dif_level_easy))) {
            saveProfile(registration, profile, SPK);
            Toast.makeText(ActivityEditProfile.this, R.string.profile_saved, Toast.LENGTH_SHORT).show();

        } else if (dif_level.equals(getString(R.string.dif_level_normal))){
            saveProfile(registration, profile, upLineSPK);
            Toast.makeText(ActivityEditProfile.this, R.string.profile_saved, Toast.LENGTH_SHORT).show();

        }else if (dif_level.equals(getString(R.string.dif_level_hard))) {
            saveProfile(registration, profile, downLineSPK);
            Toast.makeText(ActivityEditProfile.this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
        }


    }

    private void saveProfile(boolean registration, Profile profile, double maxInt){
        if (registration){
            profile.setMaxKcal((int) maxInt);
            Intent intent = new Intent(ActivityEditProfile.this, ActivityAuthMain.class);
            intent.putExtra("createUser", true);
            intent.putExtra(Config.INTENT_PROFILE, profile);
            Amplitude.getInstance().logEvent(Config.FILL_PROFILE);
            Adjust.trackEvent(new AdjustEvent(EventsAdjust.fill_reg_data));
            Amplitude.getInstance().logEvent(AmplitudaEvents.fill_reg_data);
            startActivity(intent);
        }else {
            profile.setMaxKcal((int) maxInt);
            WorkWithFirebaseDB.putProfileValue(profile);
            finish();
        }
    }

    private void createAlertDialogAboutLevelLoad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.alert_dialog_level, null);
        final RadioGroup rgLevelLoad = view.findViewById(R.id.rgLevelLoad);
        builder.setView(view);
        builder.setPositiveButton("ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (rgLevelLoad.getCheckedRadioButtonId() != -1) {
                    RadioButton radioButton = view.findViewById(rgLevelLoad.getCheckedRadioButtonId());
                    btnChoiseLevel.setText(radioButton.getText());
                }
            }
        });
        builder.setNeutralButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}
