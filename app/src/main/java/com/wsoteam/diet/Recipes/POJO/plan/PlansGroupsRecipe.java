package com.wsoteam.diet.Recipes.POJO.plan;

import android.util.Log;

import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PlansGroupsRecipe implements GroupsRecipes {

    private AtomicInteger indexBreakfast = new AtomicInteger(0),
            indexLunch = new AtomicInteger(0),
            indexDinner = new AtomicInteger(0),
            indexSnack = new AtomicInteger(0);
    private Set<RecipeItem> buffer;
    Set<RecipeItem> recipeItemSet;

    private List<ListRecipes> listRecipesGroups;

    public List<RecipeForDay> getRecipeForDays() {
        return recipeForDays;
    }

    private List<RecipeForDay> recipeForDays;

    List<RecipeItem> breakfast;
    List<RecipeItem> lunch;
    List<RecipeItem> dinner;
    List<RecipeItem> snack;

    public PlansGroupsRecipe(ListRecipes listRecipes, DietPlan plan) {


        int days = plan.getCountDays();
        listRecipesGroups = new ArrayList<>();

        ListRecipes listBreakfast = new ListRecipes("Завтрак");
        breakfast = new ArrayList<>();

        ListRecipes listLunch = new ListRecipes("Обед");
        lunch = new ArrayList<>();

        ListRecipes listDinner = new ListRecipes("Ужин");
        dinner = new ArrayList<>();

        ListRecipes listSnack = new ListRecipes("Перекус");
        snack = new ArrayList<>();

        listBreakfast.setListrecipes(breakfast);
        listLunch.setListrecipes(lunch);
        listDinner.setListrecipes(dinner);
        listSnack.setListrecipes(snack);

        listRecipesGroups.add(listBreakfast);
        listRecipesGroups.add(listLunch);
        listRecipesGroups.add(listDinner);
        listRecipesGroups.add(listSnack);

        Collections.shuffle(breakfast);
        Collections.shuffle(lunch);
        Collections.shuffle(dinner);
        Collections.shuffle(snack);

        for (RecipeItem recipe:
             listRecipes.getListrecipes()) {
            if (recipe.getBreakfast().contains(plan.getFlag())){
                breakfast.add(recipe);
            }

            if (recipe.getLunch().contains(plan.getFlag())){
                lunch.add(recipe);
            }

            if (recipe.getDinner().contains(plan.getFlag())){
                dinner.add(recipe);
            }

            if (recipe.getSnack().contains(plan.getFlag())){
                snack.add(recipe);
            }
        }
        recipeForDays = new ArrayList<>();
        for (int i = 0; i < days; i++){
            buffer = new HashSet<>();
            recipeForDays.add(selectRecipe(buffer));
        }

        recipeItemSet = new HashSet<>();
        for (ListRecipes groups:
                listRecipesGroups) {
                recipeItemSet.addAll(groups.getListrecipes());
        }

    }

    RecipeForDay selectRecipe(Set<RecipeItem> buffer){
        List<RecipeItem> breakfast = new ArrayList<>();
        List<RecipeItem> lunch = new ArrayList<>();
        List<RecipeItem> dinner = new ArrayList<>();
        List<RecipeItem> snack = new ArrayList<>();

        int x = 3 * 4;
        RecipeItem temp;

        while (x > 0) {
            temp = getBreakfast();
            if (breakfast.size() < 3 && buffer.add(temp)) {
                Log.d("kkk", "selectRecipe: b");
                breakfast.add(temp);
                x--;
            }
            temp = getLunch();
            if (lunch.size() < 3 && buffer.add(temp)){
                Log.d("kkk", "selectRecipe: l");
                lunch.add(temp);
                x--;
            }
            temp = getDinner();
            if (dinner.size() < 3 && buffer.add(temp)){
                Log.d("kkk", "selectRecipe: d");
                dinner.add(temp);
                x--;
            }
            temp = getSnack();
            if (snack.size() < 3 && buffer.add(temp)){
                Log.d("kkk", "selectRecipe: s");
                snack.add(temp);
                x--;
            }
            Log.d("kkk", "selectRecipe: " + x);
        }


        RecipeForDay forDay = new RecipeForDay();
        forDay.setBreakfast(breakfast);
        forDay.setLunch(lunch);
        forDay.setDinner(dinner);
        forDay.setSnack(snack);

        return forDay;
    }

    RecipeItem getRecipeItem(List<RecipeItem> recipeItemList, AtomicInteger index){
        if (index.get() < recipeItemList.size()){
            return recipeItemList.get(index.getAndIncrement());
        } else if (index.intValue() >= recipeItemList.size()){
            index.set(0);
            Collections.shuffle(recipeItemList);
            return getRecipeItem(recipeItemList, index);
        }

        return null;
    }

    RecipeItem getBreakfast(){
        return getRecipeItem(breakfast, indexBreakfast);
    }

    RecipeItem getLunch(){
        return getRecipeItem(lunch, indexLunch);
    }

    RecipeItem getDinner(){
        return getRecipeItem(dinner, indexDinner);
    }

    RecipeItem getSnack(){
        return getRecipeItem(snack, indexSnack);
    }

    @Override
    public List<ListRecipes> getGroups() {
        return listRecipesGroups;
    }

    @Override
    public int size() {
        return recipeItemSet.size();
    }
}
