package com.losing.weight.common.backward;

import android.util.Log;

import com.losing.weight.App;
import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;
import com.losing.weight.POJOProfile.FavoriteFood;
import com.losing.weight.Sync.POJO.UserData;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.Sync.WorkWithFirebaseDB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OldFavoriteConverter {
    static FoodDAO foodDAO = App.getInstance().getFoodDatabase().foodDAO();

    public static void run() {
        Single.fromCallable(() -> {
            List<FavoriteFood> foods = getOldObjects();
            return foods;
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<FavoriteFood>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<FavoriteFood> foods) {
                        Log.e("LOL", String.valueOf(foods.size()));
                        saveNewFoods(foods);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LOL", e.getLocalizedMessage());
                    }
                });
    }

    private static void saveNewFoods(List<FavoriteFood> foods) {
        for (int i = 0; i < foods.size(); i++) {
            WorkWithFirebaseDB.rewriteFoodFavorite(foods.get(i));
        }
        Log.e("LOL", "success");
    }


    private static List<FavoriteFood> getOldObjects() {
        List<FavoriteFood> newFavorites = new ArrayList<>();
        UserData userData = UserDataHolder.getUserData();
        Log.e("LOL", String.valueOf(userData.getFoodFavorites().size()));

        if (userData.getFoodFavorites() != null && userData.getFoodFavorites().size() > 0) {
            Iterator iterator = userData.getFoodFavorites().entrySet().iterator();
            while (iterator.hasNext()) {
                Log.e("LOL", "enter kek");
                Map.Entry pair = (Map.Entry) iterator.next();
                FavoriteFood oldFavoriteFood = (FavoriteFood) pair.getValue();
                String key = (String) pair.getKey();
                Log.e("LOL", oldFavoriteFood.toString());
                if (oldFavoriteFood.getName() == null) {
                    Log.e("LOL", "second enter");
                    oldFavoriteFood = convertOldFavorite(oldFavoriteFood, key);
                    newFavorites.add(oldFavoriteFood);
                }
            }
        }
        return newFavorites;
    }

    private static FavoriteFood convertOldFavorite(FavoriteFood oldFavoriteFood, String key) {
        Food food = foodDAO.getById(oldFavoriteFood.getId());
        Log.e("LOL", food.toString());
        FavoriteFood newFavorite = new FavoriteFood(food.getId(), food.getFullInfo(), key, food.getName(),
                food.getBrand(), food.getPortion(), food.isLiquid(), food.getKilojoules(), food.getCalories(), food.getProteins(), food.getCarbohydrates(),
                food.getSugar(), food.getFats(), food.getSaturatedFats(), food.getMonoUnSaturatedFats(), food.getPolyUnSaturatedFats(), food.getCholesterol(), food.getCellulose(),
                food.getSodium(), food.getPottassium(), food.getPercentCarbohydrates(), food.getPercentFats(), food.getPercentProteins());
        return newFavorite;
    }
}
