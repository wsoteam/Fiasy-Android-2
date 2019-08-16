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
    public static final String CUSTOM_RECIPE_SUCCESS = "custom_reciepe_success";
    public static final String CUSTOM_TEMPLATE_SUCCESS = "custom_template_success";


    //RECIPE
    public static final String VIEW_RECIPE = "view_reciepe";
    public static final String RECIPE_CATEGORY = "reciepe_category";
    public static final String RECIPE_FAVORITES = "reciepe_favorites";
    public static final String RECIPE_ADD_SUCCES = "reciepe_add_success";


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
        io.intercom.android.sdk.Intercom.client().logEvent(VIEW_PRODUCT_PAGE, eventData);
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
        io.intercom.android.sdk.Intercom.client().logEvent(PRODUCT_PAGE_FAVORITES, eventData);
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
        io.intercom.android.sdk.Intercom.client().logEvent(FOOD_SEARCH, eventData);
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
        io.intercom.android.sdk.Intercom.client().logEvent(CUSTOM_PRODUCT_SUCCESS, eventData);
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
        io.intercom.android.sdk.Intercom.client().logEvent(VIEW_ARTICLES, eventData);
    }

    public static void logCreateRecipe(String from) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_from, from);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CUSTOM_RECIPE_SUCCESS, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.recipe_from, from);
        io.intercom.android.sdk.Intercom.client().logEvent(CUSTOM_RECIPE_SUCCESS, eventData);
    }

    public static void logCreateTemplate(String from, String template_intake ) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.template_from, from);
            eventProperties.put(EventProperties.template_intake, template_intake);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CUSTOM_TEMPLATE_SUCCESS, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.template_from, from);
        eventData.put(EventProperties.template_intake, template_intake);
        io.intercom.android.sdk.Intercom.client().logEvent(CUSTOM_TEMPLATE_SUCCESS, eventData);
    }

    public static void logAddFood(String food_intake, String food_category, String food_date, String food_item) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.food_intake, food_intake);
            eventProperties.put(EventProperties.food_category, food_category);
            eventProperties.put(EventProperties.food_date, food_date);
            eventProperties.put(EventProperties.food_item, food_item);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ADD_FOOD_SUCCESS, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.food_intake, food_intake);
        eventData.put(EventProperties.food_category, food_category);
        eventData.put(EventProperties.food_date, food_date);
        eventData.put(EventProperties.food_item, food_item);
        io.intercom.android.sdk.Intercom.client().logEvent(ADD_FOOD_SUCCESS, eventData);
    }

    public static void logViewRecipe(String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_item, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_RECIPE, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.recipe_item, name);
        io.intercom.android.sdk.Intercom.client().logEvent(VIEW_RECIPE, eventData);
    }

    public static void logAddFavoriteRecipe(String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.favorites_recipe, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_FAVORITES, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.favorites_recipe, name);
        io.intercom.android.sdk.Intercom.client().logEvent(RECIPE_FAVORITES, eventData);
    }

    public static void logAddRecipeInDiary(String eating) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_intake, eating);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_ADD_SUCCES, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.recipe_intake, eating);
        io.intercom.android.sdk.Intercom.client().logEvent(RECIPE_ADD_SUCCES, eventData);
    }

    public static void logChoiseRecipeCategory(String categoryName) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_category, categoryName);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_CATEGORY, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.recipe_category, categoryName);
        io.intercom.android.sdk.Intercom.client().logEvent(RECIPE_CATEGORY, eventData);
    }

    public static void logMoveOnboard(int page) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.go_onboard, page);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ONBOARING_NEXT, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.go_onboard, page);
        io.intercom.android.sdk.Intercom.client().logEvent(ONBOARING_NEXT, eventData);
    }

    public static void logSkipOnboard(int page) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.skip_onboard, page);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ONBOARING_SKIP, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.skip_onboard, page);
        io.intercom.android.sdk.Intercom.client().logEvent(ONBOARING_SKIP, eventData);
    }

    public static void logOpenPolitic() {
        Amplitude.getInstance().logEvent(REGISRTATION_PRIVACY);
        io.intercom.android.sdk.Intercom.client().logEvent(REGISRTATION_PRIVACY);
    }

    public static void logRestorePassword() {
        Amplitude.getInstance().logEvent(RESEND_SUCCESS);
        io.intercom.android.sdk.Intercom.client().logEvent(RESEND_SUCCESS);
    }

    public static void logRegistrationError(String error_type) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.error_type, error_type);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(REGISTRATION_ERROR, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.error_type, error_type);
        io.intercom.android.sdk.Intercom.client().logEvent(REGISTRATION_ERROR, eventData);
    }

    public static void logRegistration(String typeRegistration) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.registration, typeRegistration);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(REGISTRATION_SUCCESS, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.registration, typeRegistration);
        io.intercom.android.sdk.Intercom.client().logEvent(REGISTRATION_SUCCESS, eventData);
    }

    public static void logEnter(String typeEnter) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.registration, typeEnter);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ENTER_SUCCESS, eventProperties);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put(EventProperties.registration, typeEnter);
        io.intercom.android.sdk.Intercom.client().logEvent(ENTER_SUCCESS, eventData);
    }
}
