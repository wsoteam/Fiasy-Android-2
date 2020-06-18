package com.losing.weight.presentation.activity;

import android.view.View;

import com.losing.weight.R;
import com.losing.weight.presentation.diary.WidgetsAdapter;
import org.jetbrains.annotations.NotNull;

public class DiaryActivityWidget extends WidgetsAdapter.WidgetView {

  private final ActivityWidget view;

  public DiaryActivityWidget(@NotNull View itemView) {
    super(itemView);
    view = itemView.findViewById(R.id.activityWidget);
  }
}
