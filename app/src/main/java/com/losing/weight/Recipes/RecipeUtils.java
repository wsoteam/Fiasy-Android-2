package com.losing.weight.Recipes;

import android.content.Context;

import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.Recipes.v2.RecipeActivity;

public class RecipeUtils {

    public static void startDetailActivity(Context context, RecipeItem recipeItem) {

        context.startActivity(RecipeActivity.newIntent(context, recipeItem));
    }
}
