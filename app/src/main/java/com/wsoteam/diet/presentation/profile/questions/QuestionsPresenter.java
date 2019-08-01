package com.wsoteam.diet.presentation.profile.questions;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import android.text.TextUtils;
import android.view.View;

import com.amplitude.api.Amplitude;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.helpers.BodyCalculates;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.global.Screens;

import java.util.Calendar;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class QuestionsPresenter extends BasePresenter<QuestionsView> {

    private static final int MIN_AGE = 9;
    private static final int MAX_AGE = 200;
    private static final int MIN_HEIGHT = 100;
    private static final int MAX_HEIGHT = 300;
    private static final int MIN_WEIGHT = 30;
    private static final int MAX_WEIGHT = 300;
    private Context context;
    private Router router;

    public QuestionsPresenter(Context context, Router router) {
        this.router = router;
        this.context = context;
    }

    void calculate(String name, String age, boolean isFemale, String height, String diff, String activity, String weight) {
        if (!TextUtils.isEmpty(age) && Integer.parseInt(age) >= MIN_AGE && Integer.parseInt(age) <= MAX_AGE) {
            if (!TextUtils.isEmpty(height) && Integer.parseInt(height) >= MIN_HEIGHT && Integer.parseInt(height) <= MAX_HEIGHT) {
                if (!TextUtils.isEmpty(weight) && Double.parseDouble(weight) >= MIN_WEIGHT && Double.parseDouble(weight) <= MAX_WEIGHT) {
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    String[] fullName = name.split(" ");
                    String lastName = "";
                    if (fullName.length > 0)
                        name = fullName[0];
                    if (fullName.length > 1)
                        lastName = fullName[1];
                    Profile profile = new Profile(name, lastName, isFemale, Integer.parseInt(age), Integer.parseInt(height),
                            Double.parseDouble(weight), 0,
                            activity, "", 0, 0, 0,
                            0, 0, diff, day, month, year);
                    Profile profileFinal = BodyCalculates.calculate(context, profile);
                    getViewState().saveProfile(profileFinal);
                    getViewState().showMessage(context.getString(R.string.profile_saved));
                } else {
                    getViewState().showMessage(context.getString(R.string.spk_check_weight));
                }
            } else {
                getViewState().showMessage(context.getString(R.string.spk_check_your_height));
            }
        } else {
            getViewState().showMessage(context.getString(R.string.spk_check_your_age));
        }
    }

    void saveProfile(boolean isNeedShowOnboard, Profile profile, boolean createProfile) {
        Amplitude.getInstance().logEvent(AmplitudaEvents.fill_reg_data);
        if (createProfile) {
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
        } else {
            if (profile != null) {
                WorkWithFirebaseDB.putProfileValue(profile);
            }
        }
    }

    void changeState(boolean isCheck, AppCompatCheckedTextView textView, AppCompatCheckedTextView dot, View line) {
        if (textView != null)
            textView.setChecked(isCheck);
        if (dot != null)
            dot.setChecked(isCheck);
        if (line != null)
            changeLineColor(line, isCheck);
        getViewState().changeNextState();
    }

    private void changeLineColor(View line, boolean selected) {
        if (selected)
            line.setBackgroundColor(context.getResources().getColor(R.color.orange_light));
        else
            line.setBackgroundColor(context.getResources().getColor(R.color.gray_light2));
    }

}