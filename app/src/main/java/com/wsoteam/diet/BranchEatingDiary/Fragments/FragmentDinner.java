package com.wsoteam.diet.BranchEatingDiary.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentDinner extends Fragment {
    private RecyclerView recyclerView;
    private DinnerItemAdapter dinnerItemAdapter;
    private ImageView ivEmptyState;
    private TextView tvEmptyStateTitle, tvEmptyStateText;

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Dinner> dinnerArrayList = Dinner.listAll(Dinner.class);

        if (dinnerArrayList.size() == 0){
            showEmptyStateViews();
        }else {
            hideEmptyStateViews();
        }

        setNumbersInCollapsingBar((ArrayList<Dinner>) dinnerArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dinnerItemAdapter = new DinnerItemAdapter((ArrayList<Dinner>) dinnerArrayList);
        recyclerView.setAdapter(dinnerItemAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                dinnerItemAdapter.removeItem(viewHolder.getAdapterPosition());
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void hideEmptyStateViews() {
        ivEmptyState.setVisibility(View.GONE);
        tvEmptyStateTitle.setVisibility(View.GONE);
        tvEmptyStateText.setVisibility(View.GONE);
    }

    private void showEmptyStateViews() {
        ivEmptyState.setVisibility(View.VISIBLE);
        tvEmptyStateTitle.setVisibility(View.VISIBLE);
        tvEmptyStateText.setVisibility(View.VISIBLE);
    }

    private void setNumbersInCollapsingBar(ArrayList<Dinner> dinnerArrayList) {
        int kcal = 0, fat = 0, carbo = 0, prot = 0;

        TextView tvEatingDiaryCollapsingFat = getActivity().findViewById(R.id.tvEatingDiaryCollapsingFat);
        TextView tvEatingDiaryCollapsingCarbo = getActivity().findViewById(R.id.tvEatingDiaryCollapsingCarbo);
        TextView tvEatingDiaryCollapsingProt = getActivity().findViewById(R.id.tvEatingDiaryCollapsingProt);
        TextView tvEatingDiaryCollapsingKcal = getActivity().findViewById(R.id.tvEatingDiaryCollapsingKcal);

        for (int i = 0; i < dinnerArrayList.size(); i++) {
            kcal += dinnerArrayList.get(i).getCalories();
            fat += dinnerArrayList.get(i).getFat();
            carbo += dinnerArrayList.get(i).getCarbohydrates();
            prot += dinnerArrayList.get(i).getProtein();
        }
        tvEatingDiaryCollapsingFat.setText(String.valueOf(fat) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingCarbo.setText(String.valueOf(carbo) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingProt.setText(String.valueOf(prot) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingKcal.setText(String.valueOf(kcal) + " " + getActivity().getString(R.string.kcal));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner, container, false);
        recyclerView = view.findViewById(R.id.rvEatingDinner);
        ivEmptyState = view.findViewById(R.id.ivEmptyState);
        tvEmptyStateTitle = view.findViewById(R.id.tvEmptyStateTitle);
        tvEmptyStateText = view.findViewById(R.id.tvEmptyStateText);
        return view;
    }

    private class DinnerItemHolder extends RecyclerView.ViewHolder {
        private TextView tvEatingItemName, tvEatingItemFat, tvEatingItemCarbo,
                tvEatingItemProt, tvEatingItemKcal, tvEatingItemWeight, tvLeterOfProductInDiary;
        private ImageView ivImage;
        private CardView cvInvisibleCardEatingDiary;

        public DinnerItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_eating_diary_lists, viewGroup, false));

            tvEatingItemName = itemView.findViewById(R.id.tvEatingItemName);
            tvEatingItemFat = itemView.findViewById(R.id.tvEatingItemFat);
            tvEatingItemCarbo = itemView.findViewById(R.id.tvEatingItemCarbo);
            tvEatingItemProt = itemView.findViewById(R.id.tvEatingItemProt);
            tvEatingItemKcal = itemView.findViewById(R.id.tvEatingItemKcal);
            tvEatingItemWeight = itemView.findViewById(R.id.tvEatingItemWeight);
            tvLeterOfProductInDiary = itemView.findViewById(R.id.tvLeterOfProductInDiary);
            cvInvisibleCardEatingDiary = itemView.findViewById(R.id.cvInvisibleCardEatingDiary);
            ivImage = itemView.findViewById(R.id.ivImage);

        }

        public void bind(Dinner dinner) {
            cvInvisibleCardEatingDiary.setVisibility(View.GONE);
            tvEatingItemName.setText(String.valueOf(dinner.getName()));
            tvEatingItemFat.setText(String.valueOf(dinner.getFat()) + " г");
            tvEatingItemCarbo.setText(String.valueOf(dinner.getCarbohydrates()) + " г");
            tvEatingItemProt.setText(String.valueOf(dinner.getProtein()) + " г");
            tvEatingItemKcal.setText(String.valueOf(dinner.getCalories()) + " ккал");
            tvEatingItemWeight.setText(String.valueOf(dinner.getWeight()) + " г");

            if (!dinner.getUrlOfImages().equals("")){
                Glide.with(getActivity()).load(dinner.getUrlOfImages()).into(ivImage);
            }else {
                cvInvisibleCardEatingDiary.setVisibility(View.VISIBLE);
                tvLeterOfProductInDiary.setText(String.valueOf(Character.toUpperCase(dinner.getName().charAt(0))));
            }

        }
    }

    private class DinnerItemAdapter extends RecyclerView.Adapter<DinnerItemHolder> {
        ArrayList<Dinner> dinnerList;

        public DinnerItemAdapter(ArrayList<Dinner> dinnerList) {
            this.dinnerList = dinnerList;
        }

        @NonNull
        @Override
        public DinnerItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DinnerItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DinnerItemHolder holder, int position) {
            holder.bind(dinnerList.get(position));
        }

        @Override
        public int getItemCount() {
            return dinnerList.size();
        }

        public void removeItem(int adapterPosition) {
            dinnerList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            Dinner.deleteAll(Dinner.class);
            for (int i = 0; i < dinnerList.size(); i++) {
                dinnerList.get(i).save();
            }
            setNumbersInCollapsingBar(dinnerList);
        }
    }


}
