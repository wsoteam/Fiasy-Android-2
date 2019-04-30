package com.wsoteam.diet.Recipes.POJO;

import java.util.ArrayList;
import java.util.List;

public class EatingGroupsRecipes implements GroupsRecipes {

    List<ListRecipes> listRecipesGroups;

    public EatingGroupsRecipes(ListRecipes listRecipes) {
        listRecipesGroups = new ArrayList<>();
        ListRecipes listBreakfast = new ListRecipes("breakfast");
        List<RecipeItem> breakfast = new ArrayList<>();
        ListRecipes listLunch = new ListRecipes("lunch");
        List<RecipeItem> lunch = new ArrayList<>();
        ListRecipes listDinner = new ListRecipes("dinner");
        List<RecipeItem> dinner = new ArrayList<>();
        ListRecipes listSnack = new ListRecipes("snack");
        List<RecipeItem> snack = new ArrayList<>();

        for (RecipeItem recipeItem:
             listRecipes.getListrecipes()) {

            for (String eating:
                 recipeItem.getEating()) {
                switch (eating){
                    default:
                    //case "breakfast":
                        breakfast.add(recipeItem);
                       // break;
                   // case "lunch":
                        lunch.add(recipeItem);
                       // break;
                  //  case "dinner":
                        dinner.add(recipeItem);
                      //  break;
                   // case "snack":
                        snack.add(recipeItem);
                        break;
                }
            }
        }

        listBreakfast.setListrecipes(breakfast);
        listLunch.setListrecipes(lunch);
        listDinner.setListrecipes(dinner);
        listSnack.setListrecipes(snack);

        listRecipesGroups.add(listBreakfast);
        listRecipesGroups.add(listLunch);
        listRecipesGroups.add(listDinner);
        listRecipesGroups.add(listSnack);
    }

    @Override
    public List<ListRecipes> getGroups() {
        return listRecipesGroups;
    }
}
