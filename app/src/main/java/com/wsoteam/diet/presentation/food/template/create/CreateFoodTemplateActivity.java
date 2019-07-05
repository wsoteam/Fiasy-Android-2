package com.wsoteam.diet.presentation.food.template.create;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class CreateFoodTemplateActivity extends BaseActivity implements CreateFoodTemplateView {

    private static final String TAG = "template";

    @Inject
    @InjectPresenter
    CreateFoodTemplatePresenter presenter;

    @BindView(R.id.etNameTemplate) EditText etNameTemplate;
    @BindView(R.id.sEating) Spinner eatingSpinner;
    @BindView(R.id.rvContainer) RecyclerView recyclerView;

    @ProvidePresenter
    CreateFoodTemplatePresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food_template);
        ButterKnife.bind(this);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }


    @OnClick({R.id.btnAddFoods, R.id.btnLeft})
    public void onViewClicked(View view) {
       switch (view.getId()){
           case R.id.btnAddFoods:
               presenter.addFoods();
               break;
           case R.id.btnLeft:
               break;
           case R.id.btnSave:
               presenter.saveTemplate();
               break;
       }
    }
}
