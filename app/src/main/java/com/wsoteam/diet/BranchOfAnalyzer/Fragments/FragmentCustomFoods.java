package com.wsoteam.diet.BranchOfAnalyzer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityDetailOfFood;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.CustomFood;
import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentCustomFoods extends Fragment implements TabsFragment {

    @BindView(R.id.imbAddFood) FloatingActionButton imbAddFood;
    @BindView(R.id.tvAddFood) TextView tvAddFood;
    @BindView(R.id.rvFavorites) RecyclerView rvFavorites;
    @BindView(R.id.ivAddFavorite) ImageView ivAddFavorite;
    @BindView(R.id.tvTitleFavoriteAdd) TextView tvTitleFavoriteAdd;
    @BindView(R.id.tvTextAddFavorite) TextView tvTextAddFavorite;
    @BindView(R.id.btnAddFavorite) Button btnAddFavorite;
    Unbinder unbinder;
    private List<CustomFood> customFoods = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private String searchString = "";

    @Override
    public void sendString(String searchString) {
        this.searchString = searchString;
        search(searchString);
    }

    @Override
    public void onResume() {
        super.onResume();
        customFoods = getCustomFoods();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_foods, container, false);
        unbinder = ButterKnife.bind(this, view);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void showResult(List<CustomFood> customFoods) {
        if (customFoods.size() > 0) {
            hideMessageUI();
            itemAdapter = new ItemAdapter(customFoods);
            rvFavorites.setAdapter(itemAdapter);
        } else {
            showNoFind();
            itemAdapter = new ItemAdapter(customFoods);
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

    private void search(String searchString) {
        if (customFoods.size() != 0) {
            List<CustomFood> correctFoods = new ArrayList<>();
            for (int i = 0; i < customFoods.size(); i++) {
                if (customFoods.get(i).getName().toLowerCase().contains(searchString.toLowerCase())) {
                    correctFoods.add(customFoods.get(i));
                }
            }
            showResult(correctFoods);
        }
    }

    private void updateUI() {
        if (customFoods.size() == 0) {
            itemAdapter = new ItemAdapter(customFoods);
            rvFavorites.setAdapter(itemAdapter);
            showStartScreen();
        } else {
            hideMessageUI();
            itemAdapter = new ItemAdapter(customFoods);
            rvFavorites.setAdapter(itemAdapter);
        }
    }

    private void hideMessageUI() {
        tvTextAddFavorite.setVisibility(View.GONE);
        tvTitleFavoriteAdd.setVisibility(View.GONE);
        ivAddFavorite.setVisibility(View.GONE);
        btnAddFavorite.setVisibility(View.GONE);
    }

    private void showStartScreen() {
        btnAddFavorite.setVisibility(View.VISIBLE);
        tvTextAddFavorite.setVisibility(View.VISIBLE);
        tvTitleFavoriteAdd.setVisibility(View.VISIBLE);
        ivAddFavorite.setVisibility(View.VISIBLE);
        imbAddFood.setVisibility(View.GONE);
        tvAddFood.setVisibility(View.GONE);
    }

    private List<CustomFood> getCustomFoods() {
        List<CustomFood> customFoods = new ArrayList<>();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getCustomFoods() != null) {
            Iterator iterator = UserDataHolder.getUserData().getCustomFoods().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                CustomFood customFood = (CustomFood) pair.getValue();
                customFood.setKey((String) pair.getKey());
                customFoods.add(customFood);
            }
        }
        return customFoods;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.imbAddFood, R.id.btnAddFavorite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imbAddFood:
                startActivity(new Intent(getActivity(), ActivityCreateFood.class));
                break;
            case R.id.btnAddFavorite:
                startActivity(new Intent(getActivity(), ActivityCreateFood.class));
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
            Intent intent = new Intent(getActivity(), ActivityDetailOfFood.class);
            //intent.putExtra(Config.INTENT_DETAIL_FOOD, itemAdapter.foods.get(getAdapterPosition()));
            intent.putExtra(Config.TAG_CHOISE_EATING, ((ActivityListAndSearch) getActivity()).spinnerId);
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getActivity().getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
            startActivity(intent);
        }

        public void bind(CustomFood customFood) {
            tvNameOfFood.setText(customFood.getName());
            tvCalories.setText(String.valueOf(Math.round(customFood.getCalories() * 100)) + " Ккал");
            tvWeight.setText("Вес: 100г");
            tvProt.setText("Б. " + String.valueOf(Math.round(customFood.getProteins() * 100)));
            tvFats.setText("Ж. " + String.valueOf(Math.round(customFood.getFats() * 100)));
            tvCarbo.setText("У. " + String.valueOf(Math.round(customFood.getCarbohydrates() * 100)));
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<CustomFood> customFoods;

        public ItemAdapter(List<CustomFood> customFoods) {
            this.customFoods = customFoods;
        }


        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.bind(customFoods.get(position));
        }

        @Override
        public int getItemCount() {
            return customFoods.size();
        }
    }
}
