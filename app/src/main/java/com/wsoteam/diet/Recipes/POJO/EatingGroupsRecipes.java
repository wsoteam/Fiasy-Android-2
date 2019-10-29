package com.wsoteam.diet.Recipes.POJO;

import android.content.Context;
import android.util.Log;

import com.wsoteam.diet.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EatingGroupsRecipes implements GroupsRecipes {

    private List<ListRecipes> listRecipesGroups;
    private int prev = -1;
    private boolean adding;

    public EatingGroupsRecipes(ListRecipes listRecipes, Context context) {
        listRecipesGroups = new ArrayList<>();
        ListRecipes listBreakfast = new ListRecipes(context.getString(R.string.breakfast));
        List<RecipeItem> breakfast = new ArrayList<>();
        ListRecipes listLunch = new ListRecipes(context.getString(R.string.lunch));
        List<RecipeItem> lunch = new ArrayList<>();
        ListRecipes listDinner = new ListRecipes(context.getString(R.string.dinner));
        List<RecipeItem> dinner = new ArrayList<>();
        ListRecipes listSnack = new ListRecipes(context.getString(R.string.snack));
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



        Collections.shuffle(breakfast);
        Collections.shuffle(lunch);
        Collections.shuffle(dinner);
        Collections.shuffle(snack);

        listBreakfast.setListrecipes(breakfast);
        listLunch.setListrecipes(lunch);
        listDinner.setListrecipes(dinner);
        listSnack.setListrecipes(snack);

        if (breakfast.size() > 0) listRecipesGroups.add(listBreakfast);
        if (lunch.size() > 0) listRecipesGroups.add(listLunch);
        if (dinner.size() > 0) listRecipesGroups.add(listDinner);
        if (snack.size() > 0) listRecipesGroups.add(listSnack);
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

    @Override
    public int size() {
        return 0;
    }
}
