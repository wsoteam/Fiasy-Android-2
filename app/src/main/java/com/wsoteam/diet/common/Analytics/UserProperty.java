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
    public static final String registration = "registration";
    public static final String registration_google = "facebook";
    public static final String registration_fb = "google";
    public static final String registration_email = "email";

    public static final String q_male_status = "male";
    public static final String q_male_status_male = "male";
    public static final String q_male_status_female = "female";

    public static final String q_height_status = "height";

    public static final String q_weight_status = "weight";

    public static final String q_age_status = "age";

    public static final String q_active_status = "active";
    public static final String q_active_status1 = "1";
    public static final String q_active_status2 = "2";
    public static final String q_active_status3 = "3";
    public static final String q_active_status4 = "4";
    public static final String q_active_status5 = "5";
    public static final String q_active_status6 = "6";
    public static final String q_active_status7 = "7";

    public static final String q_goal_status = "goal";
    public static final String q_goal_status1 = "1";
    public static final String q_goal_status2 = "2";
    public static final String q_goal_status3 = "3";
    public static final String q_goal_status4 = "4";

    public static final String premium_status = "premium_status";
    public static final String buy = "premium";
    public static final String not_buy = "free";
    public static final String trial = "trial";
    public static final String preferential = "trial_no_pay";
    public static final String paid = "paid";

    public static final String premium_duration = "premium_duration";
    public static final String trial_duration = "trial_duration";
    public static final String ltv_duration = "ltv_duration";
    public static final String ltv_revenue = "ltv_revenue";

    public static final String user_id = "id";


    public static void setUserProperties(String sex, String height, String weight, String age, String active, String goal, String id) {
        Identify identify = new Identify()
                .set(q_male_status, sex)
                .set(q_height_status, height)
                .set(q_weight_status, weight)
                .set(q_age_status, age)
                .set(q_active_status, active)
                .set(q_goal_status, goal)
                .set(user_id, id);
        Amplitude.getInstance().identify(identify);

        UserAttributes userAttributes = new UserAttributes.Builder()
                .withCustomAttribute(q_male_status, sex)
                .withCustomAttribute(q_height_status, height)
                .withCustomAttribute(q_weight_status, weight)
                .withCustomAttribute(q_age_status, age)
                .withCustomAttribute(q_active_status, active)
                .withCustomAttribute(q_goal_status, id)
                .withCustomAttribute(user_id, id)
                .build();
        Intercom.client().updateUser(userAttributes);
    }

    public static void setPremStatus(String status) {
        Identify identify = new Identify()
                .set(premium_status, status);
        Amplitude.getInstance().identify(identify);

        UserAttributes userAttributes = new UserAttributes.Builder()
                .withCustomAttribute(premium_status, status)
                .build();
        Intercom.client().updateUser(userAttributes);
    }

}
