package com.wsoteam.diet.Recipes.POJO;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EatingGroupsRecipes implements GroupsRecipes {

    List<ListRecipes> listRecipesGroups;
    private int prev = -1;
    private boolean adding;

    public EatingGroupsRecipes(ListRecipes listRecipes) {
        listRecipesGroups = new ArrayList<>();
        ListRecipes listBreakfast = new ListRecipes("Завтрак");
        List<RecipeItem> breakfast = new ArrayList<>();
        ListRecipes listLunch = new ListRecipes("Обед");
        List<RecipeItem> lunch = new ArrayList<>();
        ListRecipes listDinner = new ListRecipes("Ужин");
        List<RecipeItem> dinner = new ArrayList<>();
        ListRecipes listSnack = new ListRecipes("Перекус");
        List<RecipeItem> snack = new ArrayList<>();


        // 1 - lunch
        // 2 - breakfast
        // 3 - dinner
        // 4 - snack

        for (RecipeItem recipe:
             listRecipes.getListrecipes()) {

            adding = false;

            select(recipe, breakfast, lunch, dinner, snack);

            if (!adding){
                prev = -1;
                select(recipe, breakfast, lunch, dinner, snack);
            }

        }


        Log.d("recipeG", "breakfast: " + breakfast.size());
        Log.d("recipeG", "lunch: " + lunch.size());
        Log.d("recipeG", "dinner: " + dinner.size());
        Log.d("recipeG", "snack: " + snack.size());

        listBreakfast.setListrecipes(breakfast);
        listLunch.setListrecipes(lunch);
        listDinner.setListrecipes(dinner);
        listSnack.setListrecipes(snack);

        listRecipesGroups.add(listBreakfast);
        listRecipesGroups.add(listLunch);
        listRecipesGroups.add(listDinner);
        listRecipesGroups.add(listSnack);
    }

    private void select(RecipeItem recipe,
                        List<RecipeItem> breakfast,
                        List<RecipeItem> lunch,
                        List<RecipeItem> dinner,
                        List<RecipeItem> snack ){

        if (recipe.getUrl().equals("link")) { return;}

        if (recipe.getLunch() != null && !recipe.getLunch().get(0).equals("") && prev != 1){
            lunch.add(recipe);
            prev = 1;
            adding = true;
        }

        if (recipe.getBreakfast() != null && !recipe.getBreakfast().get(0).equals("") && prev != 2){
            breakfast.add(recipe);
            prev = 2;
            adding = true;
        }

        if (recipe.getDinner() != null && !recipe.getDinner().get(0).equals("") && prev != 3){
            dinner.add(recipe);
            prev = 3;
            adding = true;
        }

        if (recipe.getSnack() != null && !recipe.getSnack().get(0).equals("") && prev != 4){
            snack.add(recipe);
            prev = 4;
            adding = true;
        }

    }

    @Override
    public List<ListRecipes> getGroups() {
        return listRecipesGroups;
    }
}
