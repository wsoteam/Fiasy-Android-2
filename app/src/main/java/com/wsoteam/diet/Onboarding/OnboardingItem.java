package com.wsoteam.diet.Onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.R;

public class OnboardingItem {

    Context context;
    private int title;
    private int description;
    private int img;

    public OnboardingItem(Context context, int title, int description, int img){
        this.title = title;
        this.description = description;
        this.img = img;
        this.context = context;
    }

    public View getView(){
        View view = LayoutInflater.from(context).inflate(R.layout.onboarding_item, null);
        TextView titleTextView = view.findViewById(R.id.onboarding_item_title);
        TextView descriptionTextView = view.findViewById(R.id.onboarding_item_description);
        ImageView imageView = view.findViewById(R.id.onboarding_item_images);

        Glide
                .with(context)
                .load(img)
                .into(imageView);
//        imageView.setImageResource(img);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        return view;
    }
}
