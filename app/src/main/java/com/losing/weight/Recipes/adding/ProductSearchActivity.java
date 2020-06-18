package com.losing.weight.Recipes.adding;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.common.backward.FoodConverter;
import com.losing.weight.common.networking.food.FoodResultAPI;
import com.losing.weight.common.networking.food.FoodSearch;
import com.losing.weight.common.networking.food.POJO.Result;
import com.losing.weight.utils.DrawableUtilsKt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchActivity extends AppCompatActivity {

    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    @BindView(R.id.ivActivityListAndSearchEmptyImage) ImageView ivEmptyImage;
    @BindView(R.id.tvActivityListAndSearchEmptyText) TextView tvEmptyText;
    @BindView(R.id.ibActivityListAndSearchCollapsingCancelButton) ImageView ibSpeakAndClear;
    private Window window;

    private int RESPONSE_LIMIT = 100;
    private ItemAdapter itemAdapter;
    private boolean isCanSpeak = true;
    private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();
    private List<Food> foodList;
    private String searchString = "";


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
        foodList = new ArrayList<>();


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
        edtSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(edtSearchField.getText().toString().replaceAll("\\s+", " "));
                    return true;
                }
                return false;
            }
        });

    }

    private void hideMessageUI() {
        ivEmptyImage.setVisibility(View.GONE);
        tvEmptyText.setVisibility(View.GONE);
    }

    private void showNoFind() {
        ivEmptyImage.setImageDrawable(DrawableUtilsKt.getVectorIcon(this,
            R.drawable.ic_no_find));
        tvEmptyText.setText(getResources().getString(R.string.text_no_find_food));
        tvEmptyText.setVisibility(View.VISIBLE);
        ivEmptyImage.setVisibility(View.VISIBLE);
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // намерение для вызова формы обработки речи (ОР)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // сюда он слушает и запоминает
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите!");
        startActivityForResult(intent, 1234); // вызываем активность ОР
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
                foodList.add(food);
                Log.d("kkk", "onActivityResult: search");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {
        List<Result> foods = new ArrayList<>();
        itemAdapter = new ItemAdapter(foods);
        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(ProductSearchActivity.this));
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
        if (rvListOfSearchResponse == null) {
            return;
        }
        rvListOfSearchResponse.setAdapter(itemAdapter = new ItemAdapter(t));
        if (t.size() > 0) {
            hideMessageUI();
        } else {
            showNoFind();
        }
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
            Intent intent = new Intent(ProductSearchActivity.this, ActivityDetailFood.class);
            intent.putExtra(Config.INTENT_DETAIL_FOOD, FoodConverter.convertResultToFood(itemAdapter.foods.get(getAdapterPosition())));
            startActivityForResult(intent, 45);

        }

        public void bind(Result food) {
            tvNameOfFood.setText(food.getName());
            tvCalories.setText(String.format(getString(R.string.n_KCal), Math.round(food.getCalories() * 100)));
            if (food.isLiquid()) {
                tvWeight.setText(getString(R.string.search_food_activity_weight_ml));
            } else {
                tvWeight.setText(getString(R.string.search_food_activity_weight_g));
            }
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

        private void loadNextPortion(int offset){
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


        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ProductSearchActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            if (position == currentPaginationTrigger){
                currentPaginationTrigger += RESPONSE_LIMIT - 1;
                countPaginations += 1;
                loadNextPortion(countPaginations * RESPONSE_LIMIT);
            }
            holder.bind(foods.get(position));
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
