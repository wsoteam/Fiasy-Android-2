package com.wsoteam.diet.BranchOfDiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOForDB.DiaryData;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.WeightDiaryObject;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActivityListOfDiary extends AppCompatActivity {
    private FloatingActionButton fabAddData;
    private RecyclerView recyclerView;
    private ArrayList<WeightDiaryObject> diaryDataArrayList = new ArrayList<>();
    private GraphView graphView;
    private InterstitialAd interstitialAd;
    private SharedPreferences isRewrite;
    private LinearLayout llEmptyStateLayout;
    private final int WATER_ON_KG_FEMALE = 30;
    private final int WATER_ON_KG_MALE = 40;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
        drawGraphs();
    }

    private void writeFromSqlToFB() {
        if (DiaryData.listAll(DiaryData.class).size() != 0) {
            List<DiaryData> oldDiaryDataList = DiaryData.listAll(DiaryData.class);
            for (int i = 0; i < oldDiaryDataList.size(); i++) {
                WeightDiaryObject newDiaryDataObject = new WeightDiaryObject();
                newDiaryDataObject.setOwnId(oldDiaryDataList.get(i).getOwnId());
                newDiaryDataObject.setNumberOfDay(oldDiaryDataList.get(i).getNumberOfDay());
                newDiaryDataObject.setMonth(oldDiaryDataList.get(i).getMonth());
                newDiaryDataObject.setYear(oldDiaryDataList.get(i).getYear());
                newDiaryDataObject.setWeight(oldDiaryDataList.get(i).getWeight());
                newDiaryDataObject.setChest(oldDiaryDataList.get(i).getChest());
                newDiaryDataObject.setWaist(oldDiaryDataList.get(i).getWaist());
                newDiaryDataObject.setHips(oldDiaryDataList.get(i).getHips());
                newDiaryDataObject.setNameOfMonth(oldDiaryDataList.get(i).getNameOfMonth());
                WorkWithFirebaseDB.addWeightDiaryItem(newDiaryDataObject);
            }
        }
        DiaryData.deleteAll(DiaryData.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_diary);
        fabAddData = findViewById(R.id.fabAddDataListOfDiary);
        recyclerView = findViewById(R.id.rvListOfDiary);
        llEmptyStateLayout = findViewById(R.id.llEmptyStateLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //writeFromSqlToFB();

        fabAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListOfDiary.this, ActivityAddData.class);
                startActivity(intent);
            }
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Данные дневника");
    }

    private void updateUI() {
        if (UserDataHolder.getUserData().getDiaryDataList() == null) {
            llEmptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            diaryDataArrayList = getListOfDiaryData();
            llEmptyStateLayout.setVisibility(View.GONE);
            bubbleSort();
            recyclerView.setAdapter(new ItemAdapter(diaryDataArrayList));


            if (UserDataHolder.getUserData().getProfile() != null) {
                Profile profile = UserDataHolder.getUserData().getProfile();
                // if the day when the profile was created earlier than
                // the last entry in the diary or equal in weight and day of profile creation
                if ((profile.getNumberOfDay() < diaryDataArrayList.get(0).getNumberOfDay()
                        && profile.getMonth() <= diaryDataArrayList.get(0).getMonth()
                        && profile.getYear() <= diaryDataArrayList.get(0).getYear())
                        || (profile.getNumberOfDay() == diaryDataArrayList.get(0).getNumberOfDay()
                        && profile.getMonth() <= diaryDataArrayList.get(0).getMonth()
                        && profile.getYear() <= diaryDataArrayList.get(0).getYear()
                        && profile.getWeight() != diaryDataArrayList.get(0).getWeight())) {

                    isRewrite = getSharedPreferences(Config.TAG_OF_REWRITE, MODE_PRIVATE);

                    if (isRewrite.getInt(Config.TAG_OF_REWRITE, Config.NOT_ENTER_EARLY) == Config.NOT_ENTER_EARLY) {
                        createADAboutRewriteProfile(isRewrite.edit());
                    }

                    if (isRewrite.getInt(Config.TAG_OF_REWRITE, Config.NOT_REWRITE_PROFILE) == Config.REWRITE_PROFILE) {
                        profile.setNumberOfDay(diaryDataArrayList.get(0).getNumberOfDay());
                        profile.setMonth(diaryDataArrayList.get(0).getMonth());
                        profile.setYear(diaryDataArrayList.get(0).getYear());
                        profile = updateProfile(profile, diaryDataArrayList.get(0).getWeight());
                        showToastAfterReWrite();
                    }
                }

                profile.setLoseWeight(profile.getWeight() - diaryDataArrayList.get(diaryDataArrayList.size() - 1).getWeight());

                WorkWithFirebaseDB.putProfileValue(profile);
            }
        }

    }

    private ArrayList<WeightDiaryObject> getListOfDiaryData() {
        ArrayList<WeightDiaryObject> diaryDataArrayList = new ArrayList<>();
        Iterator iterator = UserDataHolder.getUserData().getDiaryDataList().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            WeightDiaryObject diaryData = (WeightDiaryObject) pair.getValue();
            diaryDataArrayList.add(diaryData);
        }
        return diaryDataArrayList;
    }

    //Repeat calculate from edit profile. Re - calculate SPK
    private Profile updateProfile(Profile profile, double currentWeight) {

        String levelNone = getString(R.string.level_none);
        double BOO = 0, SDD = 0.1, SPK = 0, upLineSPK = 0, downLineSPK = 0;
        double rateNone = 1.2, rateEasy = 1.375, rateMedium = 1.4625, rateHard = 1.55,
                rateUpHard = 1.6375, rateSuper = 1.725, rateUpSuper = 1.9;
        double forCountUpLine = 300, forCountDownLine = 500;
        double fat, protein, carbohydrate;
        int maxWater;

        if (profile.isFemale()) {
            BOO = (9.99 * currentWeight + 6.25 * profile.getHeight() - 4.92 * profile.getAge() - 161) * 1.1;
            maxWater = WATER_ON_KG_FEMALE * (int) currentWeight;
        } else {
            BOO = (9.99 * currentWeight + 6.25 * profile.getHeight() - 4.92 * profile.getAge() + 5) * 1.1;
            maxWater = WATER_ON_KG_MALE * (int) currentWeight;
        }

        /*Check level load*/
        if (profile.getExerciseStress().equals(getString(R.string.level_none))) {
            SPK = BOO * rateNone;
        }
        if (profile.getExerciseStress().toString().equals(getString(R.string.level_easy))) {
            SPK = BOO * rateEasy;
        }
        if (profile.getExerciseStress().toString().equals(getString(R.string.level_medium))) {
            SPK = BOO * rateMedium;
        }
        if (profile.getExerciseStress().toString().equals(getString(R.string.level_hard))) {
            SPK = BOO * rateHard;
        }
        if (profile.getExerciseStress().toString().equals(getString(R.string.level_up_hard))) {
            SPK = BOO * rateUpHard;
        }
        if (profile.getExerciseStress().toString().equals(getString(R.string.level_super))) {
            SPK = BOO * rateSuper;
        }
        if (profile.getExerciseStress().toString().equals(getString(R.string.level_up_super))) {
            SPK = BOO * rateUpSuper;
        }

        upLineSPK = SPK - forCountUpLine;
        downLineSPK = SPK - forCountDownLine;

        fat = upLineSPK * 0.2 / 9;
        protein = upLineSPK * 0.3 / 4;
        carbohydrate = upLineSPK * 0.5 / 3.75;


        profile.setWaterCount(maxWater);
        profile.setWeight(currentWeight);
        profile.setMaxFat((int) fat);
        profile.setMaxProt((int) protein);
        profile.setMaxCarbo((int) carbohydrate);

        if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_easy))) {
            profile.setMaxKcal((int) SPK);
        } else {
            if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_normal))) {
                profile.setMaxKcal((int) upLineSPK);
            } else {
                profile.setMaxKcal((int) downLineSPK);
            }
        }

        return profile;
    }

    private void showToastAfterReWrite() {
        LayoutInflater toastInflater = getLayoutInflater();
        View toastLayout = toastInflater.inflate(R.layout.toast_complete_gift, null, false);
        TextView tvToastCompleteGift = toastLayout.findViewById(R.id.tvToastCompleteGift);
        ImageView ivToastCompleteGift = toastLayout.findViewById(R.id.ivToastCompleteGift);

        tvToastCompleteGift.setText("Величина СПК обновлена");
        Glide.with(this).load(R.drawable.ic_refresh_profile).into(ivToastCompleteGift);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    private void createADAboutRewriteProfile(SharedPreferences.Editor editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.alert_dialog_rewrite_profile_data, null);
        Button btnADRefreshProfileCancel = view.findViewById(R.id.btnADRefreshProfileCancel);
        Button btnADRefreshProfileOk = view.findViewById(R.id.btnADRefreshProfileOk);

        btnADRefreshProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(Config.TAG_OF_REWRITE, Config.NOT_REWRITE_PROFILE).commit();
                alertDialog.cancel();
            }
        });

        btnADRefreshProfileOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(Config.TAG_OF_REWRITE, Config.REWRITE_PROFILE).commit();
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void drawGraphs() {
        graphView = findViewById(R.id.gvGraphOfWeight);
        graphView.removeAllSeries();
        graphView.getLegendRenderer().resetStyles();
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(150);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(diaryDataArrayList.size());


        DataPoint[] weightPoints = new DataPoint[diaryDataArrayList.size()];
        ArrayList<DataPoint> chestPointsNotSortedArray = new ArrayList<>();
        ArrayList<DataPoint> hipsPointsNotSortedArray = new ArrayList<>();
        ArrayList<DataPoint> waistPointsNotSortedArray = new ArrayList<>();
        for (int i = diaryDataArrayList.size() - 1, j = 0; i >= 0; i--, j++) {
            int weight = (int) diaryDataArrayList.get(i).getWeight();
            weightPoints[j] = new DataPoint(j, weight); // fill from end to start
            if (diaryDataArrayList.get(i).getChest() != 0) {
                double temp = diaryDataArrayList.get(i).getChest();
                //Toast.makeText(this, String.valueOf(diaryDataArrayList.get(i).getChest()), Toast.LENGTH_SHORT).show();
                chestPointsNotSortedArray.add(new DataPoint(j, temp));
            }
            if (diaryDataArrayList.get(i).getHips() != 0) {
                hipsPointsNotSortedArray.add(new DataPoint(j, diaryDataArrayList.get(i).getHips()));
            }
            if (diaryDataArrayList.get(i).getWaist() != 0) {
                waistPointsNotSortedArray.add(new DataPoint(j, diaryDataArrayList.get(i).getWaist()));
            }
        }
        String test = "";

        DataPoint[] chestPoints = new DataPoint[chestPointsNotSortedArray.size()];
        for (int i = 0; i < chestPoints.length; i++) {
            chestPoints[i] = chestPointsNotSortedArray.get(i);

        }
        DataPoint[] hipsPoints = new DataPoint[hipsPointsNotSortedArray.size()];
        for (int i = 0; i < hipsPoints.length; i++) {
            hipsPoints[i] = hipsPointsNotSortedArray.get(i);
        }
        DataPoint[] waistPoints = new DataPoint[waistPointsNotSortedArray.size()];
        for (int i = 0; i < waistPoints.length; i++) {
            waistPoints[i] = waistPointsNotSortedArray.get(i);
        }


        if (chestPoints.length > 1) {
            LineGraphSeries<DataPoint> chestLine = new LineGraphSeries<>(chestPoints);
            //chestLine.setDrawDataPoints(true);
            chestLine.setColor(getResources().getColor(R.color.red));
            //chestLine.setDataPointsRadius(12);
            chestLine.setThickness(10);
            chestLine.setTitle("Грудь");

            graphView.addSeries(chestLine);
        }


        if (hipsPoints.length > 1) {
            LineGraphSeries<DataPoint> hipsLine = new LineGraphSeries<>(hipsPoints);
            //hipsLine.setDrawDataPoints(true);
            hipsLine.setColor(getResources().getColor(R.color.yellow));
            //hipsLine.setDataPointsRadius(12);
            hipsLine.setThickness(10);
            hipsLine.setTitle("Бедра");

            graphView.addSeries(hipsLine);
        }

        if (waistPoints.length > 1) {
            LineGraphSeries<DataPoint> waistLine = new LineGraphSeries<>(waistPoints);
            //waistLine.setDrawDataPoints(true);
            waistLine.setColor(getResources().getColor(R.color.blue));
            //waistLine.setDataPointsRadius(12);
            waistLine.setThickness(10);
            waistLine.setTitle("Талия");

            graphView.addSeries(waistLine);
        }


        if (weightPoints.length != 0) {
            LineGraphSeries<DataPoint> weightLine = new LineGraphSeries<>(weightPoints);
            //weightLine.setDrawDataPoints(true);
            weightLine.setColor(getResources().getColor(R.color.light_green));
            //weightLine.setDataPointsRadius(12);
            weightLine.setThickness(10);
            weightLine.setTitle("Вес");

            graphView.addSeries(weightLine);
        }

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setTextSize(25);
        graphView.getLegendRenderer().setTextColor(getResources().getColor(R.color.white));
        graphView.getLegendRenderer().setFixedPosition(50, 0);


    }


    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvDay, tvMonth, tvWeight, tvSubWeight, tvChest, tvWaist, tvHips, tvSubChest, tvSubWaist, tvSubHips;
        private ImageView ivLeftColor;

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_diary_data, viewGroup, false));
            tvDay = itemView.findViewById(R.id.tvItemDiaryDataNumber);
            tvMonth = itemView.findViewById(R.id.tvItemDiaryDataMonth);
            tvWeight = itemView.findViewById(R.id.tvItemDiaryDataWeight);
            tvSubWeight = itemView.findViewById(R.id.tvItemDiaryDataSubWeight);
            tvChest = itemView.findViewById(R.id.tvItemDiaryDataChestCount);
            tvWaist = itemView.findViewById(R.id.tvItemDiaryDataWaistCount);
            tvHips = itemView.findViewById(R.id.tvItemDiaryDataHipsCount);
            ivLeftColor = itemView.findViewById(R.id.ivColorAboutWeight);
            tvSubChest = itemView.findViewById(R.id.tvItemDiaryDataChestCountSub);
            tvSubWaist = itemView.findViewById(R.id.tvItemDiaryDataWaistCountSub);
            tvSubHips = itemView.findViewById(R.id.tvItemDiaryDataHipsCountSub);
        }

        public void bind(WeightDiaryObject currentDiaryData, WeightDiaryObject previousDiaryData) {
            tvDay.setText(String.valueOf(currentDiaryData.getNumberOfDay()));
            tvMonth.setText(currentDiaryData.getNameOfMonth());
            tvWeight.setText(String.valueOf(currentDiaryData.getWeight()) + " кг");

            if (currentDiaryData.getChest() != 0) {
                tvChest.setText(String.valueOf(currentDiaryData.getChest()));
            } else {
                tvChest.setText("--");
            }
            if (currentDiaryData.getHips() != 0) {
                tvHips.setText(String.valueOf(currentDiaryData.getHips()));
            } else {
                tvHips.setText("--");
            }
            if (currentDiaryData.getWaist() != 0) {
                tvWaist.setText(String.valueOf(currentDiaryData.getWaist()));
            } else {
                tvWaist.setText("--");
            }

            if (previousDiaryData.getOwnId() == 0) {
                ivLeftColor.setColorFilter(getResources().getColor(R.color.yellow));
                tvSubWeight.setText("--");
            } else {
                if (previousDiaryData.getWeight() > currentDiaryData.getWeight()) {
                    double difference = previousDiaryData.getWeight() - currentDiaryData.getWeight();
                    ivLeftColor.setColorFilter(getResources().getColor(R.color.green));
                    tvSubWeight.setText("-" + String.valueOf(difference));
                    tvSubWeight.setTextColor(getResources().getColor(R.color.green));
                } else {
                    if (previousDiaryData.getWeight() == currentDiaryData.getWeight()) {
                        ivLeftColor.setColorFilter(getResources().getColor(R.color.yellow));
                        tvSubWeight.setText("0");
                        tvSubWeight.setTextColor(getResources().getColor(R.color.yellow));
                    } else {
                        double difference = currentDiaryData.getWeight() - previousDiaryData.getWeight();
                        ivLeftColor.setColorFilter(getResources().getColor(R.color.red));
                        tvSubWeight.setText("+" + String.valueOf(difference));
                        tvSubWeight.setTextColor(getResources().getColor(R.color.red));
                    }
                }

                if (previousDiaryData.getChest() != 0) {
                    toCompateTwoNumbers(tvSubChest, previousDiaryData.getChest(), currentDiaryData.getChest());
                } else {
                    tvSubChest.setText("");
                }
                if (previousDiaryData.getHips() != 0) {
                    toCompateTwoNumbers(tvSubHips, previousDiaryData.getHips(), currentDiaryData.getHips());
                } else {
                    tvSubHips.setText("");
                }
                if (previousDiaryData.getWaist() != 0) {
                    toCompateTwoNumbers(tvSubWaist, previousDiaryData.getWaist(), currentDiaryData.getWaist());
                } else {
                    tvSubWaist.setText("");
                }
            }


        }

        private void toCompateTwoNumbers(TextView currentSubTextView, int previousNumber, int currentNumber) {
            int different = 0;
            if (previousNumber > currentNumber) {
                different = previousNumber - currentNumber;
                currentSubTextView.setTextColor(getResources().getColor(R.color.green));
                currentSubTextView.setText("-" + String.valueOf(different));
            } else if (previousNumber == currentNumber) {
                currentSubTextView.setText("0");
                currentSubTextView.setTextColor(getResources().getColor(R.color.yellow));
            } else {
                different = currentNumber - previousNumber;
                currentSubTextView.setTextColor(getResources().getColor(R.color.red));
                currentSubTextView.setText("+" + String.valueOf(different));
            }
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        ArrayList<WeightDiaryObject> diaryDataArrayList;

        public ItemAdapter(ArrayList<WeightDiaryObject> diaryDataArrayList) {
            this.diaryDataArrayList = diaryDataArrayList;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListOfDiary.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            if (position + 1 != diaryDataArrayList.size()) {
                holder.bind(diaryDataArrayList.get(position), diaryDataArrayList.get(position + 1));
            } else {
                holder.bind(diaryDataArrayList.get(position), new WeightDiaryObject());
            }
        }

        @Override
        public int getItemCount() {
            return diaryDataArrayList.size();
        }

    }

    private void bubbleSort() {
        if (diaryDataArrayList.size() > 1) {
            WeightDiaryObject[] arrayForWrite = new WeightDiaryObject[diaryDataArrayList.size()];

            for (int i = 0; i < diaryDataArrayList.size(); i++) {
                arrayForWrite[i] = diaryDataArrayList.get(i);
            }

            int lenght = arrayForWrite.length;
            for (int i = 0; i < lenght - 1; i++) {
                for (int j = 0; j < lenght - i - 1; j++) {
                    if (arrayForWrite[j].getOwnId() < arrayForWrite[j + 1].getOwnId()) {
                        WeightDiaryObject temp = arrayForWrite[j];
                        arrayForWrite[j] = arrayForWrite[j + 1];
                        arrayForWrite[j + 1] = temp;
                    }
                }
            }
            diaryDataArrayList = new ArrayList<>();
            for (int i = 0; i < arrayForWrite.length; i++) {
                diaryDataArrayList.add(arrayForWrite[i]);
            }
        }

    }


}
