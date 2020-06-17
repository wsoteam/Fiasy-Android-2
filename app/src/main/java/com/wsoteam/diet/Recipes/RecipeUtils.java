package com.wsoteam.diet.Recipes;

import android.content.Context;

import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.v2.RecipeActivity;

public class RecipeUtils {

    public static void startDetailActivity(Context context, RecipeItem recipeItem) {

        context.startActivity(RecipeActivity.newIntent(context, recipeItem));
    }
}
