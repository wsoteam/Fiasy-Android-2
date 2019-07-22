package com.wsoteam.diet.Recipes.POJO;

import java.util.ArrayList;
import java.util.List;

public class PlansGroupsRecipe implements GroupsRecipes{
    private List<ListRecipes> listRecipesGroups;

    public PlansGroupsRecipe(ListRecipes listRecipes, String plan) {
        listRecipesGroups = new ArrayList<>();

        ListRecipes listBreakfast = new ListRecipes("Завтрак");
        List<RecipeItem> breakfast = new ArrayList<>();

        ListRecipes listLunch = new ListRecipes("Обед");
        List<RecipeItem> lunch = new ArrayList<>();

        ListRecipes listDinner = new ListRecipes("Ужин");
        List<RecipeItem> dinner = new ArrayList<>();

        ListRecipes listSnack = new ListRecipes("Перекус");
        List<RecipeItem> snack = new ArrayList<>();

        listBreakfast.setListrecipes(breakfast);
        listLunch.setListrecipes(lunch);
        listDinner.setListrecipes(dinner);
        listSnack.setListrecipes(snack);

        listRecipesGroups.add(listBreakfast);
        listRecipesGroups.add(listLunch);
        listRecipesGroups.add(listDinner);
        listRecipesGroups.add(listSnack);

        for (RecipeItem recipe:
             listRecipes.getListrecipes()) {
            if (recipe.getBreakfast().contains(plan)){
                breakfast.add(recipe);
            }

            if (recipe.getLunch().contains(plan)){
                lunch.add(recipe);
            }

            if (recipe.getDinner().contains(plan)){
                dinner.add(recipe);
            }

            if (recipe.getSnack().contains(plan)){
                snack.add(recipe);
            }
        }
    }

    @Override
    public List<ListRecipes> getGroups() {
        return listRecipesGroups;
    }

    @Override
    public int size() {

        int size = 0;

        for (ListRecipes groups:
             listRecipesGroups) {
            size = size + groups.getListrecipes().size();
        }

        return size;
    }
}
