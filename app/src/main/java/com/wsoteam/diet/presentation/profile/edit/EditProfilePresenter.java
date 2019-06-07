package com.wsoteam.diet.presentation.profile.edit;

import android.content.Context;
import android.text.TextUtils;

import com.amplitude.api.Amplitude;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.helpers.BodyCalculates;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.global.Screens;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class EditProfilePresenter extends BasePresenter<EditProfileView> {

    private static final int MIN_AGE = 9;
    private static final int MAX_AGE = 200;
    private static final int MIN_HEIGHT = 100;
    private static final int MAX_HEIGHT = 300;
    private static final int MIN_WEIGHT = 30;
    private static final int MAX_WEIGHT = 300;
    private Context context;
    private Router router;

    public EditProfilePresenter(Context context, Router router) {
        this.router = router;
        this.context = context;
    }

    void calculate(int rgFemaleOrMale, String edtAge, String edtHeight, String edtWeight, String sportActivity, String dif_level) {
        int age = Integer.parseInt(edtAge);
        int height = Integer.parseInt(edtHeight);
        double weight = Double.parseDouble(edtWeight);
        if (rgFemaleOrMale != -1) {
            if (!TextUtils.isEmpty(edtAge) && age >= MIN_AGE && age <= MAX_AGE) {
                if (!TextUtils.isEmpty(edtHeight) && height >= MIN_HEIGHT && height <= MAX_HEIGHT) {
                    if (!TextUtils.isEmpty(edtWeight) && weight >= MIN_WEIGHT && weight <= MAX_WEIGHT) {
                        calculate(rgFemaleOrMale, age, height, weight, sportActivity, dif_level);
                    } else {
                        getViewState().showMessage(context.getString(R.string.spk_check_weight));
                    }
                } else {
                    getViewState().showMessage(context.getString(R.string.spk_check_your_height));
                }
            } else {
                getViewState().showMessage(context.getString(R.string.spk_check_your_age));
            }
        } else {
            getViewState().showMessage(context.getString(R.string.spk_choise_your_gender));
        }
    }

    private void calculate(int rgFemaleOrMaleId, int age, int height, double weight, String sportActivity, String dif_level) {
        boolean isFemale = false;

        switch (rgFemaleOrMaleId) {
            case R.id.rdSpkFemale:
                isFemale = true;
                break;
            case R.id.rdSpkMale:
                isFemale = false;
                break;
        }

        Profile profile = BodyCalculates.calculate(context, weight, height, age, isFemale, sportActivity, dif_level);
        getViewState().saveProfile(profile);
        getViewState().showMessage(context.getString(R.string.profile_saved));
    }

    void saveProfile(boolean isNeedShowOnboard, Profile profile) {
        Amplitude.getInstance().logEvent(AmplitudaEvents.fill_reg_data);
        if (isNeedShowOnboard) {
            Box box = new Box();
            box.setBuyFrom(AmplitudaEvents.buy_prem_onboarding);
            box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
            box.setOpenFromIntrodaction(true);
            box.setOpenFromPremPart(false);
            router.navigateTo(new Screens.AuthScreen(profile, box));
        } else {
            router.navigateTo(new Screens.AuthScreen(profile));
        }
    }

    void onHelpClicked() {
        router.navigateTo(new Screens.HelpScreen());
    }

}