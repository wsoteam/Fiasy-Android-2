package com.losing.weight.presentation.main.water;

import com.arellomobile.mvp.InjectViewState;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.Sync.WorkWithFirebaseDB;
import com.losing.weight.presentation.global.BasePresenter;



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
        if (UserDataHolder.getUserData() != null
            && UserDataHolder.getUserData().getProfile() != null
            && UserDataHolder.getUserData().getProfile().getMaxWater() >= 1.5
            && UserDataHolder.getUserData().getProfile().getMaxWater() <= 3) {
            UserDataHolder.getUserData().getProfile().setMaxWater(water);
        }
    }

}