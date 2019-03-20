package com.wsoteam.diet.BranchOfDiary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.WeightDiaryObject;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ActivityAddData extends AppCompatActivity {
    private FloatingActionButton fabSaveData;
    private EditText edtWeight, edtChest, edtWaist, edtHips;
    private WeightDiaryObject diaryData;
    private DatePicker datePicker;
    private int maxDay, maxMonth, maxYear;
    private boolean isReadyToClose = false;
    InterstitialAd interstitialAd;

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
        setContentView(R.layout.activity_add_data);
        fabSaveData = findViewById(R.id.fabSaveInputDataDiary);
        edtWeight = findViewById(R.id.edtInputDataDiaryWeight);
        edtChest = findViewById(R.id.edtInputDataDiaryChest);
        edtWaist = findViewById(R.id.edtInputDataDiaryWaist);
        edtHips = findViewById(R.id.edtInputDataDiaryHips);
        datePicker = findViewById(R.id.datePicker);
        diaryData = new WeightDiaryObject();

        maxDay = datePicker.getDayOfMonth();
        maxMonth = datePicker.getMonth();
        maxYear = datePicker.getYear();


        fabSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDate()) {
                    getWeight();
                    getOtherData();
                    saveWeightDiaryItem();
                    Amplitude.getInstance().logEvent(Config.SAVE_DIARY_WEIGHT);
                    if (isReadyToClose) {
                        finish();
                    }
                }
            }
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Добавление данных в дневник");

    }

    private void saveWeightDiaryItem() {
        String keyOfReplaceObject;
        if (UserDataHolder.getUserData() != null
                && UserDataHolder.getUserData().getDiaryDataList() != null
                && (keyOfReplaceObject = findSameObject(diaryData)) != null){
            WorkWithFirebaseDB.replaceWeightDiaryItem(diaryData, keyOfReplaceObject);
        }else{
            WorkWithFirebaseDB.addWeightDiaryItem(diaryData);
        }

    }

    private String findSameObject(WeightDiaryObject weightDiaryObject) {
        Iterator iterator = UserDataHolder.getUserData().getDiaryDataList().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            WeightDiaryObject diaryData = (WeightDiaryObject) pair.getValue();
            if(weightDiaryObject.getOwnId() == diaryData.getOwnId()){
                return (String) pair.getKey();
            }
        }
        return null;
    }


    private void getOtherData() {
        if (!edtChest.getText().toString().equals("")) {
            diaryData.setChest(Integer.parseInt(edtChest.getText().toString()));
        }
        if (!edtWaist.getText().toString().equals("")) {
            diaryData.setWaist(Integer.parseInt(edtWaist.getText().toString()));
        }
        if (!edtHips.getText().toString().equals("")) {
            diaryData.setHips(Integer.parseInt(edtHips.getText().toString()));
        }
    }

    private boolean getDate() {
        if (maxDay < datePicker.getDayOfMonth() && maxMonth <= datePicker.getMonth() && maxYear <= datePicker.getYear()) {
            Toast.makeText(this, "Неправильно введена дата", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            int weightOfDate = datePicker.getDayOfMonth() + datePicker.getMonth() * 100 + datePicker.getYear() * 2000;
            diaryData.setNumberOfDay(datePicker.getDayOfMonth());
            diaryData.setMonth(datePicker.getMonth());
            diaryData.setNameOfMonth(getNameOfMonth(datePicker.getMonth()));
            diaryData.setYear(datePicker.getYear());
            diaryData.setOwnId(weightOfDate);
            return true;
        }
    }

    private void getWeight() {
        if (edtWeight.getText().toString().equals("")) {
            Toast.makeText(this, "Введите пожалуйста Ваш вес", Toast.LENGTH_SHORT).show();
        } else {
            diaryData.setWeight(Double.parseDouble(edtWeight.getText().toString()));
            isReadyToClose = true;
        }
    }

    public String getNameOfMonth(int numberOfMonth) {
        String nameOfMonth = new String();
        switch (numberOfMonth) {
            case 0:
                nameOfMonth = "январь";
                break;
            case 1:
                nameOfMonth = "февраль";
                break;
            case 2:
                nameOfMonth = "март";
                break;
            case 3:
                nameOfMonth = "апрель";
                break;
            case 4:
                nameOfMonth = "май";
                break;
            case 5:
                nameOfMonth = "июнь";
                break;
            case 6:
                nameOfMonth = "июль";
                break;
            case 7:
                nameOfMonth = "август";
                break;
            case 8:
                nameOfMonth = "сентябрь";
                break;
            case 9:
                nameOfMonth = "октябрь";
                break;
            case 10:
                nameOfMonth = "ноябрь";
                break;
            case 11:
                nameOfMonth = "декабрь";
                break;
        }
        return nameOfMonth;
    }
}
