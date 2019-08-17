package com.wsoteam.diet.presentation.plans.detail;

import android.support.v7.widget.RecyclerView;
import com.wsoteam.diet.presentation.global.BaseView;

public interface DetailPlansView extends BaseView {
    void setAdapter(RecyclerView.Adapter adapter);
    void visibilityButtonJoin(boolean value);
    void startAlert(String plan);
    void showAlertJoinToPlan();
}
