package com.wsoteam.diet.presentation.main.water;

import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.global.BasePresenter;



@InjectViewState
public class WaterPresenter extends BasePresenter<WaterView> {


    public WaterPresenter() {

    }

    float getUsersMaxWater() {
        if (UserDataHolder.getUserData() != null
            && UserDataHolder.getUserData().getProfile() != null
            && UserDataHolder.getUserData().getProfile().getMaxWater() >= 1.5
            && UserDataHolder.getUserData().getProfile().getMaxWater() <= 3) {
            return UserDataHolder.getUserData().getProfile().getMaxWater();
        } else {
            return 2f;
        }
    }

    void saveUsersMaxWater(float water){
        WorkWithFirebaseDB.setMaxWater(water);
    }

}