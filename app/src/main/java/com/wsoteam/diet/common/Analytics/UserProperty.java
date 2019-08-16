package com.wsoteam.diet.common.Analytics;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.Sync.UserDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;

public class UserProperty {
    public static final String reg_social = "reg_social";
    public static final String reg_social_facebook = "facebook";
    public static final String reg_social_google = "google";

    public static final String reg_enter_social = "reg_enter_social";
    public static final String reg_enter_social_facebook = "facebook";
    public static final String reg_enter_social_google = "google";

    public static final String reg_reg_social = "reg_reg_social";
    public static final String reg_reg_enter_social_facebook = "facebook";
    public static final String reg_reg_enter_social_google = "google";

    public static final String q_male_status = "q_male_status";
    public static final String q_male_status_male = "male";
    public static final String q_male_status_female = "female";

    public static final String q_height_status = "q_height_status";

    public static final String q_weight_status = "q_weight_status";

    public static final String q_age_status = "q_age_status";

    public static final String q_active_status = "q_active_status";
    public static final String q_active_status1 = "1";
    public static final String q_active_status2 = "2";
    public static final String q_active_status3 = "3";
    public static final String q_active_status4 = "4";
    public static final String q_active_status5 = "5";
    public static final String q_active_status6 = "6";
    public static final String q_active_status7 = "7";

    public static final String q_goal_status = "q_goal_status";
    public static final String q_goal_status1 = "1";
    public static final String q_goal_status2 = "2";
    public static final String q_goal_status3 = "3";
    public static final String q_goal_status4 = "4";

    public static final String premium_status = "premium_status";
    public static final String premium_duration = "premium_duration";
    public static final String trial_duration = "trial_duration";
    public static final String ltv_duration = "ltv_duration";
    public static final String ltv_revenue = "ltv_revenue";

    public static void setRegUserProperties(String regType) {
        Identify identify = new Identify().set(reg_social, regType);
        Amplitude.getInstance().identify(identify);

        try {
            UserAttributes userAttributes = new UserAttributes.Builder()
                    .withName(UserDataHolder.getUserData().getProfile().getFirstName())
                    .withEmail(currentUser.getEmail())
                    .build();
            Intercom.client().updateUser(userAttributes);
        } catch (Exception e) {

        }
    }

}
