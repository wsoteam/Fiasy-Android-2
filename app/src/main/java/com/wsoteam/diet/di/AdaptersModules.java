package com.wsoteam.diet.di;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.presentation.food.adapter.FoodAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Router;

@Module
public class AdaptersModules {

    @Provides
    FoodAdapter provideFoodListAdapter(Context context, Router router){
        return new FoodAdapter(context, router);
    }

    @Provides
    FoodTemplate provideFoodTemplate(){
        return new FoodTemplate("", "", "", new ArrayList<>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Context context){
        return new LinearLayoutManager(context);
    }

}
