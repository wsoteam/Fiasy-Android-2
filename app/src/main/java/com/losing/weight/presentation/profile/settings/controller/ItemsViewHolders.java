package com.losing.weight.presentation.profile.settings.controller;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.losing.weight.R;
import com.losing.weight.utils.DrawableUtilsKt;

public class ItemsViewHolders extends RecyclerView.ViewHolder{
    @BindView(R.id.ivIconItem) ImageView ivIconItem;
    @BindView(R.id.ivArrow) ImageView ivArrow;
    @BindView(R.id.textView89) TextView tvName;

    public ItemsViewHolders(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.item_settings, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public void bind(String name, int drawable, int color) {

        tvName.setText(name);
        tvName.setTextColor(color);

        ivIconItem.setImageDrawable(DrawableUtilsKt.getVectorIcon(itemView.getContext(), drawable));

    }
}
