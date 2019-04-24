package com.wsoteam.diet;

import com.amplitude.api.Amplitude;

import org.json.JSONException;
import org.json.JSONObject;

public class AmplitudaEvents {
    public static final String first_launch = "first_launch";
    public static final String free_enter = "free_enter";


    public static final String close_premium = "close_premium";
    public static final String click_on_buy = "click_on_buy";

    public static final String FIRST_DAY = "FIRST_DAY";
    public static final String FIRST_WEEK = "FIRST_WEEK";
    public static final String FIRST_MONTH = "FIRST_MONTH";

    public static final String start_registration = "start_registration";
    public static final String fill_reg_data = "fill_reg_data";
    public static final String create_acount = "create_acount";
    public static final String view_prem_onboarding = "view_prem_onboarding";
    public static final String buy_prem_onboarding = "buy_prem_onboarding";

    public static final String attempt_add_food = "attempt_add_food";
    public static final String view_search_food = "view_search_food";
    public static final String view_detail_food = "view_detail_food";
    public static final String success_add_food = "success_add_food";

    public static final String view_all_recipes = "view_all_recipes";
    public static final String view_group_recipes = "view_group_recipes";
    public static final String view_prem_recipe = "view_prem_recipe";
    public static final String buy_prem_recipe = "buy_prem_recipe";
    public static final String view_recipe = "view_recipe";

    public static final String view_prem_content = "view_prem_content";
    public static final String buy_prem_content = "buy_prem_content";

    public static final String view_prem_training = "view_prem_training";
    public static final String buy_prem_training = "buy_prem_training";

    public static final String view_prem_free_onboard = "view_prem_free_onboard";
    public static final String buy_prem_free_onboard = "buy_prem_free_onboard";


    public static final String view_profile = "view_profile";
    public static final String view_settings = "view_settings";
    public static final String edit_profile = "edit_profile";


    public static final String REG_STATUS = "REG_STATUS";
    public static final String registered = "registered";
    public static final String unRegistered = "un_registered";

    public static final String PREM_STATUS = "PREM_STATUS";
    public static final String buy = "buy";
    public static final String not_buy = "not_buy";

    public static final String LONG_OF_PREM = "LONG_OF_PREM";
    public static final String PRICE_OF_PREM = "PRICE_OF_PREM";

    public static final String  ONE_MONTH_PRICE = "449р", THREE_MONTH_PRICE = "319р", ONE_YEAR_PRICE = "199р";

    public static final String  view_diary = "view_diary";
    public static final String  reg_offer = "reg_offer";


    public static final String  REG_FROM = "REG_FROM";
    public static final String  reg_from_diary = "reg_from_diary";
    public static final String  reg_from_add_food = "reg_from_add_food";


    public static final String  view_prem = "view_prem";
    public static final String  VIEW_PREM_FROM = "VIEW_PREM_FROM";


    public static final String  buy_prem = "buy_prem";
    public static final String  BUY_PREM_FROM = "BUY_PREM_FROM";


    public static final String  REG_VIA = "REG_VIA";

    public static final String  REG_OFFER_WHEN = "REG_OFFER_WHEN";
    public static final String  open_profile = "open_profile";
    public static final String  open_trainer = "open_trainer";
    public static final String  open_articles = "open_articles";
    public static final String  add_food = "add_food";


    public static final String  CLICK_BUY_WITH = "CLICK_BUY_WITH";



    public static void logEventRegistration(String from){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(REG_FROM, from);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(start_registration, eventProperties);
    }

    public static void logEventViewPremium(String from, String abVersion){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(VIEW_PREM_FROM, from);
            eventProperties.put(ABConfig.PREMIUM_VERSION, abVersion);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(view_prem, eventProperties);
    }

    public static void logEventBuyPremium(String from, String abVersion){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(BUY_PREM_FROM, from);
            eventProperties.put(ABConfig.PREMIUM_VERSION, abVersion);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(buy_prem, eventProperties);
    }

    public static void logEventReg(String via){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(REG_VIA, via);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(create_acount, eventProperties);
    }

    public static void logEventRegOffer(String when){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(REG_OFFER_WHEN, when);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(reg_offer, eventProperties);
    }

    public static void logEventClickBuy(String plan){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(CLICK_BUY_WITH, plan);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(click_on_buy, eventProperties);
    }

}
