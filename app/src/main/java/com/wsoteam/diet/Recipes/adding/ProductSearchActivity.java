package com.wsoteam.diet.Recipes.adding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityDetailOfFood;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.CFood;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductSearchActivity extends AppCompatActivity {

    @BindView(R.id.spnEatingList) Spinner spinner;
    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    @BindView(R.id.ivActivityListAndSearchEmptyImage) ImageView ivEmptyImage;
    @BindView(R.id.tvActivityListAndSearchEmptyText) TextView tvEmptyText;
    @BindView(R.id.tvIndex) TextView tvIndex;

    private List<CFood> recievedListFood;
    private int RESPONSE_LIMIT = 100;
    private ItemAdapter itemAdapter;
    private Thread equalsFirstPortion, equalsSecondPortion, containsFirstPortion, containsSecondPortion, thread;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_search);
        ButterKnife.bind(this);
        bindSpinnerChoiceEating();
        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(ProductSearchActivity.this));
        //rvListOfSearchResponse.setAdapter(new ItemAdapter(recievedListFood));
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
                if (charSequence.length() > 2) {
                    recievedListFood = new ArrayList<>();
                    cr(charSequence);
//                    turnOffSearch();
                    //firstEqualsSearch(charSequence);
//                    firstContainsSearch(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Amplitude.getInstance().logEvent(AmplitudaEvents.view_search_food);

    }

    private void cr(CharSequence charSequence) {
//        Observable<List<CFood>> listObservable = Observable.fromArray(recievedListFood);
        Single.fromCallable(() -> {
            firstContainsSearch(charSequence);
            return null;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> System.out.print(t), Throwable::printStackTrace);
    }


    private void turnOffSearch() {
        if (equalsFirstPortion != null) {
            Thread dummy = equalsFirstPortion;
            equalsFirstPortion = null;
            dummy.interrupt();
        }
        if (equalsSecondPortion != null) {
            Thread dummy = equalsSecondPortion;
            equalsSecondPortion = null;
            dummy.interrupt();
        }
        if (containsFirstPortion != null) {
            Thread dummy = containsFirstPortion;
            containsFirstPortion = null;
            dummy.interrupt();
        }
        if (containsSecondPortion != null) {
            Thread dummy = containsSecondPortion;
            containsSecondPortion = null;
            dummy.interrupt();
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        turnOffSearch();
    }

    private List<CFood> shuffleList(CharSequence charSequence, List<CFood> recievedListFood) {
        List<CFood> cFoods = recievedListFood;
        for (int i = 0; i < recievedListFood.size(); i++) {
            if (cFoods.get(i).getBrend() != null
                    && cFoods.get(i).getName().replace(" (" + cFoods.get(i).getBrend() + ")", "").
                    equalsIgnoreCase(charSequence.toString() + " ")) {
                CFood bubble = cFoods.get(i);
                cFoods.remove(i);
                cFoods.add(0, bubble);
            }
            if (cFoods.get(i).getBrend() == null && cFoods.get(i).getName().equalsIgnoreCase(charSequence.toString() + " ")) {
                CFood bubble = cFoods.get(i);
                cFoods.remove(i);
                cFoods.add(0, bubble);
            }
        }
        return cFoods;
    }

    private void firstEqualsSearch(CharSequence charSequence) {
        Log.e("LOL", "FE search");
        equalsFirstPortion = new Thread(() -> {
            List<CFood> cFoods;
            String searchString = charSequence.toString();
            cFoods = CFood.findWithQuery(CFood.class,
                    "Select * from C_Food where name like ? limit 100", searchString);
            Log.e("LOL", String.valueOf(recievedListFood.size()));
            if (recievedListFood.size() > 0) {
                for (int i = 0; i < cFoods.size(); i++) {
                    recievedListFood.add(cFoods.get(i));
                }
            }
            if (recievedListFood.size() >= RESPONSE_LIMIT) {
                secondEqualsSearch(charSequence);
            } else {
                firstContainsSearch(charSequence);
            }
        });
        equalsFirstPortion.start();
    }

    private void secondEqualsSearch(CharSequence charSequence) {
        Log.e("LOL", "SE search");
        equalsSecondPortion = new Thread(() -> {
            String searchString = charSequence.toString();
            recievedListFood = CFood.findWithQuery(CFood.class,
                    "Select * from C_Food where name like ?", searchString);
            if (recievedListFood.size() > RESPONSE_LIMIT) {
                //handler.sendEmptyMessage(0);
            }
            Log.e("LOL", String.valueOf(recievedListFood.size()));
            firstContainsSearch(charSequence);
        });
        equalsSecondPortion.start();
    }

    private void firstContainsSearch(CharSequence charSequence) {
        Log.e("LOL", "FC search");
        Log.d("MyLogs", "FIRST element - " + CFood.first(CFood.class));
//        containsFirstPortion = new Thread(new Runnable() {
//            @Override
//            public void run() {
        List<CFood> cFoods;
        String searchString = charSequence.toString();
        String searchQuery = "Select * from C_Food where";
        String firstQuery = " name like '%";
        String firstPartQuery = " and name like '%";
        String secondPartQuery = "%'";
        String responseLimit = " limit 100";
        if (searchString.contains(" ") && searchString.split(" ").length > 1) {
            String[] arrayWords = searchString.split(" ");
            for (int i = 0; i < arrayWords.length; i++) {
                if (i == 0) {
                    searchQuery = searchQuery + firstQuery + arrayWords[i] + secondPartQuery;
                } else {
                    searchQuery = searchQuery + firstPartQuery + arrayWords[i] + secondPartQuery;
                }
            }
            recievedListFood = CFood.findWithQuery(CFood.class, searchQuery + responseLimit);
            if (recievedListFood.size() >= RESPONSE_LIMIT) {
                secondContainsSearch(charSequence);
            }
        } else {
            String finishedString = "'%" + searchString + "%'";
            cFoods = CFood.findWithQuery(CFood.class, "SELECT * FROM C_Food WHERE name LIKE " + finishedString + " LIMIT 100");
            Log.d("MyLogs", "Size1 - " + cFoods.size());
            recievedListFood.addAll(cFoods);
            Log.d("MyLogs", "Size2 - " + recievedListFood.size());
            if (recievedListFood.size() >= RESPONSE_LIMIT) {
//                secondContainsSearch(charSequence);
            }
        }
//            }
//        });
//        containsFirstPortion.start();
    }

    private void secondContainsSearch(CharSequence charSequence) {
        Log.e("LOL", "SC search second");
//        containsSecondPortion = new Thread(new Runnable() {
//            @Override
//            public void run() {
        String searchString = charSequence.toString();
        String searchQuery = "Select * from C_Food where";
        String firstQuery = " name like '%";
        String firstPartQuery = " and name like '%";
        String secondPartQuery = "%'";
        if (searchString.contains(" ") && searchString.split(" ").length > 1) {
            String[] arrayWords = searchString.split(" ");
            for (int i = 0; i < arrayWords.length; i++) {
                if (i == 0) {
                    searchQuery = searchQuery + firstQuery + arrayWords[i] + secondPartQuery;
                } else {
                    searchQuery = searchQuery + firstPartQuery + arrayWords[i] + secondPartQuery;
                }
            }
            recievedListFood = CFood.findWithQuery(CFood.class, searchQuery);
            Log.e("LOL", String.valueOf(recievedListFood.size()));
            if (recievedListFood.size() >= RESPONSE_LIMIT) {
                //handler.sendEmptyMessage(0);
            }
        } else {
            String finishedString = "%" + searchString + "%";
            recievedListFood = CFood.findWithQuery(CFood.class,
                    "Select * from C_Food where name like ?", finishedString);
            Log.e("LOL", String.valueOf(recievedListFood.size()));
            if (recievedListFood.size() >= RESPONSE_LIMIT) {
                //handler.sendEmptyMessage(0);
            }
        }
//            }
//        });
//        containsSecondPortion.start();
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
            Intent intent = new Intent(ProductSearchActivity.this, ActivityDetailOfFood.class);
            intent.putExtra(Config.INTENT_DETAIL_FOOD, recievedListFood.get(getAdapterPosition()));
            intent.putExtra(Config.TAG_CHOISE_EATING, spinner.getSelectedItemPosition());
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
            startActivity(intent);
        }

        public void bind(CFood cFood) {
            tvNameOfFood.setText(cFood.getName().replace("()", ""));
            tvCalories.setText(String.valueOf(Math.round(cFood.getCalories() * 100)) + " Ккал");
            if (cFood.isLiquid()) {
                tvWeight.setText("Вес: 100мл");
            } else {
                tvWeight.setText("Вес: 100г");
            }
            tvProt.setText("Б. " + String.valueOf(Math.round(cFood.getProteins() * 100)));
            tvFats.setText("Ж. " + String.valueOf(Math.round(cFood.getFats() * 100)));
            tvCarbo.setText("У. " + String.valueOf(Math.round(cFood.getCarbohydrates() * 100)));
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        List<CFood> foods;

        public ItemAdapter(List<CFood> foods) {
            this.foods = foods;
            tvIndex.setText(String.valueOf(foods.size()));
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ProductSearchActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.bind(foods.get(position));
        }

        @Override
        public int getItemCount() {
            return foods.size();
        }

        public void setNewItem(CFood newItem) {
            foods.add(newItem);
            notifyDataSetChanged();
            tvIndex.setText(String.valueOf(foods.size()));
        }
    }


}

