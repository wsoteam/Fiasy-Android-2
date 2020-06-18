package com.losing.weight.presentation.profile.norm;

import com.arellomobile.mvp.MvpView;
import com.losing.weight.POJOProfile.Profile;

public interface ChangeNormView extends MvpView {
    void bindFields(Profile profile, String goal, String activity);
    void setGoal(String goal);
    void setActivity(String activity);
    void setDefaultPremParams(String kcal, String fat, String carbo, String prot);
}
