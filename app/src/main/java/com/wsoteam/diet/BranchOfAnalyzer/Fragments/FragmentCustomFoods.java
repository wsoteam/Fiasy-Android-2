package com.wsoteam.diet.BranchOfAnalyzer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityDetailOfFood;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.Const;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.CustomFood;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.Analytics.EventProperties;

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
    private double coefficientConvert = 4.1868;

    @Override
    public void sendString(String searchString) {
        this.searchString = searchString;
        search(searchString);
    }

    @Override
    public void sendClearSearchField() {

    }


    @Override
    public void onStart() {
        super.onStart();
        customFoods = getCustomFoods();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_foods, container, false);
        unbinder = ButterKnife.bind(this, view);
        clearSearchField();
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void clearSearchField() {
        ((EditText) getActivity().findViewById(R.id.edtActivityListAndSearchCollapsingSearchField)).setText("");
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
        Picasso.get().load(R.drawable.ic_no_find).into(ivAddFavorite);
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
            imbAddFood.setVisibility(View.VISIBLE);
            tvAddFood.setVisibility(View.VISIBLE);
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
                startActivity(new Intent(getActivity(), ActivityCreateFood.class).putExtra(EventProperties.product_from, EventProperties.product_from_button));
                break;
            case R.id.btnAddFavorite:
                startActivity(new Intent(getActivity(), ActivityCreateFood.class).putExtra(EventProperties.product_from, EventProperties.product_from_button));
                break;
        }
    }


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.tvNameOfFood) TextView tvNameOfFood;
        @BindView(R.id.tvCalories) TextView tvCalories;
        @BindView(R.id.tvWeight) TextView tvWeight;
        @BindView(R.id.tvProt) TextView tvProt;
        @BindView(R.id.tvFats) TextView tvFats;
        @BindView(R.id.tvCarbo) TextView tvCarbo;
        private String key = "";

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_rv_list_of_search_response, viewGroup, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onCreatePopupMenu(v);
            return false;
        }

        public void onCreatePopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.inflate(R.menu.food_popup_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.delete_food:
                            showConfirmDialog();
                            break;
                        case R.id.edit_food:
                            itemAdapter.editFood(getAdapterPosition());
                            break;
                    }
                    return false;
                }
            });
        }

        private void showConfirmDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog = builder.create();
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.alert_dialog_confirm, null);
            Button delete = view.findViewById(R.id.btnDeleteConfirm);
            Button cancel = view.findViewById(R.id.btnCancelConfirm);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapter.removeItem(getAdapterPosition());
                    alertDialog.cancel();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });


            alertDialog.setView(view);
            alertDialog.show();
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ActivityDetailOfFood.class);
            intent.putExtra(Config.INTENT_DETAIL_FOOD, convert(itemAdapter.customFoods.get(getAdapterPosition())));
            intent.putExtra(Config.TAG_CHOISE_EATING, ((ActivityListAndSearch) getActivity()).spinnerId);
            intent.putExtra(Config.TAG_OWN_FOOD, true);
            intent.putExtra(Config.INTENT_DATE_FOR_SAVE, getActivity().getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
            startActivity(intent);
        }

        private Food convert(CustomFood customFood) {
            Food food = new Food();
            food.setName(customFood.getName());
            food.setBrand(customFood.getBrand());
            food.setPortion(1);
            food.setLiquid(false);
            food.setKilojoules(customFood.getCalories() * coefficientConvert);
            food.setCalories(customFood.getCalories());
            food.setProteins(customFood.getProteins());
            food.setCarbohydrates(customFood.getCarbohydrates());
            food.setFats(customFood.getFats());
            food.setSaturatedFats(customFood.getSaturatedFats());
            food.setSugar(customFood.getSugar());
            food.setMonoUnSaturatedFats(customFood.getMonoUnSaturatedFats());
            food.setPolyUnSaturatedFats(customFood.getPolyUnSaturatedFats());
            food.setCholesterol(customFood.getCholesterol());
            food.setCellulose(customFood.getCellulose());
            food.setSodium(customFood.getSodium());
            food.setPottassium(customFood.getPottassium());
            return food;
        }

        public void bind(CustomFood customFood) {
            tvNameOfFood.setText(customFood.getName());
            tvCalories.setText(String.valueOf(Math.round(customFood.getCalories() * 100)) + " Ккал");
            tvWeight.setText("Вес: 100г");
            tvProt.setText("Б. " + String.valueOf(Math.round(customFood.getProteins() * 100)));
            tvFats.setText("Ж. " + String.valueOf(Math.round(customFood.getFats() * 100)));
            tvCarbo.setText("У. " + String.valueOf(Math.round(customFood.getCarbohydrates() * 100)));
            this.key = customFood.getKey();
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

        private void removeItem(int position) {
            WorkWithFirebaseDB.deleteCustomFood(customFoods.get(position).getKey());
            customFoods.remove(position);
            notifyItemRemoved(position);
            if (customFoods.size() == 0){
                showStartScreen();
            }
        }

        public void editFood(int position) {
            Intent intent = new Intent(getActivity(), ActivityCreateFood.class);
            intent.putExtra(Const.EDIT_CUSTOM_FOOD, customFoods.get(position));
            startActivity(intent);
        }
    }




}
