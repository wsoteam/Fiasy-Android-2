package com.wsoteam.diet.Recipes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.v2.BlockedRecipeActivity;
import com.wsoteam.diet.Recipes.v2.RecipeActivity;
import com.wsoteam.diet.utils.Subscription;

public class RecipeUtils {

    public static void startDetailActivity(Context context, RecipeItem recipeItem) {

        Log.d("xxx", recipeItem.getName() + " --- " + recipeItem.isPremium());
        Intent intent;

        if (!Subscription.check(context) && recipeItem.isPremium()) {
            intent = new Intent(context, BlockedRecipeActivity.class);
        } else {
            intent = new Intent(context, RecipeActivity.class);
        }
        intent.putExtra(Config.RECIPE_INTENT, recipeItem);

        context.startActivity(intent);
    }
}
