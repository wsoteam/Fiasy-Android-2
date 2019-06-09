package com.wsoteam.diet.BranchOfAnalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.RunClass.Diet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActivityListAndSearch extends AppCompatActivity {
    @BindView(R.id.spnEatingList) Spinner spinner;
    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    @BindView(R.id.ivActivityListAndSearchEmptyImage) ImageView ivEmptyImage;
    @BindView(R.id.tvActivityListAndSearchEmptyText) TextView tvEmptyText;
    @BindView(R.id.tvIndex) TextView tvIndex;

    private int RESPONSE_LIMIT = 50;
    private ItemAdapter itemAdapter;
    private boolean isEqualsNext = true;
    private FoodDAO foodDAO = Diet.getInstance().getFoodDatabase().foodDAO();
    private final int ONE_WORD = 1, TWO_WORDS = 2, THREE_WORDS = 3, FOUR_WORDS = 4, FIVE_WORDS = 5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_search);
        ButterKnife.bind(this);
        bindSpinnerChoiceEating();
        updateUI();

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
                search(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Amplitude.getInstance().logEvent(AmplitudaEvents.attempt_add_food);
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_search_food);

    }

    private void updateUI() {
        List<Food> foods = new ArrayList<>();
        itemAdapter = new ItemAdapter(foods);
        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(ActivityListAndSearch.this));
        rvListOfSearchResponse.setAdapter(itemAdapter);
    }

    private void search(CharSequence charSequence) {
        Single.fromCallable(() -> {
            List<Food> cFOODS = getFirstList(charSequence);
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

    private List<Food> getFirstList(CharSequence charSequence) {
        List<Food> foods = new ArrayList<>();
        foods.addAll(foodDAO.searchFullMatchWord(charSequence.toString(), RESPONSE_LIMIT, 0));
        if (foods.size() < RESPONSE_LIMIT) {
            isEqualsNext = false;
            if (charSequence.toString().contains(" ") && charSequence.toString().split(" ").length > 1) {
                foods.addAll(searchMultiWords(charSequence.toString(), foods.size()));
            } else {
                foods.addAll(foodDAO.searchOneWord("%" + charSequence.toString() + "%", RESPONSE_LIMIT, foods.size()));
            }
        }
        Log.e("LOL", "First" + String.valueOf(foods.size()));
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


    private void bindSpinnerChoiceEating() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.item_spinner_food_search, getResources().getStringArray(R.array.eatingList));
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search);
        spinner.setAdapter(adapter);
        spinner.setSelection(getIntent().getIntExtra(Config.TAG_CHOISE_EATING, 0));
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

    private List<Food> shuffleList(CharSequence charSequence, List<Food> recievedListFood) {
        List<Food> cFOODS = recievedListFood;
        for (int i = 0; i < recievedListFood.size(); i++) {
            if (cFOODS.get(i).getBrand() != null
                    && cFOODS.get(i).getName().replace(" (" + cFOODS.get(i).getBrand() + ")", "").
                    equalsIgnoreCase(charSequence.toString() + " ")) {
                Food bubble = cFOODS.get(i);
                cFOODS.remove(i);
                cFOODS.add(0, bubble);
            }
            if (cFOODS.get(i).getBrand() == null && cFOODS.get(i).getName().equalsIgnoreCase(charSequence.toString() + " ")) {
                Food bubble = cFOODS.get(i);
                cFOODS.remove(i);
                cFOODS.add(0, bubble);
            }
        }
        return cFOODS;
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
            Intent intent = new Intent(ActivityListAndSearch.this, ActivityDetailOfFood.class);
            intent.putExtra(Config.INTENT_DETAIL_FOOD, itemAdapter.foods.get(getAdapterPosition()));
            intent.putExtra(Config.TAG_CHOISE_EATING, spinner.getSelectedItemPosition());
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
            startActivity(intent);
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

        public List<Food> getFoods() {
            return foods;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListAndSearch.this);
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


}

