package com.wsoteam.diet.Recipes.POJO.plan;

import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class PlansGroupsRecipe implements GroupsRecipes {
    private List<ListRecipes> listRecipesGroups;

    public List<RecipeForDay> getRecipeForDays() {
        return recipeForDays;
    }

    private List<RecipeForDay> recipeForDays;

    public PlansGroupsRecipe(ListRecipes listRecipes, String plan) {
        int days = 28;
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
        recipeForDays = new ArrayList<>();
        for (int i = 0; i < 28; i++){
            recipeForDays.add(createForDay());
        }
    }

    RecipeForDay createForDay(){
        List<RecipeItem> breakfast = new ArrayList<>();
        breakfast.add(listRecipesGroups.get(0).getListrecipes().get(0));
        breakfast.add(listRecipesGroups.get(0).getListrecipes().get(2));
        breakfast.add(listRecipesGroups.get(0).getListrecipes().get(3));
        List<RecipeItem> lunch = new ArrayList<>();
        lunch.add(listRecipesGroups.get(1).getListrecipes().get(0));
        lunch.add(listRecipesGroups.get(1).getListrecipes().get(1));
        lunch.add(listRecipesGroups.get(1).getListrecipes().get(2));
        List<RecipeItem> dinner = new ArrayList<>();
        dinner.add(listRecipesGroups.get(2).getListrecipes().get(0));
        dinner.add(listRecipesGroups.get(2).getListrecipes().get(1));
        dinner.add(listRecipesGroups.get(2).getListrecipes().get(2));
        List<RecipeItem> snack = new ArrayList<>();
        snack.add(listRecipesGroups.get(2).getListrecipes().get(0));
        snack.add(listRecipesGroups.get(2).getListrecipes().get(1));
        snack.add(listRecipesGroups.get(2).getListrecipes().get(2));

        RecipeForDay forDay = new RecipeForDay();
        forDay.setBreakfast(breakfast);
        forDay.setLunch(lunch);
        forDay.setDinner(dinner);
        forDay.setSnack(snack);

        return forDay;
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
