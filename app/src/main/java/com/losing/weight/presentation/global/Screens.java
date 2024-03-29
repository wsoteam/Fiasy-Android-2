package com.losing.weight.presentation.global;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.OtherActivity.ActivityPrivacyPolicy;
import com.losing.weight.POJOProfile.Profile;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.presentation.auth.main.MainAuthActivity;
import com.losing.weight.presentation.auth.restore.ActivityForgotPassword;
import com.losing.weight.presentation.food.template.create.CreateFoodTemplateActivity;
import com.losing.weight.presentation.food.template.create.detail.DetailFoodActivity;
import com.losing.weight.presentation.food.template.create.search.SearchFoodActivity;
import com.losing.weight.presentation.plans.detail.PlanRecipeActivity;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

  public static final class PlanRecipeScreen extends SupportAppScreen {
    private RecipeItem recipeItem;
    private String[] path = new String[3];
    private int visibility = View.GONE;

    public PlanRecipeScreen(RecipeItem recipeItem, String day, String meal, String number) {
      this.recipeItem = recipeItem;
      path[0] = day;
      path[1] = meal;
      path[2] = number;
    }

    public PlanRecipeScreen(RecipeItem recipeItem, int visibility, String day, String meal,
        String number) {
      this.recipeItem = recipeItem;
      this.visibility = visibility;
      path[0] = day;
      path[1] = meal;
      path[2] = number;
    }

    @Override
    public Intent getActivityIntent(Context context) {
//      return new Intent(context, PlanRecipeActivityOld.class)
//          .putExtra(Config.RECIPE_INTENT, recipeItem)
//          .putExtra("RecipePath", path)
//          .putExtra("VisibilityButton", visibility);
      return PlanRecipeActivity.Companion.newIntent(context, recipeItem, path, visibility);
    }
  }

  public static final class EditFoodActivity extends SupportAppScreen {
    int position;

    public EditFoodActivity(int position) {
      this.position = position;
    }

    @Override
    public Intent getActivityIntent(Context context) {
      Intent intent = new Intent(context, DetailFoodActivity.class);
      intent.putExtra(Config.INTENT_DETAIL_FOOD, position);
      intent.putExtra(Config.DETAIL_FOOD_BTN_NAME, "Изменить");
      return intent;
    }
  }

  public static final class CreateSearchFoodActivity extends SupportAppScreen {
    @Override
    public Intent getActivityIntent(Context context) {
      return new Intent(context, SearchFoodActivity.class);
    }
  }

  public static final class CreateFodTemplateScreen extends SupportAppScreen {
    @Override
    public Intent getActivityIntent(Context context) {
      return new Intent(context, CreateFoodTemplateActivity.class);
    }
  }

  public static final class EditProfileScreen extends SupportAppScreen {
    private final boolean isReg;

    public EditProfileScreen(boolean isReg) {
      this.isReg = isReg;
    }
  }

  public static final class SignInScreen extends SupportAppScreen {
    @Override
    public Intent getActivityIntent(Context context) {
      return new Intent(context, MainAuthActivity.class);
    }
  }

  public static final class AuthScreen extends SupportAppScreen {
    private Box box = null;
    private Profile profile;

    public AuthScreen(Profile profile) {
      this.profile = profile;
    }

    public AuthScreen(Profile profile, Box box) {
      this.profile = profile;
      this.box = box;
    }

    @Override
    public Intent getActivityIntent(Context context) {
      Intent intent = new Intent(context, MainAuthActivity.class)
          .putExtra(Config.CREATE_PROFILE, true)
          .putExtra(Config.INTENT_PROFILE, profile);
      if (box != null) {
        intent.putExtra(Config.TAG_BOX, box);
      }
      return intent;
    }
  }

  public static final class ForgotPassScreen extends SupportAppScreen {
    @Override
    public Intent getActivityIntent(Context context) {
      return new Intent(context, ActivityForgotPassword.class);
    }
  }

  public static final class PrivacyPolicyScreen extends SupportAppScreen {
    @Override
    public Intent getActivityIntent(Context context) {
      return new Intent(context, ActivityPrivacyPolicy.class);
    }
  }

  public static final class SubscriptionScreen extends SupportAppScreen {
    @Override
    public Intent getActivityIntent(Context context) {
      Box box = new Box();
      box.setSubscribe(false);
      box.setOpenFromPremPart(true);
      box.setOpenFromIntrodaction(false);
      box.setComeFrom(AmplitudaEvents.view_prem_plans);
      box.setBuyFrom(EventProperties.trial_from_plans);
      Intent intent = new Intent(context, ActivitySubscription.class)
          .putExtra(Config.TAG_BOX, box);
      return intent;
    }
  }
}