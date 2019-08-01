package com.wsoteam.diet.BranchOfCalculating;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

public class ActivityListOfCalculating extends AppCompatActivity {

    private RecyclerView rvListOfCalculating;
    private String[] listOfTitles;
    private String[] listOfDescriptions;
    private final int NUMBER_OF_IMT = 0, NUMBER_OF_SPK = 1;
    private InterstitialAd interstitialAd;
    private int[] backgroundDrawables = new int[]{R.drawable.image_imt, R.drawable.image_spk};

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillDataForList();
        setContentView(R.layout.activity_list_of_calculating);
        rvListOfCalculating = findViewById(R.id.rvListOfCalculating);
        rvListOfCalculating.setLayoutManager(new LinearLayoutManager(this));
        rvListOfCalculating.setAdapter(new CalculatingAdapter(listOfTitles, listOfDescriptions));

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Список калькуляторов");


    }

    private void fillDataForList() {
        listOfTitles = getResources().getStringArray(R.array.titles_of_calculating_list);
        listOfDescriptions = getResources().getStringArray(R.array.descriptions_of_calculating_list);
    }


    private class CaclulatingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description;
        ImageView ivBackground;

        public CaclulatingViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_of_list_calculating, viewGroup, false));

            title = itemView.findViewById(R.id.tvTitleOfItemListCalculating);
            description = itemView.findViewById(R.id.tvDescriptionOfItemListCalculating);
            ivBackground = itemView.findViewById(R.id.ivCalculateItemBackImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (getAdapterPosition()) {
                case NUMBER_OF_IMT:
                    Intent intentIMT = new Intent(ActivityListOfCalculating.this, ActivityCalculatorIMT.class);
                    startActivity(intentIMT);
                    break;
                case NUMBER_OF_SPK:
                    Intent intentSPK = new Intent(ActivityListOfCalculating.this, ActivityCalculatorSPK.class);
                    startActivity(intentSPK);
                    break;
            }

        }

        public void bind(String title, String desription) {
            Glide.with(ActivityListOfCalculating.this).load(backgroundDrawables[getAdapterPosition()]).into(ivBackground);
            this.title.setText(title);
            this.description.setText(desription);
        }
    }

    private class CalculatingAdapter extends RecyclerView.Adapter<CaclulatingViewHolder> {
        String[] titles, desriptions;

        public CalculatingAdapter(String[] titles, String[] desriptions) {
            this.titles = titles;
            this.desriptions = desriptions;
        }

        @NonNull
        @Override
        public CaclulatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListOfCalculating.this);
            return new CaclulatingViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CaclulatingViewHolder holder, int position) {
            holder.bind(titles[position], desriptions[position]);
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}
