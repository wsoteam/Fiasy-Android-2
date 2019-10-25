package com.wsoteam.diet.presentation.search.results.controllers.suggestions;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;

public class SuggestVH extends RecyclerView.ViewHolder {
  @BindView(R.id.tvTitle) TextView tvTitle;

  public SuggestVH(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.view_suggest_item, viewGroup, false));
    ButterKnife.bind(this, itemView);
  }

  public void bind(String title, String query) {
    tvTitle.setText(getPaintedString(title, query));
  }

  private Spannable getPaintedString(String title, String query) {
    Spannable spannable = new SpannableString(title);
    spannable.setSpan(new ForegroundColorSpan(itemView.getResources().getColor(R.color.srch_suggest_painted_string)), 0, query.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    );
    return spannable;
  }
}
