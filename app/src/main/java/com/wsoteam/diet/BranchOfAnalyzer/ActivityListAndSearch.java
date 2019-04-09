package com.wsoteam.diet.BranchOfAnalyzer;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.POJOFoodItem.DbAnalyzer;
import com.wsoteam.diet.POJOFoodItem.FoodConnect;
import com.wsoteam.diet.POJOFoodItem.FoodItem;
import com.wsoteam.diet.POJOFoodItem.ListOfFoodItem;
import com.wsoteam.diet.POJOFoodItem.ListOfGroupsOfFood;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityListAndSearch extends AppCompatActivity {
    @BindView(R.id.spnEatingList) Spinner spinner;
    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    @BindView(R.id.ivActivityListAndSearchEmptyImage) ImageView ivEmptyImage;
    @BindView(R.id.tvActivityListAndSearchEmptyText) TextView tvEmptyText;
    @BindView(R.id.fabSearchAddNewProduct) FloatingActionButton fabSearchAddNewProduct;


    private ArrayList<FoodItem> listOfGroupsFoods = new ArrayList<>();
    private ArrayList<FoodItem> tempListOfGroupsFoods = new ArrayList<>();
    private final int HARD_KCAL = 500;
    private DbAnalyzer dbAnalyzerGlobal = new DbAnalyzer();
    private final String EMPTY = "";
    private final String TAG_OWN_PRODUCT = "OWN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_search);
        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.eatingList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fabSearchAddNewProduct.setVisibility(View.GONE);

        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(ActivityListAndSearch.this));
        AsyncLoadFoodList asyncLoadFoodList = new AsyncLoadFoodList();
        asyncLoadFoodList.execute();

        edtSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (ivEmptyImage.getVisibility() == View.VISIBLE) {
                    ivEmptyImage.setVisibility(View.GONE);
                    tvEmptyText.setVisibility(View.GONE);
                }
                searchAndShowList(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        YandexMetrica.reportEvent("Открыт экран: Анализатор");
        Adjust.trackEvent(new AdjustEvent(EventsAdjust.attempt_add_food));
        Adjust.trackEvent(new AdjustEvent(EventsAdjust.view_search_food));
        Amplitude.getInstance().logEvent(AmplitudaEvents.attempt_add_food);
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_search_food);

    }

    private void searchAndShowList(CharSequence text) {
        tempListOfGroupsFoods = new ArrayList<>();
        for (int j = 0; j < listOfGroupsFoods.size(); j++) {
            if (listOfGroupsFoods.get(j).getName().contains(text)
                    || (listOfGroupsFoods.get(j).getName()).contains(text.toString().substring(0, 1).toUpperCase()
                    + text.toString().substring(1))) {
                tempListOfGroupsFoods.add(listOfGroupsFoods.get(j));
            }
        }
        rvListOfSearchResponse.setAdapter(new ItemAdapter(tempListOfGroupsFoods));
    }


    private ArrayList<FoodItem> fillItemsList(List<ListOfGroupsOfFood> listOfGroups) {
        ArrayList<FoodItem> items = new ArrayList<>();
        for (int i = 0; i < listOfGroups.size(); i++) {
            FoodItem itemOfGlobalBaseForWriting;
            for (int j = 0; j < listOfGroups.get(i).getListOfFoodItems().size(); j++) {
                itemOfGlobalBaseForWriting = new FoodItem(listOfGroups.get(i).getListOfFoodItems().get(j).getCalories()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getCarbohydrates()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getComposition()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getDescription()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getFat()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getName()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getProperties()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getProtein()
                        , listOfGroups.get(i).getListOfFoodItems().get(j).getUrlOfImages()
                        , listOfGroups.get(i).getName()
                        , listOfGroups.get(i).getListOfFoodItems().size());
                items.add(itemOfGlobalBaseForWriting);
            }

        }
        return items;
    }

    @OnClick({R.id.ibActivityListAndSearchCollapsingCancelButton, R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibActivityListAndSearchCollapsingCancelButton:
                edtSearchField.setText("");
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvCal, tvNameOfGroup, tvLeterOfProduct;
        private ImageView ivMainImage, ivHardKcal, ivLockStatus;

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_rv_list_of_search_response, viewGroup, false));
            tvName = itemView.findViewById(R.id.tvName);
            tvCal = itemView.findViewById(R.id.tvCal);
            ivMainImage = itemView.findViewById(R.id.ivImage);
            tvNameOfGroup = itemView.findViewById(R.id.tvNameOfGroup);
            ivHardKcal = itemView.findViewById(R.id.ivHardKcal);
            ivLockStatus = itemView.findViewById(R.id.ivLockStatus);
            tvLeterOfProduct = itemView.findViewById(R.id.tvLeterOfProduct);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ActivityListAndSearch.this, ActivityDetailOfFood.class);
            intent.putExtra("ActivityDetailOfFood", tempListOfGroupsFoods.get(getAdapterPosition()));
            intent.putExtra(Config.TAG_CHOISE_EATING, getIntent().getStringExtra(Config.TAG_CHOISE_EATING));
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
            startActivity(intent);
        }

        public void bind(FoodItem itemOfGlobalBase, boolean isItemForSeparator) {
            ivHardKcal.setVisibility(View.GONE);
            ivLockStatus.setVisibility(View.GONE);
            tvLeterOfProduct.setVisibility(View.GONE);
            tvName.setText(itemOfGlobalBase.getName());
            tvCal.setText(itemOfGlobalBase.getCalories() + " " + getString(R.string.for_100_g_of_product));

            if (!itemOfGlobalBase.getNameOfGroup().equals(TAG_OWN_PRODUCT)) {
                tvNameOfGroup.setText(itemOfGlobalBase.getNameOfGroup());
                Glide.with(ActivityListAndSearch.this).load(itemOfGlobalBase.getUrlOfImages()).into(ivMainImage);
            } else {
                tvLeterOfProduct.setVisibility(View.VISIBLE);
                tvNameOfGroup.setText("Сохраненные");
                tvLeterOfProduct.setText(String.valueOf(Character.toUpperCase(itemOfGlobalBase.getName().charAt(0))));
                Glide.with(ActivityListAndSearch.this).load(R.drawable.gradient_for_background_splash).into(ivMainImage);
            }

            if (Integer.parseInt(itemOfGlobalBase.getCalories()) > HARD_KCAL) {
                ivHardKcal.setVisibility(View.VISIBLE);
            }

        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        ArrayList<FoodItem> itemsOfGlobalBases;

        public ItemAdapter(ArrayList<FoodItem> itemsOfGlobalBases) {
            this.itemsOfGlobalBases = itemsOfGlobalBases;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListAndSearch.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            if (itemsOfGlobalBases.get(position).getUrlOfImages().equals("0")) {
                holder.bind(itemsOfGlobalBases.get(position), true);
            } else {
                holder.bind(itemsOfGlobalBases.get(position), false);
            }

        }

        @Override
        public int getItemCount() {
            return itemsOfGlobalBases.size();
        }
    }

    private class AsyncLoadFoodList extends AsyncTask<Void, Void, DbAnalyzer> {
        @Override
        protected void onPostExecute(DbAnalyzer dbAnalyzer) {
            dbAnalyzerGlobal = dbAnalyzer;
            listOfGroupsFoods = fillItemsList(dbAnalyzer.getListOfGroupsOfFood());
        }

        @Override
        protected DbAnalyzer doInBackground(Void... voids) {
            String json;
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<FoodConnect> jsonAdapter = moshi.adapter(FoodConnect.class);
            try {
                InputStream inputStream = getAssets().open("food_list.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                Log.e("LOL", String.valueOf(size));
                inputStream.read(buffer);
                inputStream.close();
                json = new String(buffer, "UTF-8");

                FoodConnect foodConnect = jsonAdapter.fromJson(json);

                DbAnalyzer dbAnalyzer = foodConnect.getDbAnalyzer();

                //load own products
                if (ListOfFoodItem.count(ListOfFoodItem.class) > 0) {
                    ArrayList<ListOfFoodItem> listOfFoodItem = (ArrayList) ListOfFoodItem.listAll(ListOfFoodItem.class);
                    ListOfGroupsOfFood savedGroupFood = new ListOfGroupsOfFood(listOfFoodItem, TAG_OWN_PRODUCT, TAG_OWN_PRODUCT);
                    dbAnalyzer.getListOfGroupsOfFood().add(0, savedGroupFood);
                }

                //bk, kfc, mcDonalds and child food move to last position
                return shuffleWithNewPriority(dbAnalyzer);
            } catch (Exception e) {

            }
            return null;
        }

        private DbAnalyzer shuffleWithNewPriority(DbAnalyzer dbAnalyzer) {
            int BK = 0, KFC = 1, MCDonalds = 2, childEat = 4;

            dbAnalyzer.getListOfGroupsOfFood().add(dbAnalyzer.getListOfGroupsOfFood().get(BK));
            dbAnalyzer.getListOfGroupsOfFood().add(dbAnalyzer.getListOfGroupsOfFood().get(KFC));
            dbAnalyzer.getListOfGroupsOfFood().add(dbAnalyzer.getListOfGroupsOfFood().get(MCDonalds));
            dbAnalyzer.getListOfGroupsOfFood().add(dbAnalyzer.getListOfGroupsOfFood().get(childEat));

            dbAnalyzer.getListOfGroupsOfFood().remove(childEat);
            dbAnalyzer.getListOfGroupsOfFood().remove(MCDonalds);
            dbAnalyzer.getListOfGroupsOfFood().remove(KFC);
            dbAnalyzer.getListOfGroupsOfFood().remove(BK);

            return dbAnalyzer;
        }
    }

    private void createADAboutAddNewProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.alert_dialog_add_new_product, null);

        EditText edtADAddNewProductName = view.findViewById(R.id.edtADAddNewProductName);

        EditText edtADAddNewProductWeight = view.findViewById(R.id.edtADAddNewProductWeight);
        EditText edtADAddNewProductKcal = view.findViewById(R.id.edtADAddNewProductKcal);

        EditText edtADAddNewProductProt = view.findViewById(R.id.edtADAddNewProductProt);
        EditText edtADAddNewProductCarbo = view.findViewById(R.id.edtADAddNewProductCarbo);
        EditText edtADAddNewProductFat = view.findViewById(R.id.edtADAddNewProductFat);


        Button btnADAddNewProductCancel = view.findViewById(R.id.btnADAddNewProductCancel);
        Button btnADAddNewProductSave = view.findViewById(R.id.btnADAddNewProductSave);


        btnADAddNewProductCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnADAddNewProductSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtADAddNewProductName.getText().toString().equals("")
                        && !edtADAddNewProductWeight.getText().toString().equals("")
                        && !edtADAddNewProductKcal.getText().toString().equals("")) {

                    saveNewProduct(edtADAddNewProductName.getText().toString(),
                            edtADAddNewProductWeight.getText().toString(), edtADAddNewProductKcal.getText().toString(),
                            edtADAddNewProductProt.getText().toString(), edtADAddNewProductCarbo.getText().toString(),
                            edtADAddNewProductFat.getText().toString());

                    alertDialog.cancel();

                } else {
                    Toast.makeText(ActivityListAndSearch.this, "Для сохранение заполните три обязательных первых поля", Toast.LENGTH_LONG).show();
                }
            }

        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void saveNewProduct(String name, String weight, String kcal, String prot, String carbo, String fat) {
        double protInPortion = 0, carboInPortion = 0, fatInPortion = 0;

        double coefficient = (Integer.parseInt(weight) / 100);
        double kcalInPortion = Integer.parseInt(kcal) / coefficient;
        if (!prot.equals("")) {
            protInPortion = Integer.parseInt(prot) / coefficient;
        }
        if (!carbo.equals("")) {
            carboInPortion = Integer.parseInt(carbo) / coefficient;
        }
        if (!fat.equals("")) {
            fatInPortion = Integer.parseInt(fat) / coefficient;
        }

        ListOfFoodItem item = new ListOfFoodItem();
        item.setName(name);
        item.setCalories(String.valueOf((int) kcalInPortion));
        item.setUrlOfImages(TAG_OWN_PRODUCT);

        item.setProtein(String.valueOf((int) protInPortion));
        item.setCarbohydrates(String.valueOf((int) carboInPortion));
        item.setFat(String.valueOf((int) fatInPortion));

        listOfGroupsFoods.add(0, new FoodItem(String.valueOf((int) kcalInPortion),
                String.valueOf((int) carboInPortion), EMPTY, EMPTY, String.valueOf((int) fatInPortion),
                name, EMPTY, String.valueOf((int) protInPortion), EMPTY, TAG_OWN_PRODUCT, 0));

        item.save();

        Intent intent = new Intent(ActivityListAndSearch.this, ActivityDetailOfFood.class);
        intent.putExtra("ActivityDetailOfFood", listOfGroupsFoods.get(0));
        intent.putExtra(Config.TAG_CHOISE_EATING, getIntent().getStringExtra(Config.TAG_CHOISE_EATING));
        startActivity(intent);

    }
}

