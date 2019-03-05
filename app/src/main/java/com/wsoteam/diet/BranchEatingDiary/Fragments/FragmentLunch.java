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
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentLunch extends Fragment {
    private RecyclerView recyclerView;
    private LunchItemAdapter lunchItemAdapter;
    private ImageView ivEmptyState;
    private TextView tvEmptyStateTitle, tvEmptyStateText;

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Lunch> lunchArrayList = Lunch.listAll(Lunch.class);
        if (lunchArrayList.size() == 0) {
            showEmptyStateViews();
        } else {
            hideEmptyStateViews();
        }
        setNumbersInCollapsingBar((ArrayList<Lunch>) lunchArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lunchItemAdapter = new LunchItemAdapter((ArrayList<Lunch>) lunchArrayList);
        recyclerView.setAdapter(lunchItemAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                lunchItemAdapter.removeItem(viewHolder.getAdapterPosition());
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

    private void setNumbersInCollapsingBar(ArrayList<Lunch> lunchList) {
        int kcal = 0, fat = 0, carbo = 0, prot = 0;

        TextView tvEatingDiaryCollapsingFat = getActivity().findViewById(R.id.tvEatingDiaryCollapsingFat);
        TextView tvEatingDiaryCollapsingCarbo = getActivity().findViewById(R.id.tvEatingDiaryCollapsingCarbo);
        TextView tvEatingDiaryCollapsingProt = getActivity().findViewById(R.id.tvEatingDiaryCollapsingProt);
        TextView tvEatingDiaryCollapsingKcal = getActivity().findViewById(R.id.tvEatingDiaryCollapsingKcal);

        for (int i = 0; i < lunchList.size(); i++) {
            kcal += lunchList.get(i).getCalories();
            fat += lunchList.get(i).getFat();
            carbo += lunchList.get(i).getCarbohydrates();
            prot += lunchList.get(i).getProtein();
        }
        tvEatingDiaryCollapsingFat.setText(String.valueOf(fat) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingCarbo.setText(String.valueOf(carbo) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingProt.setText(String.valueOf(prot) + " " + getActivity().getString(R.string.gramm));
        tvEatingDiaryCollapsingKcal.setText(String.valueOf(kcal) + " " + getActivity().getString(R.string.kcal));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lunch, container, false);
        recyclerView = view.findViewById(R.id.rvEatingLunch);
        ivEmptyState = view.findViewById(R.id.ivEmptyState);
        tvEmptyStateTitle = view.findViewById(R.id.tvEmptyStateTitle);
        tvEmptyStateText = view.findViewById(R.id.tvEmptyStateText);
        return view;
    }

    private class LunchItemHolder extends RecyclerView.ViewHolder {
        private TextView tvEatingItemName, tvEatingItemFat, tvEatingItemCarbo,
                tvEatingItemProt, tvEatingItemKcal, tvEatingItemWeight, tvLeterOfProductInDiary;
        private ImageView ivImage;
        private CardView cvInvisibleCardEatingDiary;

        public LunchItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
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

        public void bind(Lunch lunch) {
            cvInvisibleCardEatingDiary.setVisibility(View.GONE);
            tvEatingItemName.setText(String.valueOf(lunch.getName()));
            tvEatingItemFat.setText(String.valueOf(lunch.getFat()) + " г");
            tvEatingItemCarbo.setText(String.valueOf(lunch.getCarbohydrates()) + " г");
            tvEatingItemProt.setText(String.valueOf(lunch.getProtein()) + " г");
            tvEatingItemKcal.setText(String.valueOf(lunch.getCalories()) + " ккал");
            tvEatingItemWeight.setText(String.valueOf(lunch.getWeight()) + " г");

            if (!lunch.getUrlOfImages().equals("")){
                Glide.with(getActivity()).load(lunch.getUrlOfImages()).into(ivImage);
            }else {
                cvInvisibleCardEatingDiary.setVisibility(View.VISIBLE);
                tvLeterOfProductInDiary.setText(String.valueOf(Character.toUpperCase(lunch.getName().charAt(0))));
            }

        }
    }

    private class LunchItemAdapter extends RecyclerView.Adapter<LunchItemHolder> {
        ArrayList<Lunch> lunchList;

        public LunchItemAdapter(ArrayList<Lunch> lunchList) {
            this.lunchList = lunchList;
        }

        @NonNull
        @Override
        public LunchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LunchItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull LunchItemHolder holder, int position) {
            holder.bind(lunchList.get(position));
        }

        @Override
        public int getItemCount() {
            return lunchList.size();
        }

        public void removeItem(int adapterPosition) {
            lunchList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            Lunch.deleteAll(Lunch.class);
            for (int i = 0; i < lunchList.size(); i++) {
                lunchList.get(i).save();
            }
            setNumbersInCollapsingBar(lunchList);
        }
    }

}
