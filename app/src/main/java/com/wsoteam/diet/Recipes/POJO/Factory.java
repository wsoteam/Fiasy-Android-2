package com.wsoteam.diet.Recipes.POJO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Factory {

    public static List<String> ingredients = Arrays.asList("4 яйца","2 авокадо","2/3 ст. л. оливкового масла","1 ст. л. молодого лука, нарезать");
    public static List<String> instruction = Arrays.asList("Разрезать надвое авокадо и вынуть ядро. Полить половинки оливковым маслом",
            "Разбить яйцо в каждое отверстие, где находилось ядро. Посыпать луком",
            "Печь при температуре 175 C в течение 15 минут или до тех пор, пока желток не приобретет желаемую консистенцию");
    public static List<String> eating = Arrays.asList("breakfast","lunch");
    public static List<String> diet = Arrays.asList("keto","vegan","3week");

    public static ListRecipes getlistRecipes(){
        ListRecipes listRecipes = new ListRecipes();
        List<RecipeItem> recipeItemList = new ArrayList<>();
        RecipeItem recipeItem = new RecipeItem(
                "Авокадо с яйцами",
                25,
                524,
                "Завтрак, который сам себя готовит",
                8,
                13,
                79,
                16.7,
                10.4,
                6.7,
                2.6,
                45.2,
                8.3,
                31.6,
                424,
                138,
                1103,
                2,
                ingredients,
                instruction,
                eating,
                diet);

        recipeItemList.add(recipeItem);
        recipeItemList.add(recipeItem);
        recipeItemList.add(recipeItem);
        recipeItemList.add(recipeItem);
        recipeItemList.add(recipeItem);

        listRecipes.setName("test");
        listRecipes.setListrecipes(recipeItemList);

        return listRecipes;
    }
}
