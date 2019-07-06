package com.wsoteam.diet.presentation.food.template.create;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.food.adapter.FoodAdapter;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class CreateFoodTemplateActivity extends BaseActivity implements CreateFoodTemplateView,
        TextWatcher, AdapterView.OnItemSelectedListener {

    @Inject
    @InjectPresenter
    CreateFoodTemplatePresenter presenter;

    @Inject Context context;
    @Inject FoodAdapter adapter;
    @Inject LinearLayoutManager layoutManager;

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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#32000000"));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        etNameTemplate.addTextChangedListener(this);
        eatingSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.btnAddFoods, R.id.btnLeft, R.id.btnSave})
    public void onViewClicked(View view) {
       switch (view.getId()){
           case R.id.btnAddFoods:
               presenter.onAddFoodClicked();
               break;
           case R.id.btnLeft:
               presenter.onCancelClicked();
               break;
           case R.id.btnSave:
               presenter.onSaveClicked();
               break;
       }
    }


    @Override
    public void setData(FoodTemplate foodTemplate) {
        adapter.setListContent(foodTemplate.getFoodList());
        updateRecyclerView();
        etNameTemplate.setText(foodTemplate.getName());
        eatingSpinner.setSelection(getIndex(eatingSpinner, foodTemplate.getEating()));
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }

//    TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.onNameChanged(s.toString());
    }


//    AdapterView.OnItemSelectedListener
    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            presenter.onEatingChanged(eatingSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
