package com.wsoteam.diet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.POJOS.DescriptionOfDiet;
import com.wsoteam.diet.POJOS.GlobalObject;
import com.wsoteam.diet.POJOS.GroupOfFood;
import com.wsoteam.diet.POJOS.ItemOfGlobalBase;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsFood;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfPOJO;
import com.wsoteam.diet.POJOS.ListOfRecipes;

import java.util.ArrayList;
import java.util.List;

public class TestFillDB {

    public static void fiilDB(ListOfPOJO listOfPOJO, ListOfGroupsRecipes listOfGroupsRecipes) {
        DescriptionOfDiet descriptionOfDiet = new DescriptionOfDiet("name", "main", "faq",
                "exit", "contradictions");


        ArrayList<Integer> countOfItems = new ArrayList<>();
        countOfItems.add(31);
        countOfItems.add(48);
        countOfItems.add(83);
        countOfItems.add(50);
        countOfItems.add(296);
        countOfItems.add(61);
        countOfItems.add(350);
        countOfItems.add(261);
        countOfItems.add(68);
        countOfItems.add(233);
        countOfItems.add(42);
        countOfItems.add(162);
        countOfItems.add(294);
        countOfItems.add(199);
        countOfItems.add(83);
        countOfItems.add(422);
        countOfItems.add(85);
        countOfItems.add(80);
        countOfItems.add(449);
        countOfItems.add(87);
        countOfItems.add(102);
        countOfItems.add(76);
        countOfItems.add(168);
        countOfItems.add(269);
        countOfItems.add(473);
        countOfItems.add(123);
        countOfItems.add(106);
        countOfItems.add(186);
        countOfItems.add(48);
        countOfItems.add(23);
        countOfItems.add(157);





        ItemOfGlobalBase itemOfGlobalBase = new ItemOfGlobalBase("name", "des", "com",
                "prop", "cal", "prot",
                "fat", "car", "url");

        List<ItemOfGlobalBase> itemOfGlobalBases = new ArrayList<>();
        itemOfGlobalBases.add(itemOfGlobalBase);
        itemOfGlobalBases.add(itemOfGlobalBase);
        itemOfGlobalBases.add(itemOfGlobalBase);
        itemOfGlobalBases.add(itemOfGlobalBase);
        itemOfGlobalBases.add(itemOfGlobalBase);

        GroupOfFood groupOfFood = new GroupOfFood("name", itemOfGlobalBases, "url");

        List<GroupOfFood> groupOfFoods = new ArrayList<>();
        groupOfFoods.add(groupOfFood);
        groupOfFoods.add(groupOfFood);
        groupOfFoods.add(groupOfFood);
        groupOfFoods.add(groupOfFood);

        ListOfGroupsFood groupOfFoodListOfGroupsFood = new ListOfGroupsFood("name",
                groupOfFoods);

        /*ItemRecipes itemRecipes = new ItemRecipes("name", "url", "body");

        List<ItemRecipes> itemsRecipes = new ArrayList<>();
        itemsRecipes.add(itemRecipes);
        itemsRecipes.add(itemRecipes);
        itemsRecipes.add(itemRecipes);
        itemsRecipes.add(itemRecipes);
        itemsRecipes.add(itemRecipes);
        ListOfRecipes listOfRecipes1 = new ListOfRecipes("name0", "url", itemsRecipes);

        List<ItemRecipes> itemsRecipes2 = new ArrayList<>();
        itemsRecipes2.add(itemRecipes);
        itemsRecipes2.add(itemRecipes);
        itemsRecipes2.add(itemRecipes);
        itemsRecipes2.add(itemRecipes);
        itemsRecipes2.add(itemRecipes);
        itemsRecipes2.add(itemRecipes);
        ListOfRecipes listOfRecipes2 = new ListOfRecipes("name1", "url", itemsRecipes2);

        List<ItemRecipes> itemsRecipes3 = new ArrayList<>();
        itemsRecipes3.add(itemRecipes);
        itemsRecipes3.add(itemRecipes);
        itemsRecipes3.add(itemRecipes);
        itemsRecipes3.add(itemRecipes);
        itemsRecipes3.add(itemRecipes);
        itemsRecipes3.add(itemRecipes);
        itemsRecipes3.add(itemRecipes);
        ListOfRecipes listOfRecipes3 = new ListOfRecipes("name2", "url", itemsRecipes3);

        List<ItemRecipes> itemsRecipes4 = new ArrayList<>();
        itemsRecipes4.add(itemRecipes);
        itemsRecipes4.add(itemRecipes);
        itemsRecipes4.add(itemRecipes);
        itemsRecipes4.add(itemRecipes);
        ListOfRecipes listOfRecipes4 = new ListOfRecipes("name3", "url", itemsRecipes4);

        List<ItemRecipes> itemsRecipes5 = new ArrayList<>();
        itemsRecipes5.add(itemRecipes);
        itemsRecipes5.add(itemRecipes);
        itemsRecipes5.add(itemRecipes);
        itemsRecipes5.add(itemRecipes);
        itemsRecipes5.add(itemRecipes);
        itemsRecipes5.add(itemRecipes);
        itemsRecipes5.add(itemRecipes);
        ListOfRecipes listOfRecipes5 = new ListOfRecipes("name4", "url", itemsRecipes5);

        List<ItemRecipes> itemsRecipes6 = new ArrayList<>();
        itemsRecipes6.add(itemRecipes);
        itemsRecipes6.add(itemRecipes);
        itemsRecipes6.add(itemRecipes);
        itemsRecipes6.add(itemRecipes);
        itemsRecipes6.add(itemRecipes);
        ListOfRecipes listOfRecipes6 = new ListOfRecipes("name5", "url", itemsRecipes6);



        List<ListOfRecipes> listOfRecipes = new ArrayList<>();
        listOfRecipes.add(listOfRecipes1);
        listOfRecipes.add(listOfRecipes2);
        listOfRecipes.add(listOfRecipes3);
        listOfRecipes.add(listOfRecipes4);
        listOfRecipes.add(listOfRecipes5);
        listOfRecipes.add(listOfRecipes6);

        ListOfGroupsRecipes listOfGroupsRecipes = new ListOfGroupsRecipes("name", listOfRecipes);*/


        GlobalObject globalObject = new GlobalObject("GB", groupOfFoodListOfGroupsFood,
                listOfPOJO, descriptionOfDiet, listOfGroupsRecipes);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_ENTITY_FOR_DB);

        myRef.setValue(globalObject);
    }

    private static List<GroupOfFood> fillAllCellForItemsFromGlobalBase(ArrayList<Integer> count) {
        List<GroupOfFood> listOfGroupsFood = new ArrayList<>();
        ItemOfGlobalBase item = new ItemOfGlobalBase("name", "des", "com",
                "prop", "cal", "prot",
                "fat", "car", "url");
        for (int i = 0; i < count.size(); i++) {
            List<ItemOfGlobalBase> listOfItems = new ArrayList<>();
            for (int j = 0; j < count.get(i); j++) {
                listOfItems.add(item);
            }
            GroupOfFood groupOfFood = new GroupOfFood("name", listOfItems, "url");
            listOfGroupsFood.add(groupOfFood);
        }
        return listOfGroupsFood;
    }
}
