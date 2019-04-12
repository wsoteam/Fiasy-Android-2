package com.wsoteam.diet.BranchProfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.yandex.metrica.YandexMetrica;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityEditCompletedProfile extends AppCompatActivity {
    @BindView(R.id.edtName) EditText edtName;
    @BindView(R.id.edtSecondName) EditText edtSecondName;
    private EditText edtHeight, edtAge, edtWeight;
    private Button btnChoiseLevel;
    private RadioGroup rgFemaleOrMale;
    private Button btnSave;

    private AlertDialog alertDialogLevelLoad;
    private final int WATER_ON_KG_FEMALE = 30;
    private final int WATER_ON_KG_MALE = 40;
    private String urlOfPhoto = "default";
    private int day, month, year;

    private boolean isFemale = true;
    private double SPK = 0, upLineSPK = 0, downLineSPK = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_completed_profile);
        ButterKnife.bind(this);
        edtHeight = findViewById(R.id.edtSpkGrowth);
        edtAge = findViewById(R.id.edtSpkAge);
        edtWeight = findViewById(R.id.edtSpkWeight);
        btnChoiseLevel = findViewById(R.id.btnSpkChoiseLevel);
        rgFemaleOrMale = findViewById(R.id.rgFemaleOrMaleSpk);
        btnSave = findViewById(R.id.rectangle_8);


        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            fillViewsIfProfileNotNull();
        }


        btnChoiseLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogLevelLoad();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
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

        YandexMetrica.reportEvent("Открыт экран: Редактировать профиль");


    }

    private void fillViewsIfProfileNotNull() {
        Profile profile = UserDataHolder.getUserData().getProfile();

        edtHeight.setText(String.valueOf(profile.getHeight()));
        edtAge.setText(String.valueOf(profile.getAge()));
        edtWeight.setText(String.valueOf(profile.getWeight()));
        btnChoiseLevel.setText(profile.getExerciseStress());
        if (!profile.getFirstName().equals("default")) {
            edtName.setText(profile.getFirstName());
            edtSecondName.setText(profile.getLastName());
        }
        if (profile.isFemale()) {
            rgFemaleOrMale.check(R.id.rdSpkFemale);
        } else {
            rgFemaleOrMale.check(R.id.rdSpkMale);
        }

    }

    private boolean checkInputData() {
        if (!edtName.getText().equals("")) {
            if (!edtSecondName.getText().equals("")) {
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
                                Toast.makeText(ActivityEditCompletedProfile.this, R.string.spk_check_weight, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        } else {
                            Toast.makeText(ActivityEditCompletedProfile.this, R.string.spk_check_your_height, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(ActivityEditCompletedProfile.this, R.string.spk_check_your_age, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ActivityEditCompletedProfile.this, R.string.spk_choise_your_gender, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ActivityEditCompletedProfile.this, "Введите Вашу фамилию", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ActivityEditCompletedProfile.this, "Введите Ваше имя", Toast.LENGTH_SHORT).show();
            return false;
        }

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
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_none))) {
            SPK = BOO * rateNone;
        }
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_easy))) {
            SPK = BOO * rateEasy;
        }
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_medium))) {
            SPK = BOO * rateMedium;
        }
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_hard))) {
            SPK = BOO * rateHard;
        }
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_up_hard))) {
            SPK = BOO * rateUpHard;
        }
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_super))) {
            SPK = BOO * rateSuper;
        }
        if (btnChoiseLevel.getText().toString().equals(getString(R.string.level_up_super))) {
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


        Profile profile = new Profile(edtName.getText().toString(), edtSecondName.getText().toString(),
                isFemale, age, Integer.parseInt(edtHeight.getText().toString()), weight, 0,
                btnChoiseLevel.getText().toString(), urlOfPhoto, maxWater, 0, (int) protein,
                (int) fat, (int) carbohydrate, btnChoiseLevel.getText().toString(), day, month, year);

        Log.e("LOL", profile.getExerciseStress());


        if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_easy))) {
            saveProfile(profile, SPK);
            Toast.makeText(ActivityEditCompletedProfile.this, R.string.profile_saved, Toast.LENGTH_SHORT).show();

        } else if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_normal))) {
            saveProfile(profile, upLineSPK);
            Toast.makeText(ActivityEditCompletedProfile.this, R.string.profile_saved, Toast.LENGTH_SHORT).show();

        } else if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_hard))) {
            saveProfile(profile, downLineSPK);
            Toast.makeText(ActivityEditCompletedProfile.this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
        }


    }

    private void saveProfile(Profile profile, double maxInt) {
        profile.setMaxKcal((int) maxInt);
        WorkWithFirebaseDB.putProfileValue(profile);
        finish();
    }

    private void createAlertDialogLevelLoad() {

        if (alertDialogLevelLoad != null) {
            alertDialogLevelLoad.show();
        } else {
            View.OnClickListener listener;

            final View view = View.inflate(this, R.layout.alert_dialog_level, null);
            final RadioGroup rgLevelLoad = view.findViewById(R.id.rgLevelLoad);
            final RadioButton radioButton = view.findViewById(R.id.rbLevelLoadNone);
            radioButton.setChecked(true);

            alertDialogLevelLoad = new AlertDialog.Builder(this)
                    .setView(view)
                    .show();


            listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton radioButton = view.findViewById(rgLevelLoad.getCheckedRadioButtonId());
                    btnChoiseLevel.setText(radioButton.getText());
                    alertDialogLevelLoad.dismiss();
                }
            };

            view.findViewById(R.id.rbLevelLoadNone).setOnClickListener(listener);
            view.findViewById(R.id.rbLevelLoadEasy).setOnClickListener(listener);
            view.findViewById(R.id.rbLevelLoadMedium).setOnClickListener(listener);
            view.findViewById(R.id.rbLevelLoadHard).setOnClickListener(listener);
            view.findViewById(R.id.rbLevelLoadUpHard).setOnClickListener(listener);
            view.findViewById(R.id.rbLevelLoadSuper).setOnClickListener(listener);
            view.findViewById(R.id.rbLevelLoadUpSuper).setOnClickListener(listener);

        }
    }
}
