package com.wsoteam.diet;

public class Config {

    public static final boolean RELEASE = false;
//    public static final boolean RELEASE = true;

    public static final String ID_OF_RECIPE_GROUPS = "id_of_recipe_groups";
    public static final String YANDEX_API_KEY = "8e2511af-4c77-48f5-bb85-5079916b7416";
    public static final String NAME_OF_ENTITY_FOR_DB = "GLOBAL_DATA_BASE";
    public static final String NAME_OF_USER_DATA_LIST_ENTITY = "USER_LIST";

    public static final String IS_NEED_SHOW_GRADE_DIALOG = "IS_NEED_SHOW_GRADE_DIALOG";

    public static String EXERCISES_NAME_OF_ENTITY_FOR_DB = "WORKOUT";

    //Recipes
    public static final String RECIPES_BUNDLE = "RECIPES_BUNDLE";
    public static final String RECIPE_INTENT = "RECIPE_INTENT";
    public static final String RECIPE_BACK_STACK = "RECIPE_BACK_STACK";
    public static final int RECIPE_EMPTY_WEIGHT = -1;
    public static final String RECIPE_FOOD_INTENT = "RECIPE_FOOD_INTENT";
    public static final int RECIPE_FOOD_FOR_RESULT_CODE = 4987;
   public static final String USERS_RECIPES = "USERS_RECIPES";


    public static final String ARTICLE_INTENT = "ARTICLE_INTENT";

    //Diary rewrite
    public static final String TAG_OF_REWRITE = "TAG_OF_REWRITE";
    public static final int NOT_ENTER_EARLY = -1;
    public static final int REWRITE_PROFILE = 1;
    public static final int NOT_REWRITE_PROFILE = 0;

    //Tags for choise where need save food
    public static final String TAG_CHOISE_EATING = "TAG_CHOISE_EATING";

    //MainActivity
    public static final int COUNT_PAGE = 3650;
    public static final String INTENT_DATE_FOR_SAVE = "INTENT_DATE_FOR_SAVE";

    //Intents tags for send first profile
    public static final String INTENT_PROFILE = "INTENT_PROFILE";
    public static final String CREATE_PROFILE = "CREATE_PROFILE";

    public static final String ONBOARD_PROFILE = "ONBOARD_PROFILE";
    public static final String ONBOARD_PROFILE_SEX = "ONBOARD_PROFILE_SEX";
    public static final String ONBOARD_PROFILE_HEIGHT = "ONBOARD_PROFILE_HEIGHT";
    public static final String ONBOARD_PROFILE_WEIGHT = "ONBOARD_PROFILE_WEIGHT";
    public static final String ONBOARD_PROFILE_YEARS = "ONBOARD_PROFILE_YEARS";
    public static final String ONBOARD_PROFILE_ACTIVITY = "ONBOARD_PROFILE_ACTIVITY";
    public static final String ONBOARD_PROFILE_PURPOSE = "ONBOARD_PROFILE_PURPOSE";

    //Key of billing state
    public static final String STATE_BILLING = "STATE_BILLING";
    public static final String ALERT_BUY_SUBSCRIPTION = "ALERT_BUY_SUBSCRIPTION";


    public static final String AMPLITUDE_BUY_FROM = "AMPLITUDE_BUY_FROM";
    public static final String AMPLITUDE_COME_FROM = "AMPLITUDE_COME_FROM";

    public static final String IS_NEED_REG = "IS_NEED_REG";

    public static final String OPEN_PREM_FROM_INTRODACTION = "SHOWED_INTRODACTION";
    public static final String NAME_USER_FOR_INTRODACTION = "NAME_USER_FOR_INTRODACTION";

    public static final String IS_NEED_SHOW_ONBOARD = "IS_NEED_SHOW_ONBOARD";

    public static final String WATER_SETTINGS = "WATER_SETTINGS";
    public static final String MAX_WATER_COUNT_STEP = "MAX_WATER_COUNT_STEP";
    public static final String WATER_PACK = "WATER_PACK";
    public static final String WATER_REMINDER = "WATER_REMINDER";

    //Names of subscriptions
    public static final String ONE_YEAR_PRICE_TRIAL = "basic_subscription_12m_trial";
    public static final String ONE_MONTH_PRICE = "basic_subscription_1m";


    // -1 - not view grade dialog (default), 0 - not grade, 1 - grade
    public static final String IS_GRADE_APP= "IS_GRADE_APP";
    public static final int NOT_VIEW_GRADE_DIALOG= -1;
    public static final int NOT_GRADED= 0;
    public static final int GRADED= 1;

    public static final String STARTING_POINT = "STARTING_POINT";
    public static final String IS_ADDED_FOOD = "IS_ADDED_FOOD";

    public static final long ONE_DAY= 86400000l;

    public static final String GEO= "GEO";
    public static final String UA_GEO= "UA";
    public static final String DEF_GEO= "DEF_GEO";


    public static final String TAG_BOX= "TAG_BOX";


//    firebase realtime database
    public static final String PECIPES_PLANS = "RECIPES_PLANS";


    public static final String INTENT_DETAIL_FOOD = "INTENT_DETAIL_FOOD";


}
