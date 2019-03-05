package com.wsoteam.diet.BranchProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.R;

public class ActivityHelp extends AppCompatActivity {
    private RecyclerView rvHelp;
    private int[] arrayOfIdOfImages = new int[] {R.drawable.image_min_level, R.drawable.image_upmin_level,
            R.drawable.image_premedium_level, R.drawable.image_medium_level, R.drawable.image_upmedium_level,
            R.drawable.image_hard_level, R.drawable.image_super_level};

    private int[] arrayOfIdOfImagesTitle = new int[] {R.drawable.image_min_level_title, R.drawable.image_upmin_level_title,
            R.drawable.image_premedium_level_title, R.drawable.image_medium_level_title, R.drawable.image_upmedium_level_title,
            R.drawable.image_hard_level_title, R.drawable.image_super_level_title};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        rvHelp = findViewById(R.id.rvHelp);
        rvHelp.setLayoutManager(new LinearLayoutManager(this));
        rvHelp.setAdapter(new ItemAdapter(arrayOfIdOfImages, getResources().getStringArray(R.array.namesOfHelpList),
                getResources().getStringArray(R.array.textsOfHelpList), arrayOfIdOfImagesTitle));
    }

    private class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tvItemHelpListName, tvItemHelpListText;
        private ImageView ivItemHelpListImage, ivItemHelpListTitle;
        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_help_list, viewGroup, false));
            tvItemHelpListName = itemView.findViewById(R.id.tvItemHelpListName);
            tvItemHelpListText = itemView.findViewById(R.id.tvItemHelpListText);
            ivItemHelpListImage = itemView.findViewById(R.id.ivItemHelpListImage);
            ivItemHelpListTitle = itemView.findViewById(R.id.ivItemHelpListTitle);
        }

        public void bind(int idOfImage, String name, String text, int idOfImageTitle) {
            tvItemHelpListName.setText(name);
            tvItemHelpListText.setText(text);
            Glide.with(ActivityHelp.this).load(idOfImage).into(ivItemHelpListImage);
            Glide.with(ActivityHelp.this).load(idOfImageTitle).into(ivItemHelpListTitle);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder>{
        int[] arrayIdOfImages, arrayOfImagesTitle;
        String[] arrayOfNames, arrayOfTexts;

        public ItemAdapter(int[] arrayIdOfImages, String[] arrayOfNames, String[] arrayOfTexts, int[] arrayIdOfImagesTitle) {
            this.arrayIdOfImages = arrayIdOfImages;
            this.arrayOfNames = arrayOfNames;
            this.arrayOfTexts = arrayOfTexts;
            this.arrayOfImagesTitle = arrayIdOfImagesTitle;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityHelp.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.bind(arrayIdOfImages[position], arrayOfNames[position],
                    arrayOfTexts[position], arrayOfImagesTitle[position]);
        }

        @Override
        public int getItemCount() {
            return arrayIdOfImages.length;
        }
    }
}
