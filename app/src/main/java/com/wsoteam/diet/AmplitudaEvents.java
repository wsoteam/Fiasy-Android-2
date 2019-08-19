package com.wsoteam.diet;

import com.amplitude.api.Amplitude;

import org.json.JSONException;
import org.json.JSONObject;

public class AmplitudaEvents {
    public static final String buy_prem_onboarding = "buy_prem_onboarding";

    public static final String view_group_recipes = "view_group_recipes";
    public static final String view_prem_recipe = "view_prem_recipe";
    public static final String buy_prem_recipe = "buy_prem_recipe";

    public static final String view_prem_content = "view_prem_content";
    public static final String buy_prem_content = "buy_prem_content";

    public static final String view_prem_settings = "view_prem_settings";
    public static final String buy_prem_settings = "buy_prem_settings";

    public static final String view_prem_elements = "view_prem_elements";
    public static final String buy_prem_elements = "buy_prem_elements";

    public static final String view_prem_free_onboard = "view_prem_free_onboard";
    public static final String buy_prem_free_onboard = "buy_prem_free_onboard";


    public static final String  view_prem = "view_prem";
    public static final String  VIEW_PREM_FROM = "VIEW_PREM_FROM";


    public static final String  buy_prem = "buy_prem";
    public static final String  BUY_PREM_FROM = "BUY_PREM_FROM";


    public static final String  TYPE_PURCHASE = "TYPE_PURCHASE";


    public static void logEventViewPremium(String from, String abVersion){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(VIEW_PREM_FROM, from);
            eventProperties.put(ABConfig.PREMIUM_VERSION, abVersion);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(view_prem, eventProperties);
    }

    public static void logEventBuyPremium(String from, String abVersion, String typePurchase){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(BUY_PREM_FROM, from);
            eventProperties.put(ABConfig.PREMIUM_VERSION, abVersion);
            eventProperties.put(TYPE_PURCHASE, typePurchase);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(buy_prem, eventProperties);
    }
}