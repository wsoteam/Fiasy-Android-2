package com.wsoteam.diet.BranchOfAnalyzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsoteam.diet.App;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityDetailOfFood;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;
import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragmentSearch extends Fragment implements TabsFragment {

    @BindView(R.id.ivSearchImage) ImageView ivSearchImage;
    @BindView(R.id.tvTitleFavoriteAdd) TextView tvTitleFavoriteAdd;
    @BindView(R.id.tvTextAddFavorite) TextView tvTextAddFavorite;
    @BindView(R.id.btnAddFavorite) Button btnAddFavorite;
    private int RESPONSE_LIMIT = 50;
    private ItemAdapter itemAdapter;
    private boolean isEqualsNext = true;
    private FoodDAO foodDAO = App.getInstance().getFoodDatabase().foodDAO();
    private final int ONE_WORD = 1, TWO_WORDS = 2, THREE_WORDS = 3, FOUR_WORDS = 4, FIVE_WORDS = 5;

    private CompositeDisposable disposables = new CompositeDisposable();

    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    Unbinder unbinder;
    private String searchString = "";


    @Override
    public void sendString(String searchString) {
        if (searchString.length() > 2) {
            isEqualsNext = true;
            this.searchString = searchString;
            search(searchString);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        updateUI();
        showStartUI();
        return view;
    }

    private void hideMessageUI() {
        ivSearchImage.setVisibility(View.GONE);
        tvTextAddFavorite.setVisibility(View.GONE);
        tvTitleFavoriteAdd.setVisibility(View.GONE);
        btnAddFavorite.setVisibility(View.GONE);
    }

    private void showNoFind() {
        Picasso.get().load(R.drawable.ic_no_find).into(ivSearchImage);
        tvTitleFavoriteAdd.setText(getActivity().getResources().getString(R.string.title_no_find_food));
        tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_no_find_food));
        ivSearchImage.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
        tvTitleFavoriteAdd.setVisibility(View.VISIBLE);
        btnAddFavorite.setVisibility(View.VISIBLE);
    }

    private void showStartUI() {
        Picasso.get().load(R.drawable.ic_start_search).into(ivSearchImage);
        tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_start_search));
        ivSearchImage.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposables.clear();
    }

    private void search(String searchString) {
        disposables.add(Single.fromCallable(() -> {
            List<Food> cFOODS = getFirstList(searchString);
            //Events.logFoodSearch(cFOODS.size());
            return cFOODS;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> refreshAdapter(t), Throwable::printStackTrace));
    }

    private void refreshAdapter(List<Food> t) {
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

    private void updateUI() {
        List<Food> foods = new ArrayList<>();
        itemAdapter = new ItemAdapter(foods);
        rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListOfSearchResponse.setAdapter(itemAdapter);
    }

    @OnClick(R.id.btnAddFavorite)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), ActivityCreateFood.class));
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
            Intent intent = new Intent(getActivity(), ActivityDetailOfFood.class);
            intent.putExtra(Config.INTENT_DETAIL_FOOD, itemAdapter.foods.get(getAdapterPosition()));
            intent.putExtra(Config.TAG_CHOISE_EATING, ((ActivityListAndSearch) getActivity()).spinnerId);
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getActivity().getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
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
            counter = -1;
        }

        public List<Food> getFoods() {
            return foods;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
                foods = foodDAO.searchFullMatchWord(searchString, RESPONSE_LIMIT, offset);
                if (foods.size() < RESPONSE_LIMIT) {
                    isEqualsNext = false;
                    if (searchString.contains(" ") && searchString.split(" ").length > 1) {
                        foods.addAll(searchMultiWords(searchString, foods.size() + offset));
                    } else {
                        foods.addAll(foodDAO.searchOneWord("%" + searchString + "%",
                                RESPONSE_LIMIT, offset + foods.size()));
                    }
                }
            } else {
                if (searchString.contains(" ") && searchString.split(" ").length > 1) {
                    foods.addAll(searchMultiWords(searchString, offset));
                } else {
                    foods.addAll(foodDAO.searchOneWord("%" + searchString + "%",
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
}
