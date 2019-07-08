package com.wsoteam.diet.BranchOfAnalyzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.App;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityDetailOfFood;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;
import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.FavoriteFood;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentFavorites extends Fragment implements TabsFragment {

    List<Food> foods = new ArrayList<>();
    @BindView(R.id.rvFavorites) RecyclerView rvFavorites;
    Unbinder unbinder;
    @BindView(R.id.ivAddFavorite) ImageView ivAddFavorite;
    @BindView(R.id.tvTitleFavoriteAdd) TextView tvTitleFavoriteAdd;
    @BindView(R.id.tvTextAddFavorite) TextView tvTextAddFavorite;
    @BindView(R.id.btnAddFavorite) Button btnAddFavorite;
    private FoodDAO foodDAO = App.getInstance().getFoodDatabase().foodDAO();
    private ItemAdapter itemAdapter;
    private String searchString = "";

    @Override
    public void sendString(String searchString) {
        this.searchString = searchString;
        search(searchString);
    }

    private void search(String searchString) {
        if (foods.size() != 0) {
            List<Food> correctFoods = new ArrayList<>();
            for (int i = 0; i < foods.size(); i++) {
                if (foods.get(i).getFullInfo().toLowerCase().contains(searchString.toLowerCase())) {
                    correctFoods.add(foods.get(i));
                }
            }
            showResult(correctFoods);
        }
    }

    private void showResult(List<Food> correctFoods) {
        if (correctFoods.size() > 0) {
            hideMessageUI();
            itemAdapter = new ItemAdapter(correctFoods);
            rvFavorites.setAdapter(itemAdapter);
        } else {
            showNoFind();
            itemAdapter = new ItemAdapter(correctFoods);
            rvFavorites.setAdapter(itemAdapter);
        }
    }

    private void showNoFind() {
        Glide.with(getActivity()).load(R.drawable.ic_no_find).into(ivAddFavorite);
        tvTitleFavoriteAdd.setText(getActivity().getResources().getString(R.string.title_no_find_food));
        tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_no_find_food));
        ivAddFavorite.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
        tvTitleFavoriteAdd.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStart() {
        super.onStart();
        findFavoritesInDb();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void findFavoritesInDb() {
        Single.fromCallable(() -> {
            List<Food> foods = getFavoriteFoods(getMarkers());
            return foods;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Food> foods) {
                updateUI(foods);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void updateUI(List<Food> foods) {
        if (foods.size() == 0) {
            this.foods = foods;
            itemAdapter = new ItemAdapter(foods);
            rvFavorites.setAdapter(itemAdapter);
            showStartScreen();
        } else {
            hideMessageUI();
            this.foods = foods;
            itemAdapter = new ItemAdapter(foods);
            rvFavorites.setAdapter(itemAdapter);
        }
    }

    private void hideMessageUI() {
        tvTextAddFavorite.setVisibility(View.GONE);
        tvTitleFavoriteAdd.setVisibility(View.GONE);
        ivAddFavorite.setVisibility(View.GONE);
    }

    private void showStartScreen() {
        //btnAddFavorite.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
        tvTitleFavoriteAdd.setVisibility(View.VISIBLE);
        ivAddFavorite.setVisibility(View.VISIBLE);
    }

    private List<Food> getFavoriteFoods(List<FavoriteFood> favoriteFoods) {
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < favoriteFoods.size(); i++) {
            foods.add(foodDAO.getById(favoriteFoods.get(i).getId()));
        }
        return foods;
    }

    private List<FavoriteFood> getMarkers() {
        List<FavoriteFood> favoriteFoods = new ArrayList<>();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getFoodFavorites() != null) {
            Iterator iterator = UserDataHolder.getUserData().getFoodFavorites().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                FavoriteFood favoriteFood = (FavoriteFood) pair.getValue();
                favoriteFood.setKey((String) pair.getKey());
                favoriteFoods.add(favoriteFood);
            }
        }
        return favoriteFoods;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

        public ItemAdapter(List<Food> foods) {
            this.foods = foods;
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
        }

        @Override
        public int getItemCount() {
            return foods.size();
        }
    }

}
