package com.wsoteam.diet.presentation.food.template.create.search;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsoteam.diet.App;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplateHolder;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.adding.ActivityDetailFood;
import com.wsoteam.diet.Recipes.adding.ProductSearchActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchFoodActivity extends AppCompatActivity {

    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    @BindView(R.id.ivActivityListAndSearchEmptyImage) ImageView ivEmptyImage;
    @BindView(R.id.tvActivityListAndSearchEmptyText) TextView tvEmptyText;
    @BindView(R.id.tvIndex) TextView tvIndex;
    private AlertDialog dialog;
    private Window window;

    private int RESPONSE_LIMIT = 50;
    private ItemAdapter itemAdapter;
    private boolean isEqualsNext = true;
    private FoodDAO foodDAO = App.getInstance().getFoodDatabase().foodDAO();
    private final int ONE_WORD = 1, TWO_WORDS = 2, THREE_WORDS = 3, FOUR_WORDS = 4, FIVE_WORDS = 5;

    private List<Food> foodList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        ButterKnife.bind(this);

        window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#32000000"));

        updateUI();
        foodList = FoodTemplateHolder.get();



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
                isEqualsNext = true;
                search(charSequence.toString().replaceAll("\\s+", " "));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean checkFood(Food food){

        for (Food f:
             foodList) {
            if (f.getName().equals(food.getName())){
                return true;
            }
        }
        return false;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> commandList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            edtSearchField.setText(commandList.get(0));
        }
        if (requestCode == 45 && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Food food = (Food) bundle.get(Config.RECIPE_FOOD_INTENT);

               int position = getPosition(food);

               if (position < 0){
                   foodList.add(food);
               } else {
                   foodList.set(position, food);
               }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    int getPosition(Food food){
        for (Food f:
                foodList) {
            if (f.getName().equals(food.getName())){
                return  foodList.indexOf(f);
            }
        }
        return -1;
    }

    private void updateUI() {
        List<Food> foods = new ArrayList<>();
        itemAdapter = new ItemAdapter(foods);
        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(this));
        rvListOfSearchResponse.setAdapter(itemAdapter);
    }

    private void search(String searchString) {
        Single.fromCallable(() -> {
            List<Food> cFOODS = getFirstList(searchString);
            return cFOODS;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> refreshAdapter(t), Throwable::printStackTrace);
    }

    private void refreshAdapter(List<Food> t) {
        itemAdapter = new ItemAdapter(t);
        rvListOfSearchResponse.setAdapter(itemAdapter);
    }

    private List<Food> getFirstList(String searchString) {
        List<Food> foods = new ArrayList<>();
        foods.addAll(foodDAO.searchFullMatchWord(searchString, RESPONSE_LIMIT, 0));
        if (foods.size() < RESPONSE_LIMIT) {
            isEqualsNext = false;
            if (searchString.contains(" ") && searchString.split(" ").length > 1) {
                foods.addAll(searchMultiWords(searchString, foods.size()));
            } else {
                foods.addAll(foodDAO.searchOneWord("%" + searchString + "%", RESPONSE_LIMIT, foods.size()));
            }
        }
        return foods;
    }

    private List<Food> searchMultiWords(String searchPhrase, int offset) {
        List<Food> foods = new ArrayList<>();
        if (searchPhrase.split(" ").length == TWO_WORDS) {
            foods = foodDAO.searchTwoWord("%" + searchPhrase.split(" ")[0] + "%",
                    "%" + searchPhrase.split(" ")[1] + "%", RESPONSE_LIMIT, offset);
        } else if (searchPhrase.split(" ").length == THREE_WORDS) {
            foods = foodDAO.searchThreeWord("%" + searchPhrase.split(" ")[0] + "%",
                    "%" + searchPhrase.split(" ")[1] + "%",
                    "%" + searchPhrase.split(" ")[2] + "%",
                    RESPONSE_LIMIT, offset);
        } else if (searchPhrase.split(" ").length == FOUR_WORDS) {
            foods = foodDAO.searchFourWord("%" + searchPhrase.split(" ")[0] + "%",
                    "%" + searchPhrase.split(" ")[1] + "%",
                    "%" + searchPhrase.split(" ")[2] + "%",
                    "%" + searchPhrase.split(" ")[3] + "%", RESPONSE_LIMIT, offset);
        } else if (searchPhrase.split(" ").length == FIVE_WORDS) {
            foods = foodDAO.searchFiveWord("%" + searchPhrase.split(" ")[0] + "%",
                    "%" + searchPhrase.split(" ")[1] + "%",
                    "%" + searchPhrase.split(" ")[2] + "%",
                    "%" + searchPhrase.split(" ")[3] + "%",
                    "%" + searchPhrase.split(" ")[4] + "%",
                    RESPONSE_LIMIT, offset);
        }
        return foods;
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
        @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
        @BindView(R.id.tvCalories) TextView tvCalories;
        @BindView(R.id.tvWeight) TextView tvWeight;
        @BindView(R.id.tvProt) TextView tvProt;
        @BindView(R.id.tvFats) TextView tvFats;
        @BindView(R.id.tvCarbo) TextView tvCarbo;

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_rv_list_of_search_response, viewGroup, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (checkFood(itemAdapter.foods.get(getAdapterPosition()))){
                alert();
            } else {
                Intent intent = new Intent(SearchFoodActivity.this, ActivityDetailFood.class);
                intent.putExtra(Config.INTENT_DETAIL_FOOD, itemAdapter.foods.get(getAdapterPosition()));
                startActivityForResult(intent,45);
            }

        }

        void alert(){
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchFoodActivity.this);
            AlertDialog alertDialog = builder.create();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                            case R.id.btnCancel:
                            alertDialog.dismiss();
                            break;
                            case R.id.btnChange:
                                Intent intent = new Intent(SearchFoodActivity.this, ActivityDetailFood.class);
                                intent.putExtra(Config.DETAIL_FOOD_BTN_NAME, "Изменить");
                                intent.putExtra(Config.INTENT_DETAIL_FOOD, itemAdapter.foods.get(getAdapterPosition()));
                                startActivityForResult(intent,45);
                                alertDialog.dismiss();
                            break;
                    }
                }
            };

            View view = LayoutInflater.from(SearchFoodActivity.this).inflate(R.layout.alert_dialog_change_food_in_template, null);
            Button cancel = view.findViewById(R.id.btnCancel);
            Button edit = view.findViewById(R.id.btnChange);
            cancel.setOnClickListener(listener);
            edit.setOnClickListener(listener);
            alertDialog.setView(view);
            alertDialog.show();
        }

        public void bind(Food food) {
            tvNameOfFood.setText(food.getFullInfo().replace("()", ""));
            tvCalories.setText(String.valueOf(Math.round(food.getCalories() * 100)) + " Ккал");
            if (food.isLiquid()) {
                tvWeight.setText("Вес: 100мл");
            } else {
                tvWeight.setText("Вес: 100г");
            }
            tvProt.setText("Б. " + String.valueOf(Math.round(food.getProteins() * 100)));
            tvFats.setText("Ж. " + String.valueOf(Math.round(food.getFats() * 100)));
            tvCarbo.setText("У. " + String.valueOf(Math.round(food.getCarbohydrates() * 100)));
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Food> foods;
        private int counter;

        public ItemAdapter(List<Food> foods) {
            this.foods = foods;
            tvIndex.setText(String.valueOf(foods.size()));
            counter = -1;
        }


        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchFoodActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.bind(foods.get(position));
            if (position > counter && position % RESPONSE_LIMIT == 0) {
                counter = position;
                getNextPortion(position + RESPONSE_LIMIT);
            }
        }

        private void getNextPortion(int offset) {
            Single.fromCallable(() -> {
                List<Food> cFOODS = getSearchResult(offset);
                return cFOODS;
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(t -> updateAdapter(t), Throwable::printStackTrace);
        }

        private void updateAdapter(List<Food> nextPortion) {
            foods.addAll(nextPortion);
            notifyDataSetChanged();
        }

        private List<Food> getSearchResult(int offset) {
            List<Food> foods = new ArrayList<>();
            if (isEqualsNext) {
                foods = foodDAO.searchFullMatchWord(edtSearchField.getText().toString(), RESPONSE_LIMIT, offset);
                if (foods.size() < RESPONSE_LIMIT) {
                    isEqualsNext = false;
                    if (edtSearchField.getText().toString().contains(" ") && edtSearchField.getText().toString().split(" ").length > 1) {
                        foods.addAll(searchMultiWords(edtSearchField.getText().toString(), foods.size() + offset));
                    } else {
                        foods.addAll(foodDAO.searchOneWord("%" + edtSearchField.getText().toString() + "%",
                                RESPONSE_LIMIT, offset + foods.size()));
                    }
                }
            } else {
                if (edtSearchField.getText().toString().contains(" ") && edtSearchField.getText().toString().split(" ").length > 1) {
                    foods.addAll(searchMultiWords(edtSearchField.getText().toString(), offset));
                } else {
                    foods.addAll(foodDAO.searchOneWord("%" + edtSearchField.getText().toString() + "%",
                            RESPONSE_LIMIT, offset));
                }
            }
            return foods;
        }

        @Override
        public int getItemCount() {
            return foods.size();
        }

    }

    private void startAlertDialog(Food food){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.adding_recipe_product, null);

        EditText portionEditText = alertLayout.findViewById(R.id.edtActivityDetailOfFoodPortion);
        TextView kcalTextView = alertLayout.findViewById(R.id.tvActivityDetailOfFoodCalculateKcal);
        TextView carboTextView = alertLayout.findViewById(R.id.tvActivityDetailOfFoodCalculateCarbo);
        TextView fatTextView = alertLayout.findViewById(R.id.tvActivityDetailOfFoodCalculateFat);
        TextView proteinTextView = alertLayout.findViewById(R.id.tvActivityDetailOfFoodCalculateProtein);
        Button saveButton = alertLayout.findViewById(R.id.btnAddRecipe);
        ImageButton closeButton = alertLayout.findViewById(R.id.btnClose);

//        portionEditText.setText("100");
        kcalTextView.setText(String.valueOf((int)(food.getCalories())) + " Ккал");
        carboTextView.setText(String.valueOf((int)(food.getCarbohydrates())) + " г");
        fatTextView.setText(String.valueOf((int)(food.getFats())) + " г");
        proteinTextView.setText(String.valueOf((int)(food.getProteins())) + " г");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
//        alert.setCancelable(false);
        dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        portionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int index;
                double calories;
                double fats;
                double carbo;
                double proteins;

                try {
                    index = Integer.parseInt(String.valueOf(s));
                } catch (Exception e){
                    index = 0;
                }

                if (index > 0) {
                    calories = food.getCalories() * index;
                    fats = food.getFats() * index;
                    carbo = food.getCarbohydrates() * index;
                    proteins = food.getProteins() * index;
                } else {
                    calories = 0;
                    fats = 0;
                    carbo = 0;
                    proteins = 0;
                }

                kcalTextView.setText(String.valueOf((int)calories) + " Ккал");
                fatTextView.setText(String.valueOf((int)fats) + " г");
                carboTextView.setText(String.valueOf((int)carbo) + " г");
                proteinTextView.setText(String.valueOf((int)proteins) + " г");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index;
                double calories;
                double fats;
                double carbo;
                double proteins;

                try {
                    index = Integer.parseInt(String.valueOf(portionEditText.getText()));
                } catch (Exception e){
                    index = 0;
                }

                if (index > 0) {
                    calories = food.getCalories() * index;
                    fats = food.getFats() * index;
                    carbo = food.getCarbohydrates() * index;
                    proteins = food.getProteins() * index;

                    food.setCalories(calories);
                    food.setFats(fats);
                    food.setCarbohydrates(carbo);
                    food.setProteins(proteins);
                    food.setPortion(index);

                    Intent intent = new Intent();
                    intent.putExtra(Config.RECIPE_FOOD_INTENT, food);

                    setResult(RESULT_OK, intent);
                    dialog.dismiss();
                    finish();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Config.RECIPE_FOOD_INTENT,(Serializable) foodList);
        setResult(RESULT_OK,intent);
        finish();
    }
}
