package com.wsoteam.diet.presentation.food.template.create;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.EventProperties;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.food.adapter.FoodAdapter;
import com.wsoteam.diet.presentation.global.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;

public class CreateFoodTemplateActivity extends BaseActivity implements CreateFoodTemplateView,
        TextWatcher, AdapterView.OnItemSelectedListener {

    private CreateFoodTemplatePresenter presenter;
    private FoodAdapter adapter;

    @BindView(R.id.etNameTemplate) EditText etNameTemplate;
    @BindView(R.id.sEating) Spinner eatingSpinner;
    @BindView(R.id.rvContainer) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btnSave) Button saveButton;

    AlertDialog alert;

    @ProvidePresenter
    CreateFoodTemplatePresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food_template);
        ButterKnife.bind(this);

        presenter = new CreateFoodTemplatePresenter(CiceroneModule.router(),
            new FoodTemplate("", "", "", true, new ArrayList<>()));

        adapter = new FoodAdapter(this, CiceroneModule.router());

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#32000000"));

        mToolbar.inflateMenu(R.menu.create_template_menu);
        Menu menu = mToolbar.getMenu();
        MenuItem btnClose = menu.findItem(R.id.close);
        btnClose.setOnMenuItemClickListener(item -> {
            initAlert();
            return false;
        });



        int spinnerPosition = getIntent().getIntExtra(Config.EATING_SPINNER_POSITION, 0);
        eatingSpinner.setSelection(spinnerPosition);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        etNameTemplate.addTextChangedListener(this);
        eatingSpinner.setOnItemSelectedListener(this);
        presenter.onEatingChanged(eatingSpinner.getSelectedItem().toString());

        presenter.checkIntent(getIntent());
        adapter.setPresenter(presenter);

    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.btnSave})
    public void onViewClicked(View view) {
        presenter.onSaveClicked(getIntent().getStringExtra(EventProperties.template_from));
    }


    @Override
    public void setData(FoodTemplate foodTemplate) {
        adapter.setListContent(foodTemplate.getFoodList());
        updateRecyclerView();
        etNameTemplate.setText(foodTemplate.getName());
        eatingSpinner.setSelection(getIndex(eatingSpinner, foodTemplate.getEating()));
    }

    @Override
    public void setName(String str) {
        etNameTemplate.setText(str);
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

    @Override
    public void setColorSaveButton(int i) {
        if (i < 1){
            saveButton.setBackgroundResource(R.drawable.btn_shape_grey);
        } else {
            saveButton.setBackgroundResource(R.drawable.btn_recipe_shape);
        }
    }

    //    TextWatcher start
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.onNameChanged(s.toString());
    }
//    TextWatcher end

//    AdapterView.OnItemSelectedListener start
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
//    AdapterView.OnItemSelectedListener end


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        presenter.checkListFoodsSize();
    }

    void initAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выход");
        builder.setMessage("Выйти без сохранения?");
        builder.setPositiveButton("Выйти", myClickListener);
        builder.setNegativeButton("Отмена", myClickListener);
        alert = builder.create();
        alert.show();

    }

    OnClickListener myClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    CreateFoodTemplateActivity.super.onBackPressed();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    alert.dismiss();
                    break;

            }
        }
    };

    @Override
    public void onBackPressed() {
        initAlert();
    }
}
