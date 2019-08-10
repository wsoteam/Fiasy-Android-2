package com.wsoteam.diet.common.Analytics;

import com.amplitude.api.Amplitude;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.experimental.Intercom;

public class Events {


    //ONBOARD
    public static final String ONBOARING_NEXT = "onboarding_next";
    public static final String ONBOARING_SKIP = "onboarding_skip";
    public static final String REGISTRATION_SUCCESS = "registration_success";
    public static final String REGISTRATION_ERROR = "registration_error";
    public static final String ENTER_SUCCESS = "enter_success";
    public static final String ENTER_ERROR = "enter_error";
    public static final String REGISRTATION_PRIVACY = "registration_privacy";
    public static final String RESEND_SUCCESS = "resend_success";
    public static final String RESEND_ERROR = "resend_error";
    public static final String QUESTION_NEXT = "question_next";


    //PURCHASE
    public static final String TRIAL_SUCCES = "trial_success";
    public static final String TRIAL_ERROR = "trial_error";
    public static final String PURCHASE_SUCCESS = "purchase_success";
    public static final String PURCHASE_ERROR = "purchase_error";
    public static final String REVENUE = "revenue";


    //FOOD
    public static final String ADD_FOOD_SUCCESS = "add_food_success";
    public static final String EDIT_FOOD = "edit_food";
    public static final String DELETE_FOOD = "delete_food";
    public static final String FOOD_SEARCH = "food_search";
    public static final String VIEW_PRODUCT_PAGE = "view_product_page";
    public static final String PRODUCT_PAGE_FAVORITES = "product_page_favorites";
    public static final String PRODUCT_PAGE_SHARE = "product_page_share";
    public static final String PRODUCT_PAGE_BUGSEND = "product_page_bugsend";
    public static final String PRODUCT_PAGE_MICRO = "product_page_micro";
    public static final String CUSTOM_PRODUCT_SUCCESS = "custom_product_success";
    public static final String CUSTOM_RECIPE_SUCCESS = "custom_recipe_success";
    public static final String CUSTOM_TEMPLATE_SUCCESS = "custom_template_success";


    //RECIPE
    public static final String VIEW_RECIPE = "view_recipe";
    public static final String RECIPE_CATEGORY = "recipe_category";
    public static final String RECIPE_FAVORITES = "recipe_favorites";
    public static final String RECIPE_ADD_SUCCES = "recipe_add_success";


    //PROFILE
    public static final String VIEW_PROFILE = "view_profile";
    public static final String PROFILE_LOGOUT = "profile_logout";


    //INTERCOM
    public static final String INTERCOM_CHAT = "intercom_chat";


    //ARTICLES
    public static final String CHOOSE_ARTICLES = "choose_articles";
    public static final String VIEW_ARTICLES = "view_articles";
    public static final String SELECT_DIET = "select_diet";
    public static final String SELECT_TRAINING = "select_training";


    //DIET
    //public static final String SELECT_DIET_FILTRES = "select_diet_filters";
    //public static final String SHARE_DIET = "share_diet";


    //TRAINING
    //public static final String SELECT_TRAINING = "select_training";
    //public static final String SHARE_TRAINING = "share_training";

    //Amplitude events
    public static void logViewFood(String id) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.product_item, id);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_PRODUCT_PAGE, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.product_item, id);
        Intercom.client().logEvent(VIEW_PRODUCT_PAGE, eventData);
    }


    public static void logAddFavorite(String id) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.favorites, id);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(PRODUCT_PAGE_FAVORITES, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.favorites, id);
        Intercom.client().logEvent(PRODUCT_PAGE_FAVORITES, eventData);
    }


    public static void logFoodSearch(int count) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.results, count);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(FOOD_SEARCH, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.results, count);
        Intercom.client().logEvent(FOOD_SEARCH, eventData);
    }

    public static void logCreateCustomFood(String from) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.product_from, from);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CUSTOM_PRODUCT_SUCCESS, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.product_from, from);
        Intercom.client().logEvent(CUSTOM_PRODUCT_SUCCESS, eventData);
    }

    public static void logViewArticle(String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.articles_item, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_ARTICLES, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.articles_item, name);
        Intercom.client().logEvent(VIEW_ARTICLES, eventData);
    }
}
