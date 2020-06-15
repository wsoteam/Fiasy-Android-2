package com.wsoteam.diet.presentation.activity;

import android.view.View;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.diary.WidgetsAdapter;
import org.jetbrains.annotations.NotNull;

public class DiaryActivityWidget extends WidgetsAdapter.WidgetView {

  private final ActivityWidget view;

  public DiaryActivityWidget(@NotNull View itemView) {
    super(itemView);
    view = itemView.findViewById(R.id.activityWidget);
  }
}
