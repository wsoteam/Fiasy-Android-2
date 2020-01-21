package com.wsoteam.diet.common.Analytics;

import android.os.Bundle;
import android.util.Log;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Revenue;
import com.android.billingclient.api.BillingClient;
import com.wsoteam.diet.App;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String ONBOARDING_SUCCESS = "onboarding_success";
    public static final String REGISTRATION_NEXT = "registration_next";


    //PURCHASE
    public static final String TRIAL_SUCCES = "trial_success";

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
    public static final String VIEW_SETTINGS = "view_settings";


    //INTERCOM
    public static final String INTERCOM_CHAT = "intercom_chat";


    //ARTICLES
    public static final String CHOOSE_ARTICLES = "choose_articles";
    public static final String VIEW_ARTICLES = "view_articles";
    public static final String SELECT_DIET = "select_diet";
    public static final String SELECT_TRAINING = "select_training";

    //Purchase
    public static final String TRIAL_SUCCESS = "trial_success";
    public static final String PURCHASE_SUCCESS = "purchase_success";
    public static final String PREMIUM_NEXT = "premium_next";

    public static final String TRIAL_ERROR = "trial_error";

    //Purchase
    public static final String DIARY_NEXT = "diary_next";
    public static final int BREAKFAST = 0;
    public static final int LUNCH = 1;
    public static final int DINNER = 2;
    public static final int SNACK = 3;

    //Search
    public static final String SEARCH_SUCCESS = "search_success";

    public static final String ADD_TEMPLATE_SUCCESS = "add_template_success";
    public static final String ADD_CUSTOM_SUCCESS = "add_custom_success";
    public static final String ADD_CUSTOM_RECIPE = "add_custom_recipe";
    public static final String CHANGE_GOAL = "change_goal";

    //Diets
    public static final String CHOOSE_PLAN = "choose_plan";
    public static final String CONNECT_PLAN_SUCCES = "connect_plan_success";
    public static final String WATCH_PLAN_RECIPIES = "watch_plan_recipies";
    public static final String ADD_PLAN_RECIPE_SUCCESS = "add_plan_recipe_success";
    public static final String PLAN_COMPLETE = "plan_complete";
    public static final String LEAVE_PLAN = "leave_plan";

    //Tutorial
    private static final String TEACH_NEXT = "tutorial_next";

    //окно оценки приложения
    private static final String REVIEW_SUCCESS = "review_success";

    public static void  logReview(double stars, String comment){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.review_star, stars);
            eventProperties.put(EventProperties.review_comment, comment);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        Amplitude.getInstance().logEvent(REVIEW_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putDouble(EventProperties.review_star, stars);
        bundle.putString(EventProperties.review_comment, comment);
        App.getFAInstance().logEvent(REVIEW_SUCCESS, bundle);
    }


    public static void logTeach(String screen){

        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.teach_screen, screen);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        Amplitude.getInstance().logEvent(TEACH_NEXT, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.teach_screen, screen);
        App.getFAInstance().logEvent(TEACH_NEXT, bundle);
    }

    public static void logAddRecipeInDiaryPlan(String eating, String nameRecipe, String namePlan) {
        String convertedNamePlan = flagToAnalConst(namePlan);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_intake, eating);
            eventProperties.put(EventProperties.recipe_id_add, nameRecipe);
            eventProperties.put(EventProperties.diet_plans_choose, convertedNamePlan);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_ADD_SUCCES, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_intake, eating);
        bundle.putString(EventProperties.recipe_id_add, nameRecipe);
        bundle.putString(EventProperties.diet_plans_choose, convertedNamePlan);
        App.getFAInstance().logEvent(RECIPE_ADD_SUCCES, bundle);
    }


    public static void logViewRecipePlan(String name, String namePlan) {
        String convertedNamePlan = flagToAnalConst(namePlan);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_item, name);
            eventProperties.put(EventProperties.diet_plans_choose, convertedNamePlan);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_RECIPE, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_item, name);
        bundle.putString(EventProperties.diet_plans_choose, convertedNamePlan);
        App.getFAInstance().logEvent(VIEW_RECIPE, bundle);
    }


    public static void logPlanLeave(String namePlan, int activeDays) {
        String convertedNamePlan = flagToAnalConst(namePlan);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.diet_plans_choose, convertedNamePlan);
            eventProperties.put(EventProperties.active_day, activeDays);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(LEAVE_PLAN, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.diet_plans_choose, convertedNamePlan);
        bundle.putInt(EventProperties.active_day, activeDays);
        App.getFAInstance().logEvent(LEAVE_PLAN, bundle);
    }

    public static void logSetUserPropertyError(String message) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put("message", message);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent("prop_error", eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        App.getFAInstance().logEvent("prop_error", bundle);
    }

    public static void logChangeGoal() {
        Amplitude.getInstance().logEvent(CHANGE_GOAL);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(CHANGE_GOAL, bundle);
    }


    public static void logPlanComplete(String namePlan, int countRecipes) {
        String convertedNamePlan = flagToAnalConst(namePlan);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.diet_plans_choose, convertedNamePlan);
            eventProperties.put(EventProperties.recipe_added, countRecipes);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(PLAN_COMPLETE, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.diet_plans_choose, convertedNamePlan);
        bundle.putInt(EventProperties.recipe_added, countRecipes);
        App.getFAInstance().logEvent(PLAN_COMPLETE, bundle);
    }

    public static void logAddPlanRecipe(String intake, String recipe, String from) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.plan_intake, intake);
            eventProperties.put(EventProperties.recipe_id_plan, recipe);
            eventProperties.put(EventProperties.plan_intake_from, from);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ADD_PLAN_RECIPE_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.plan_intake, intake);
        bundle.putString(EventProperties.recipe_id_plan, recipe);
        bundle.putString(EventProperties.plan_intake_from, from);
        App.getFAInstance().logEvent(ADD_PLAN_RECIPE_SUCCESS, bundle);
    }

    public static void logConnectPlan(String namePlan, String watchPlan, int activeDays) {
        String convertedNamePlan = flagToAnalConst(namePlan);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.diet_plans_choose, convertedNamePlan);
            eventProperties.put(EventProperties.watch_plan, watchPlan);
            eventProperties.put(EventProperties.active_day, activeDays);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CONNECT_PLAN_SUCCES, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.diet_plans_choose, convertedNamePlan);
        bundle.putString(EventProperties.watch_plan, watchPlan);
        bundle.putInt(EventProperties.active_day, activeDays);
        App.getFAInstance().logEvent(CONNECT_PLAN_SUCCES, bundle);
    }


    public static String flagToAnalConst(String namePlan) {
        String convertedNamePlan = "";
        switch (namePlan) {
            case "keto":
                convertedNamePlan = EventProperties.diet_plans_keto;
                break;
            case "vegan":
                convertedNamePlan = EventProperties.diet_plans_vegan;
                break;
            case "3week":
                convertedNamePlan = EventProperties.diet_plans_21_day;
                break;
            case "prot":
                convertedNamePlan = EventProperties.diet_plans_high_protein;
                break;
            case "classic":
                convertedNamePlan = EventProperties.diet_plans_classic;
                break;
            case "clear":
                convertedNamePlan = EventProperties.diet_plans_clear;
                break;
            case "mediterranean":
                convertedNamePlan = EventProperties.diet_plans_mediterranean;
                break;
            case "scand":
                convertedNamePlan = EventProperties.diet_plans_scandinavian;
                break;
            case "highLCHF":
                convertedNamePlan = EventProperties.diet_plans_LCHF_strict;
                break;
            case "mediumLCHF":
                convertedNamePlan = EventProperties.diet_plans_LCHF_moderate;
                break;
            case "52":
                convertedNamePlan = EventProperties.diet_plans_5_2;
                break;
            case "lowLCHF":
                convertedNamePlan = EventProperties.diet_plans_LCHF_light;
                break;
            case "61":
                convertedNamePlan = EventProperties.diet_plans_6_1;
                break;
        }
        return convertedNamePlan;
    }


    public static void logViewPlan(String namePlan) {
        String convertedNamePlan = flagToAnalConst(namePlan);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.diet_plans_choose, convertedNamePlan);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CHOOSE_PLAN, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.diet_plans_choose, convertedNamePlan);
        App.getFAInstance().logEvent(CHOOSE_PLAN, bundle);
    }

    public static void logViewArticlesDiet() {
        Amplitude.getInstance().logEvent(SELECT_DIET);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(SELECT_DIET, bundle);
    }

    public static void logViewArticlesTraining() {
        Amplitude.getInstance().logEvent(SELECT_TRAINING);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(SELECT_TRAINING, bundle);
    }

    public static void logAddCustomRecipe(String nameRecipe) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_id_add, nameRecipe);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ADD_CUSTOM_RECIPE, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_id_add, nameRecipe);
        App.getFAInstance().logEvent(ADD_CUSTOM_RECIPE, bundle);
    }

    public static void logSearch(String nameFood) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.search_item, nameFood);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(SEARCH_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.search_item, nameFood);
        App.getFAInstance().logEvent(SEARCH_SUCCESS, bundle);
    }

    public static void logDiaryNext(int number) {
        String eating = "";
        switch (number) {
            case BREAKFAST:
                eating = EventProperties.add_intake_breakfast;
                break;
            case LUNCH:
                eating = EventProperties.add_intake_lunch;
                break;
            case DINNER:
                eating = EventProperties.add_intake_dinner;
                break;
            case SNACK:
                eating = EventProperties.add_intake_snack;
                break;
        }
        Log.e("LOL", eating);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.add_intake, eating);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(DIARY_NEXT, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.add_intake, eating);
        App.getFAInstance().logEvent(DIARY_NEXT, bundle);
    }

    public static void logTrackRevenue(double price) {
        Revenue revenue = new Revenue().setProductId("com.wild.diet").setPrice(price).setQuantity(1);
        Amplitude.getInstance().logRevenueV2(revenue);
    }

    public static void logPurchaseSuccess() {
        Amplitude.getInstance().logEvent(PURCHASE_SUCCESS);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(PURCHASE_SUCCESS, bundle);
    }

    public static void logBillingError(int responseCode) {
        String eventDecryption = "unknown";
        switch (responseCode) {
            case BillingClient.BillingResponse.USER_CANCELED:
                eventDecryption = EventProperties.trial_error_back_or_canceled;
                break;
            case BillingClient.BillingResponse.SERVICE_UNAVAILABLE:
                eventDecryption = EventProperties.trial_error_service_unvailable;
                break;
            case BillingClient.BillingResponse.BILLING_UNAVAILABLE:
                eventDecryption = EventProperties.trial_error_billing_unvailable;
                break;
            case BillingClient.BillingResponse.ITEM_UNAVAILABLE:
                eventDecryption = EventProperties.trial_error_item_unvailable;
                break;
            case BillingClient.BillingResponse.DEVELOPER_ERROR:
                eventDecryption = EventProperties.trial_error_dev_error;
                break;
            case BillingClient.BillingResponse.ERROR:
                eventDecryption = EventProperties.trial_error_error;
                break;
            case BillingClient.BillingResponse.ITEM_ALREADY_OWNED:
                eventDecryption = EventProperties.trial_error_already_owned;
                break;
            case BillingClient.BillingResponse.ITEM_NOT_OWNED:
                eventDecryption = EventProperties.trial_error_not_owned;
                break;
        }

        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.trial_error, eventDecryption);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(TRIAL_ERROR, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.trial_error, eventDecryption);
        App.getFAInstance().logEvent(TRIAL_ERROR, bundle);
    }

    public static void logViewSettings() {
        Amplitude.getInstance().logEvent(VIEW_SETTINGS);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(VIEW_SETTINGS, bundle);
    }

    public static void logDeleteFood() {
        Amplitude.getInstance().logEvent(DELETE_FOOD);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(DELETE_FOOD, bundle);
    }

    public static void logEditFood() {
        Amplitude.getInstance().logEvent(EDIT_FOOD);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(EDIT_FOOD, bundle);
    }

    public static void logPushButtonReg(String whichButton) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.enter_push_button, whichButton);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(REGISTRATION_NEXT, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.enter_push_button, whichButton);
        App.getFAInstance().logEvent(REGISTRATION_NEXT, bundle);
    }


    public static void logSuccessOnboarding(String how) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.onboarding_success_from, how);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ONBOARDING_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.onboarding_success_from, how);
        App.getFAInstance().logEvent(ONBOARDING_SUCCESS, bundle);
    }

    public static void logPushButton(String whichButton, String from) {
        logPushButton(whichButton, from, null);
    }

    public static void logPushButton(String whichButton, String from, String purchaseId) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.push_button, whichButton);
            eventProperties.put(EventProperties.push_button_from, from);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(PREMIUM_NEXT, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.push_button, whichButton);
        bundle.putString(EventProperties.push_button_from, from);
        App.getFAInstance().logEvent(PREMIUM_NEXT, bundle);
    }


    public static void logBuy(String from, String autoRenewing) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.trial_from, from);
            eventProperties.put(EventProperties.auto_renewal, autoRenewing);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(TRIAL_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.trial_from, from);
        bundle.putString(EventProperties.auto_renewal, autoRenewing);
        App.getFAInstance().logEvent(TRIAL_SUCCESS, bundle);
    }

    public static void logNewBuy(String from, String autoRenewing, String id) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.trial_from, from);
            eventProperties.put(EventProperties.auto_renewal, autoRenewing);
            eventProperties.put(EventProperties.purchase_id, id);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(TRIAL_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.trial_from, from);
        bundle.putString(EventProperties.auto_renewal, autoRenewing);
        bundle.putString(EventProperties.purchase_id, id);
        App.getFAInstance().logEvent(TRIAL_SUCCESS, bundle);
    }

    public static void logSetBuyError(String message) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put("error", message);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent("settings_error", eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString("error", message);
        App.getFAInstance().logEvent("settings_error", bundle);
    }


    public static void logViewFood(String id) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.product_item, id);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_PRODUCT_PAGE, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.product_item, id);
        App.getFAInstance().logEvent(VIEW_PRODUCT_PAGE, bundle);
    }


    public static void logAddFavorite(String id) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.favorites, id);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(PRODUCT_PAGE_FAVORITES, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.favorites, id);
        App.getFAInstance().logEvent(PRODUCT_PAGE_FAVORITES, bundle);
    }


    public static void logFoodSearch(int count) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.results, count);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(FOOD_SEARCH, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putInt(EventProperties.results, count);
        App.getFAInstance().logEvent(FOOD_SEARCH, bundle);
    }

    public static void logCreateCustomFood(String from, String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.product_from, from);
            eventProperties.put(EventProperties.product_id, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CUSTOM_PRODUCT_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.product_from, from);
        bundle.putString(EventProperties.product_id, name);
        App.getFAInstance().logEvent(CUSTOM_PRODUCT_SUCCESS, bundle);
    }

    public static void logViewArticle(String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.articles_item, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_ARTICLES, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.articles_item, name);
        App.getFAInstance().logEvent(VIEW_ARTICLES, bundle);
    }

    public static void logCreateRecipe(String from, String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_from, from);
            eventProperties.put(EventProperties.recipe_id, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CUSTOM_RECIPE_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_from, from);
        bundle.putString(EventProperties.recipe_id, name);
        App.getFAInstance().logEvent(CUSTOM_RECIPE_SUCCESS, bundle);
    }

    public static void logCreateTemplate(String from, String template_intake, List<Food> foodList) {
        String namesLine = "";
        for (int i = 0; i < foodList.size(); i++) {
            namesLine = namesLine + "  " + foodList.get(i).getName();
        }
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.template_from, from);
            eventProperties.put(EventProperties.template_intake, template_intake);
            eventProperties.put(EventProperties.product_inside, namesLine);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(CUSTOM_TEMPLATE_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.template_from, from);
        bundle.putString(EventProperties.template_intake, template_intake);
        bundle.putString(EventProperties.product_inside, namesLine);
        App.getFAInstance().logEvent(CUSTOM_TEMPLATE_SUCCESS, bundle);
    }

    public static void logAddFood(String food_intake, String food_category, String food_date, String food_item, int kcals, int weight) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.food_intake, food_intake);
            eventProperties.put(EventProperties.food_category, food_category);
            eventProperties.put(EventProperties.food_date, food_date);
            eventProperties.put(EventProperties.food_item, food_item);
            eventProperties.put(EventProperties.calorie_value, kcals);
            eventProperties.put(EventProperties.product_weight, weight);
        } catch (JSONException exception) {
        }

        Amplitude.getInstance().logEvent(ADD_FOOD_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.food_intake, food_intake);
        bundle.putString(EventProperties.food_category, food_category);
        bundle.putString(EventProperties.food_date, food_date);
        bundle.putString(EventProperties.food_item, food_item);
        bundle.putInt(EventProperties.calorie_value, kcals);
        bundle.putInt(EventProperties.product_weight, weight);
        App.getFAInstance().logEvent(ADD_FOOD_SUCCESS, bundle);
    }

    public static void logViewRecipe(String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_item, name);
            eventProperties.put(EventProperties.diet_plans_choose, EventProperties.diet_plans_without);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(VIEW_RECIPE, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_item, name);
        bundle.putString(EventProperties.diet_plans_choose, EventProperties.diet_plans_without);
        App.getFAInstance().logEvent(VIEW_RECIPE, bundle);
    }

    public static void logAddFavoriteRecipe(String name) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.favorites_recipe, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_FAVORITES, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.favorites_recipe, name);
        App.getFAInstance().logEvent(RECIPE_FAVORITES, bundle);
    }



    public static void logAddRecipeInDiary(String eating, String nameRecipe) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_intake, eating);
            eventProperties.put(EventProperties.recipe_id_add, nameRecipe);
            eventProperties.put(EventProperties.diet_plans_choose, EventProperties.diet_plans_without);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_ADD_SUCCES, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_intake, eating);
        bundle.putString(EventProperties.recipe_id_add, nameRecipe);
        bundle.putString(EventProperties.diet_plans_choose, EventProperties.diet_plans_without);
        App.getFAInstance().logEvent(RECIPE_ADD_SUCCES, bundle);
    }

    public static void logChoiseRecipeCategory(String categoryName) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.recipe_category, categoryName);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(RECIPE_CATEGORY, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.recipe_category, categoryName);
        App.getFAInstance().logEvent(RECIPE_CATEGORY, bundle);
    }

    public static void logMoveOnboard(int page) {
        String eventName = "";
        if (page != EventProperties.go_onboard_reg) {
            eventName = EventProperties.go_onboard_prename + String.valueOf(page);
        } else {
            eventName = EventProperties.go_onboard_reg_name;
        }
        Log.e("LOL", eventName);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.go_onboard, eventName);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ONBOARING_NEXT, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.go_onboard, eventName);
        App.getFAInstance().logEvent(ONBOARING_NEXT, bundle);
    }

    public static void logSkipOnboard(int page) {
        String name = "";
        name = EventProperties.skip_onboard_prename + String.valueOf(page);
        Log.e("LOL", name);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.skip_onboard, name);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ONBOARING_SKIP, eventProperties);


        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.skip_onboard, name);
        App.getFAInstance().logEvent(ONBOARING_SKIP, bundle);
    }

    public static void logOpenPolitic() {
        Amplitude.getInstance().logEvent(REGISRTATION_PRIVACY);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(REGISRTATION_PRIVACY, bundle);
    }


    public static void logRegistration(String typeRegistration) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.registration, typeRegistration);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(REGISTRATION_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.registration, typeRegistration);
        App.getFAInstance().logEvent(REGISTRATION_SUCCESS, bundle);
    }

    public static void logEnter(String typeEnter) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.registration, typeEnter);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(ENTER_SUCCESS, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.registration, typeEnter);
        App.getFAInstance().logEvent(ENTER_SUCCESS, bundle);
    }

    public static void logMoveQuestions(String page) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(EventProperties.question, page);
        } catch (JSONException exception) {
        }
        Amplitude.getInstance().logEvent(QUESTION_NEXT, eventProperties);

        Bundle bundle = new Bundle();
        bundle.putString(EventProperties.question, page);
        App.getFAInstance().logEvent(QUESTION_NEXT, bundle);
    }

    public static void logLogout() {
        Amplitude.getInstance().logEvent(PROFILE_LOGOUT);

        Bundle bundle = new Bundle();
        App.getFAInstance().logEvent(PROFILE_LOGOUT, bundle);
    }
}
