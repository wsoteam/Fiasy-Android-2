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
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBreakfast extends Fragment {
    private RecyclerView recyclerView;
    private BreakfastItemAdapter breakfastItemAdapter;
    private ImageView ivEmptyState;
    private TextView tvEmptyStateTitle, tvEmptyStateText;

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Breakfast> breakfastArrayList = Breakfast.listAll(Breakfast.class);

        if (breakfastArrayList.size() == 0) {
            showEmptyStateViews();
        } else {
            hideEmptyStateViews();
        }

        setNumbersInCollapsingBar((ArrayList<Breakfast>) breakfastArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        breakfastItemAdapter = new BreakfastItemAdapter((ArrayList<Breakfast>) breakfastArrayList);
        recyclerView.setAdapter(breakfastItemAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                breakfastItemAdapter.removeItem(viewHolder.getAdapterPosition());
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

    private void setNumbersInCollapsingBar(ArrayList<Breakfast> breakfastList) {
        int kcal = 0, fat = 0, carbo = 0, prot = 0;

        TextView tvEatingDiaryCollapsingFat = getActivity().findViewById(R.id.tvEatingDiaryCollapsingFat);
        TextView tvEatingDiaryCollapsingCarbo = getActivity().findViewById(R.id.tvEatingDiaryCollapsingCarbo);
        TextView tvEatingDiaryCollapsingProt = getActivity().findViewById(R.id.tvEatingDiaryCollapsingProt);
        TextView tvEatingDiaryCollapsingKcal = getActivity().findViewById(R.id.tvEatingDiaryCollapsingKcal);

        for (int i = 0; i < breakfastList.size(); i++) {
            kcal += breakfastList.get(i).getCalories();
            fat += breakfastList.get(i).getFat();
            carbo += breakfastList.get(i).getCarbohydrates();
            prot += breakfastList.get(i).getProtein();
        }
        tvEatingDiaryCollapsingFat.setText(String.valueOf(fat) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingCarbo.setText(String.valueOf(carbo) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingProt.setText(String.valueOf(prot) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingKcal.setText(String.valueOf(kcal) + " " + getActivity().getString(R.string.kcal));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breakfast, container, false);
        recyclerView = view.findViewById(R.id.rvEatingBreakfast);
        ivEmptyState = view.findViewById(R.id.ivEmptyState);
        tvEmptyStateTitle = view.findViewById(R.id.tvEmptyStateTitle);
        tvEmptyStateText = view.findViewById(R.id.tvEmptyStateText);
        return view;
    }

    private class BreakfastItemHolder extends RecyclerView.ViewHolder {
        private TextView tvEatingItemName, tvEatingItemFat, tvEatingItemCarbo,
                tvEatingItemProt, tvEatingItemKcal, tvEatingItemWeight, tvLeterOfProductInDiary;
        private ImageView ivImage;
        private CardView cvInvisibleCardEatingDiary;

        public BreakfastItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
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

        public void bind(Breakfast breakfast) {
            cvInvisibleCardEatingDiary.setVisibility(View.GONE);
            tvEatingItemName.setText(String.valueOf(breakfast.getName()));
            tvEatingItemFat.setText(String.valueOf(breakfast.getFat()) + " г");
            tvEatingItemCarbo.setText(String.valueOf(breakfast.getCarbohydrates()) + " г");
            tvEatingItemProt.setText(String.valueOf(breakfast.getProtein()) + " г");
            tvEatingItemKcal.setText(String.valueOf(breakfast.getCalories()) + " ккал");
            tvEatingItemWeight.setText(String.valueOf(breakfast.getWeight()) + " г");

            if (!breakfast.getUrlOfImages().equals("")) {
                Glide.with(getActivity()).load(breakfast.getUrlOfImages()).into(ivImage);
            } else {
                cvInvisibleCardEatingDiary.setVisibility(View.VISIBLE);
                tvLeterOfProductInDiary.setText(String.valueOf(Character.toUpperCase(breakfast.getName().charAt(0))));
            }


        }
    }

    private class BreakfastItemAdapter extends RecyclerView.Adapter<BreakfastItemHolder> {
        ArrayList<Breakfast> breakfastList;

        public BreakfastItemAdapter(ArrayList<Breakfast> breakfastList) {
            this.breakfastList = breakfastList;
        }

        @NonNull
        @Override
        public BreakfastItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BreakfastItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BreakfastItemHolder holder, int position) {
            holder.bind(breakfastList.get(position));
        }

        @Override
        public int getItemCount() {
            return breakfastList.size();
        }

        public void removeItem(int adapterPosition) {
            breakfastList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            Breakfast.deleteAll(Breakfast.class);
            for (int i = 0; i < breakfastList.size(); i++) {
                breakfastList.get(i).save();
            }
            setNumbersInCollapsingBar(breakfastList);
        }


    }
}
