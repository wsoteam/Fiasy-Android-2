package com.wsoteam.diet.presentation.search.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.R;

public class SectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  @BindView(R.id.ivLeft) ImageView ivLeft;
  @BindView(R.id.tvName) TextView tvName;
  private ISections callback;

  public SectionViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.item_section, viewGroup, false));
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    callback.openSection(getAdapterPosition());
  }

  public void bind(String name, int resourceId, ISections iSections) {
    Picasso.get().load(resourceId).into(ivLeft);
    tvName.setText(name);
    callback = iSections;
  }
}
