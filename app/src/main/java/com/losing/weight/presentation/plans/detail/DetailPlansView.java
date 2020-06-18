package com.losing.weight.presentation.plans.detail;

import androidx.recyclerview.widget.RecyclerView;
import com.losing.weight.presentation.global.BaseView;

public interface DetailPlansView extends BaseView {
    void setAdapter(RecyclerView.Adapter adapter);
    void visibilityButtonJoin(boolean value);
    void visibilityButtonLeave(boolean value);
    void startAlert(String plan);
    void showAlertJoinToPlan();
    void sharePlan(String str);
}
