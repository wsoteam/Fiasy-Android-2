package com.wsoteam.diet.di;

import android.content.Context;

import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.presentation.auth.main.MainAuthPresenter;
import com.wsoteam.diet.presentation.food.template.create.CreateFoodTemplatePresenter;
import com.wsoteam.diet.presentation.intro.IntroPresenter;
import com.wsoteam.diet.presentation.profile.edit.EditProfilePresenter;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Router;

@Module
public class PresentersModule {

    @Provides
    IntroPresenter provideIntroPresenter(Router router) {
        return new IntroPresenter(router);
    }

    @Provides
    EditProfilePresenter provideEditProfilePresenter(Context context, Router router) {
        return new EditProfilePresenter(context, router);
    }

    @Provides
    MainAuthPresenter provideMainAuthPresenter(Context context, Router router) {
        return new MainAuthPresenter(context, router);
    }

    @Provides
    CreateFoodTemplatePresenter provideCreateFoodTemplatePresenter(Router router, FoodTemplate foodTemplate){
        return new CreateFoodTemplatePresenter(router, foodTemplate);
    }
}