package com.wsoteam.diet.BranchProfile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {
    private CircleImageView civProfile;
    private ImageButton ibProfileEdit, ibProfileBack;
    private TextView tvProfileName, tvProfileOld, tvProfileGender,
            tvProfileLifestyle, tvProfileWeight, tvProfileLevel,
            tvProfileHeight, tvProfileFirstEnter, tvProfileChangeWeight;

    private RecyclerView rvProfileMainParams;
    private ImageView ivProfileChangeWeight;

    private ItemAdapter itemAdapter;

    private int[] arrayOfDrawabaleArrowForChangeWeight = new int[]{R.drawable.ic_decrease_weight, R.drawable.ic_increase_weight};
    private int[] arrayOfBackgroundDrawables = new int[]{R.drawable.background_item_profile_kcal,
            R.drawable.background_item_profile_water, R.drawable.background_item_profile_fat,
            R.drawable.background_item_profile_carbo, R.drawable.background_item_profile_prot};
    private int[] arrayOfIcon = new int[]{R.drawable.ic_item_profile_kcal,
            R.drawable.ic_item_profile_water, R.drawable.ic_item_profile_fat,
            R.drawable.ic_item_profile_carbo, R.drawable.ic_item_profile_protein};
    private int[] arrayOfGradients = new int[]{R.drawable.gradient_filter_profile_kcal,
            R.drawable.gradient_filter_profile_water, R.drawable.gradient_filter_profile_fat,
            R.drawable.gradient_filter_profile_carbo, R.drawable.gradient_filter_profile_prot};


    @Override
    protected void onResume() {
        super.onResume();
        if (Profile.count(Profile.class) == 1) {
            Profile profile = Profile.last(Profile.class);
            updateUIOfList(profile);
            fillViewsIfProfileNotNull(profile);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        civProfile = findViewById(R.id.civProfile);
        ibProfileEdit = findViewById(R.id.ibProfileEdit);
        ibProfileBack = findViewById(R.id.ibProfileBack);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileOld = findViewById(R.id.tvProfileOld);
        tvProfileGender = findViewById(R.id.tvProfileGender);
        tvProfileLifestyle = findViewById(R.id.tvProfileLifestyle);
        tvProfileWeight = findViewById(R.id.tvProfileWeight);
        tvProfileLevel = findViewById(R.id.tvProfileLevel);
        tvProfileHeight = findViewById(R.id.tvProfileHeight);
        tvProfileFirstEnter = findViewById(R.id.tvProfileFirstEnter);
        tvProfileChangeWeight = findViewById(R.id.tvProfileChangeWeight);
        ivProfileChangeWeight = findViewById(R.id.ivProfileChangeWeight);

        rvProfileMainParams = findViewById(R.id.rvProfileMainParams);

        ibProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ibProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityProfile.this, ActivityEditProfile.class);
                startActivity(intent);
            }
        });

    }

    private void updateUIOfList(Profile profile) {
        itemAdapter = new ItemAdapter(profile);
        rvProfileMainParams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvProfileMainParams.setAdapter(itemAdapter);
    }

    private void fillViewsIfProfileNotNull(Profile profile) {
        String day = "0", month = "0";

        tvProfileName.setText(profile.getFirstName() + " " + profile.getLastName());
        tvProfileOld.setText(String.valueOf(profile.getAge()));
        if (profile.isFemale()) {
            tvProfileGender.setText("Женщина");
        } else {
            tvProfileGender.setText("Мужчина");
        }
        tvProfileLifestyle.setText(profile.getExerciseStress());
        tvProfileWeight.setText(String.valueOf(profile.getWeight()) + " " + getString(R.string.kg));
        tvProfileLevel.setText(profile.getDifficultyLevel());
        tvProfileHeight.setText(String.valueOf(profile.getHeight()) + " " + getString(R.string.cm));
        if (profile.getNumberOfDay() < 10) {
            day = "0" + String.valueOf(profile.getNumberOfDay());
        } else {
            day = String.valueOf(profile.getNumberOfDay());
        }
        if ((profile.getMonth() + 1) < 10) {
            month = "0" + String.valueOf(profile.getMonth() + 1);
        } else {
            month = String.valueOf(profile.getMonth() + 1);
        }
        tvProfileFirstEnter.setText(day + "."
                + month + "." + String.valueOf(profile.getYear()));

        if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_easy))) {
            tvProfileLevel.setTextColor(getResources().getColor(R.color.level_easy));
        } else {
            if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_normal))) {
                tvProfileLevel.setTextColor(getResources().getColor(R.color.level_normal));
            } else {
                tvProfileLevel.setTextColor(getResources().getColor(R.color.level_hard));
            }
        }
        if (!profile.getPhotoUrl().equals("default")) {
            Uri uri = Uri.parse(profile.getPhotoUrl());
            Glide.with(this).load(uri).into(civProfile);
        }
        if (profile.getLoseWeight() < 0) {
            Glide.with(ActivityProfile.this).load(arrayOfDrawabaleArrowForChangeWeight[0]).into(ivProfileChangeWeight);
            tvProfileChangeWeight.setText(String.valueOf(profile.getLoseWeight()) + " " + getResources().getString(R.string.kg));
        } else {
            Glide.with(ActivityProfile.this).load(arrayOfDrawabaleArrowForChangeWeight[1]).into(ivProfileChangeWeight);
            tvProfileChangeWeight.setText("+" + String.valueOf(profile.getLoseWeight()) + " " + getResources().getString(R.string.kg));
        }

        Log.e("LOL", String.valueOf(profile.getLoseWeight()));
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfileItemIcon, ivProfileItemBackground, ivProfileItemFilter;
        private TextView tvProfileItemName, tvProfileItemSubstring;


        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_main_params_profile, viewGroup, false));
            ivProfileItemIcon = itemView.findViewById(R.id.ivProfileItemIcon);
            ivProfileItemFilter = itemView.findViewById(R.id.ivProfileItemFilter);
            ivProfileItemBackground = itemView.findViewById(R.id.ivProfileItemBackground);
            tvProfileItemName = itemView.findViewById(R.id.tvProfileItemName);
            tvProfileItemSubstring = itemView.findViewById(R.id.tvProfileItemSubstring);
        }

        public void bind(String nameOfParam, int countOfMainParam, String afterMainParam,
                         int idBackgroud, int idFilter, int idIcon) {
            Glide.with(ActivityProfile.this).load(idFilter).into(ivProfileItemFilter);
            tvProfileItemName.setText(nameOfParam);
            tvProfileItemSubstring.setText(String.valueOf(countOfMainParam) + " " + afterMainParam);
            Glide.with(ActivityProfile.this).load(idBackgroud).into(ivProfileItemBackground);
            Glide.with(ActivityProfile.this).load(idIcon).into(ivProfileItemIcon);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        Profile profile;

        public ItemAdapter(Profile profile) {
            this.profile = profile;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityProfile.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            int mainParam = 0;
            switch (position) {
                case 0:
                    mainParam = profile.getMaxKcal();
                    break;
                case 1:
                    mainParam = profile.getWaterCount();
                    break;
                case 2:
                    mainParam = profile.getMaxFat();
                    break;
                case 3:
                    mainParam = profile.getMaxCarbo();
                    break;
                case 4:
                    mainParam = profile.getMaxProt();
                    break;
            }
            holder.bind(getResources().getStringArray(R.array.namesOfMainParam)[position],
                    mainParam, getResources().getStringArray(R.array.afterMainParam)[position],
                    arrayOfBackgroundDrawables[position], arrayOfGradients[position], arrayOfIcon[position]);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

}
