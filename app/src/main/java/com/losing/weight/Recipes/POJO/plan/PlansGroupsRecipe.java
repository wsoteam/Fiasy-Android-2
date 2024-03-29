package com.losing.weight.Recipes.POJO.plan;

import android.content.Context;

import com.losing.weight.DietPlans.POJO.DietPlan;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.GroupsRecipes;
import com.losing.weight.Recipes.POJO.ListRecipes;
import com.losing.weight.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

    public PlansGroupsRecipe(ListRecipes listRecipes, DietPlan plan, Context context) {


        int days = plan.getCountDays();
        listRecipesGroups = new ArrayList<>();

        breakfast = new ArrayList<>();
        ListRecipes listBreakfast = new ListRecipes(context.getString(R.string.breakfast));
        listBreakfast.setListrecipes(breakfast);
        listRecipesGroups.add(listBreakfast);

        lunch = new ArrayList<>();
        ListRecipes listLunch = new ListRecipes(context.getString(R.string.lunch));
        listLunch.setListrecipes(lunch);
        listRecipesGroups.add(listLunch);

        dinner = new ArrayList<>();
        ListRecipes listDinner = new ListRecipes(context.getString(R.string.dinner));
        listDinner.setListrecipes(dinner);
        listRecipesGroups.add(listDinner);

        snack = new ArrayList<>();
        ListRecipes listSnack = new ListRecipes(context.getString(R.string.snack));
        listSnack.setListrecipes(snack);
        listRecipesGroups.add(listSnack);


        for (RecipeItem recipe:
             listRecipes.getListrecipes()) {

            if (recipe.getName().equals("name")){
                //Log.d("kkk", "PlansGroupsRecipe: default name");
                continue;
            }

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

        Collections.shuffle(breakfast);
        Collections.shuffle(lunch);
        Collections.shuffle(dinner);
        Collections.shuffle(snack);

        recipeForDays = new ArrayList<>();
        if (Locale.getDefault().getLanguage().equals("ru")) {
            buffer = new HashSet<>();
            int bufIndex = 0;
            for (int i = 0; i < days; i++, bufIndex++) {
                if (bufIndex > 0) {
                    buffer = new HashSet<>();
                    bufIndex = 0;
                }
                recipeForDays.add(selectRecipe(buffer));
            }
        } else {
            for (int i = 0; i < days; i++) {
                recipeForDays.add(selectRecipe(listRecipes.getListrecipes(), plan.getFlag()));
            }
        }

        recipeItemSet = new HashSet<>();
        for (ListRecipes groups:
                listRecipesGroups) {
                recipeItemSet.addAll(groups.getListrecipes());
        }

    }

  RecipeForDay selectRecipe( List<RecipeItem> recipes, String flag) {

    Set<RecipeItem> recipesSet = new HashSet<>();

    Collections.shuffle(recipes);
    RecipeForDay forDay = new RecipeForDay();

    for (int i = 0, count = 0; i < recipes.size() && count < 3; i++) {
      if (recipes.get(i).getBreakfast().contains(flag)) {
        if (recipesSet.add(recipes.get(i))) count++;
      }
    }

    forDay.setBreakfast(new ArrayList<RecipeItem>(recipesSet));
    recipesSet.clear();
    Collections.shuffle(recipes);

    for (int i = 0, count = 0; i < recipes.size() && count < 3; i++) {
      if (recipes.get(i).getLunch().contains(flag)) {
        if (recipesSet.add(recipes.get(i))) count++;
      }
    }

    forDay.setLunch(new ArrayList<RecipeItem>(recipesSet));
    recipesSet.clear();
    Collections.shuffle(recipes);

    for (int i = 0, count = 0; i < recipes.size() && count < 3; i++) {
      if (recipes.get(i).getDinner().contains(flag)) {
        if (recipesSet.add(recipes.get(i))) count++;
      }
    }

    forDay.setDinner(new ArrayList<RecipeItem>(recipesSet));
    recipesSet.clear();
    Collections.shuffle(recipes);

    for (int i = 0, count = 0; i < recipes.size() && count < 3; i++) {
      if (recipes.get(i).getSnack().contains(flag)) {
        if (recipesSet.add(recipes.get(i)))count++;
      }
    }

    forDay.setSnack(new ArrayList<RecipeItem>(recipesSet));
    return forDay;
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
                //Log.d("kkk", "selectRecipe: b");
                breakfast.add(temp);
                x--;
            }
            temp = getLunch();
            if (lunch.size() < 3 && buffer.add(temp)){
                //Log.d("kkk", "selectRecipe: l");
                lunch.add(temp);
                x--;
            }
            temp = getDinner();
            if (dinner.size() < 3 && buffer.add(temp)){
                //Log.d("kkk", "selectRecipe: d");
                dinner.add(temp);
                x--;
            }
            temp = getSnack();
            if (snack.size() < 3 && buffer.add(temp)){
                //Log.d("kkk", "selectRecipe: s");
                snack.add(temp);
                x--;
            }
            //Log.d("kkk", "selectRecipe: " + x);
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
