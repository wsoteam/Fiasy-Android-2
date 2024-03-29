package com.losing.weight.BranchOfAnalyzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.losing.weight.BranchOfAnalyzer.ActivityDetailOfFood;
import com.losing.weight.BranchOfAnalyzer.ActivityListAndSearch;
import com.losing.weight.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.losing.weight.BranchOfAnalyzer.TabsFragment;
import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.common.backward.FoodConverter;
import com.losing.weight.common.networking.food.FoodResultAPI;
import com.losing.weight.common.networking.food.FoodSearch;
import com.losing.weight.common.networking.food.POJO.Result;
import com.losing.weight.utils.DrawableUtilsKt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends Fragment implements TabsFragment {

    @BindView(R.id.ivSearchImage) ImageView ivSearchImage;
    @BindView(R.id.tvTitleFavoriteAdd) TextView tvTitleFavoriteAdd;
    @BindView(R.id.tvTextAddFavorite) TextView tvTextAddFavorite;
    @BindView(R.id.btnAddFavorite) Button btnAddFavorite;
    private int RESPONSE_LIMIT = 100;
    private ItemAdapter itemAdapter;
    private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();
    private String searchString = "";
    @BindView(R.id.rvListOfSearchResponse) RecyclerView rvListOfSearchResponse;
    Unbinder unbinder;

    @Override
    public void sendClearSearchField() {

    }

    @Override
    public void sendString(String searchString) {
        search(searchString.trim());
        Events.logSearch(searchString);
        this.searchString = searchString;
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
        ivSearchImage.setImageDrawable(DrawableUtilsKt.getVectorIcon(requireContext(), R.drawable.ic_no_find));
        tvTitleFavoriteAdd.setText(getActivity().getResources().getString(R.string.title_no_find_food));
        tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_no_find_food));
        ivSearchImage.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
        tvTitleFavoriteAdd.setVisibility(View.VISIBLE);
        btnAddFavorite.setVisibility(View.VISIBLE);
    }

    private void showStartUI() {
        ivSearchImage.setImageDrawable(DrawableUtilsKt.getVectorIcon(requireContext(), R.drawable.ic_start_search));
        tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_start_search));
        ivSearchImage.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    private void updateUI() {
        List<Result> foods = new ArrayList<>();
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
        @BindView(R.id.tvBrand) TextView tvBrand;

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_rv_list_of_search_response, viewGroup, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ActivityDetailOfFood.class);
            intent.putExtra(Config.INTENT_DETAIL_FOOD, FoodConverter.convertResultToFood(itemAdapter.foods.get(getAdapterPosition())));
            intent.putExtra(Config.TAG_CHOISE_EATING, ((ActivityListAndSearch) getActivity()).spinnerId);
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getActivity().getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
            startActivity(intent);
        }

        public void bind(Result food) {
            tvNameOfFood.setText(food.getName());
            tvCalories.setText(String.format(getString(R.string.n_KCal), Math.round(food.getCalories() * 100)));
            try {
                if (food.isLiquid()) {
                    tvWeight.setText(getString(R.string.search_food_activity_weight_ml));
                } else {
                    tvWeight.setText(getString(R.string.search_food_activity_weight_g));
                }
            } catch (Exception e) {
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

        public List<Result> getFoods() {
            return foods;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemHolder(layoutInflater, parent);
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
}
