package com.losing.weight.presentation.food.template.create.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.losing.weight.BranchOfAnalyzer.templates.POJO.FoodTemplateHolder;
import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.Recipes.adding.ActivityDetailFood;
import com.losing.weight.common.backward.FoodConverter;
import com.losing.weight.common.networking.food.FoodResultAPI;
import com.losing.weight.common.networking.food.FoodSearch;
import com.losing.weight.common.networking.food.POJO.Result;
import com.losing.weight.presentation.food.template.create.detail.DetailFoodActivity;
import com.losing.weight.utils.DrawableUtilsKt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.concat;

public class SearchFoodActivity extends AppCompatActivity {

    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    @BindView(R.id.ivActivityListAndSearchEmptyImage) ImageView ivEmptyImage;
    @BindView(R.id.tvActivityListAndSearchEmptyText) TextView tvEmptyText;
    @BindView(R.id.ibActivityListAndSearchCollapsingCancelButton) ImageView ibSpeakAndClear;
    private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();
    private AlertDialog dialog;
    private Window window;

    private int RESPONSE_LIMIT = 100;
    private ItemAdapter itemAdapter;
    private String searchString = "";
    private boolean isCanSpeak = true;
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

        edtSearchField.setHint(concat("       ",getString(R.string.search)));
        edtSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeSpeakButton(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtSearchField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(edtSearchField.getText().toString().replaceAll("\\s+", " "));
                return true;
            }
            return false;
        });

    }

    private void showNoFind() {
        ivEmptyImage.setImageDrawable(DrawableUtilsKt.getVectorIcon(this,
            R.drawable.ic_no_find));
        tvEmptyText.setText(getResources().getString(R.string.text_no_find_food));
        ivEmptyImage.setVisibility(View.VISIBLE);
        tvEmptyText.setVisibility(View.VISIBLE);
    }

    private void hideMessageUI() {
        ivEmptyImage.setVisibility(View.GONE);
        tvEmptyText.setVisibility(View.GONE);
    }

    private void changeSpeakButton(CharSequence charSequence) {
        if (charSequence.length() > 0 && isCanSpeak) {
            isCanSpeak = false;
            ibSpeakAndClear.setImageDrawable(DrawableUtilsKt.getVectorIcon(this, R.drawable.ic_cancel));
        } else if (charSequence.length() == 0 && !isCanSpeak) {
            isCanSpeak = true;
            ibSpeakAndClear.setImageDrawable(DrawableUtilsKt.getVectorIcon(this, R.drawable.ic_speak));
        }
    }

    private boolean checkFood(Food food) {
        for (Food f : foodList) {
            if (f.getName().equals(food.getName())) {
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
            search(edtSearchField.getText().toString().replaceAll("\\s+", " "));
        }
        if (requestCode == 45 && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Food food = (Food) bundle.get(Config.RECIPE_FOOD_INTENT);

                int position = getPosition(food);

                if (position < 0) {
                    foodList.add(food);
                } else {
                    foodList.set(position, food);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    int getPosition(Food food) {
        for (Food f : foodList) {
            if (f.getName().equals(food.getName())) {
                return foodList.indexOf(f);
            }
        }
        return -1;
    }

    private void updateUI() {
        List<Result> foods = new ArrayList<>();
        itemAdapter = new ItemAdapter(foods);
        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(this));
        rvListOfSearchResponse.setAdapter(itemAdapter);
    }

    private void search(String searchString) {
        foodResultAPI
                .getResponse(RESPONSE_LIMIT, 0, searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> refreshAdapter(t.getResults()), Throwable::printStackTrace);
    }

    private void refreshAdapter(List<Result> t) {
        itemAdapter = new ItemAdapter(t);
        rvListOfSearchResponse.setAdapter(itemAdapter);
        if (t.size() > 0) {
            hideMessageUI();
        } else {
            showNoFind();
        }
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // намерение для вызова формы обработки речи (ОР)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // сюда он слушает и запоминает
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.search_food_activity_say));
        startActivityForResult(intent, 1234); // вызываем активность ОР
    }

    @OnClick({R.id.ibActivityListAndSearchCollapsingCancelButton, R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibActivityListAndSearchCollapsingCancelButton:
                if (isCanSpeak) {
                    speak();
                } else {
                    edtSearchField.setText("");
                }
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
        @BindView(R.id.tvBrand) TextView tvBrand;

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_rv_list_of_search_response, viewGroup, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (checkFood(FoodConverter.convertResultToFood(itemAdapter.foods.get(getAdapterPosition())))) {
                alert();
            } else {
                Intent intent = new Intent(SearchFoodActivity.this, DetailFoodActivity.class);
                intent.putExtra(Config.INTENT_DETAIL_FOOD, FoodConverter.convertResultToFood(itemAdapter.foods.get(getAdapterPosition())));
                intent.putExtra(Config.SEND_RESULT_TO_BACK, true);
                startActivityForResult(intent, 45);
            }

        }

        void alert() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchFoodActivity.this);
            AlertDialog alertDialog = builder.create();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btnCancel:
                            alertDialog.dismiss();
                            break;
                        case R.id.btnChange:
                            Intent intent = new Intent(SearchFoodActivity.this, ActivityDetailFood.class);
                            intent.putExtra(Config.DETAIL_FOOD_BTN_NAME, getString(R.string.btnChange));
                            intent.putExtra(Config.INTENT_DETAIL_FOOD, FoodConverter.convertResultToFood(itemAdapter.foods.get(getAdapterPosition())));
                            startActivityForResult(intent, 45);
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

        public void bind(Result food) {
            tvNameOfFood.setText(food.getName());
            //tvCalories.setText(String.valueOf(Math.round(food.getCalories() * 100)) + " Ккал");
            tvCalories.setText(String.format(getString(R.string.n_KCal),
                Math.round(food.getCalories() * 100)));

            if (food.isLiquid()) {
                tvWeight.setText(getString(R.string.search_food_activity_weight_ml));
            } else {
                tvWeight.setText(getString(R.string.search_food_activity_weight_g));
            }
            //tvProt.setText("Б. " + String.valueOf(Math.round(food.getProteins() * 100)));
            //tvFats.setText("Ж. " + String.valueOf(Math.round(food.getFats() * 100)));
            //tvCarbo.setText("У. " + String.valueOf(Math.round(food.getCarbohydrates() * 100)));
            tvProt.setText(String.format(getString(R.string.search_food_activity_prot), Math.round(food.getProteins() * 100)));
            tvFats.setText(String.format(getString(R.string.search_food_activity_fat), Math.round(food.getFats() * 100)));
            tvCarbo.setText(String.format(getString(R.string.search_food_activity_carbo), Math.round(food.getCarbohydrates() * 100)));
            if (food.getBrand() != null && !food.getBrand().getName().equals("")) {
                tvBrand.setVisibility(View.VISIBLE);
                tvBrand.setText(food.getBrand().getName());
            } else {
                tvBrand.setVisibility(View.GONE);
            }
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Result> foods;
        private int currentPaginationTrigger = 0;
        private int countPaginations = 0;

        public ItemAdapter(List<Result> foods) {
            this.foods = foods;
        }


        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchFoodActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            if (position == currentPaginationTrigger) {
                currentPaginationTrigger += RESPONSE_LIMIT - 1;
                countPaginations += 1;
                loadNextPortion(countPaginations * RESPONSE_LIMIT);
            }
            holder.bind(foods.get(position));
        }

        private void loadNextPortion(int offset) {
            foodResultAPI
                    .getResponse(RESPONSE_LIMIT, offset, searchString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(t -> addItems(t.getResults()), Throwable::printStackTrace);
        }

        private void addItems(List<Result> results) {
            for (int i = 0; i < results.size(); i++) {
                foods.add(results.get(i));
            }
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            return foods.size();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Config.RECIPE_FOOD_INTENT, (Serializable) foodList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
