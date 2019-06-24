package com.wsoteam.diet.BranchOfAnalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.Controller.TabsAdapter;
import com.wsoteam.diet.BranchOfAnalyzer.Fragments.FragmentFavorites;
import com.wsoteam.diet.BranchOfAnalyzer.Fragments.FragmentSearch;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.adding.AddingRecipeActivity;
import com.wsoteam.diet.Recipes.adding.ListAddedRecipeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityListAndSearch extends AppCompatActivity {
    @BindView(R.id.spnEatingList) Spinner spinner;
    @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText edtSearchField;
    @BindView(R.id.ibStartAction) ImageButton ibStartAction;
    @BindView(R.id.searchFragmentContainer) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabs;
    private TabsAdapter tabsAdapter;
    public int spinnerId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_search);
        ButterKnife.bind(this);
        bindSpinnerChoiceEating();
        updateUI();
        getSupportFragmentManager().beginTransaction().add(R.id.searchFragmentContainer, new FragmentSearch()).commit();
        edtSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((TabsFragment) tabsAdapter.getItem(viewPager.getCurrentItem())).
                        sendString(charSequence.toString().replaceAll("\\s+", " "));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (edtSearchField.getText().toString().length() != 0) {
                    edtSearchField.setText("");
                }
                tabs.getTabAt(i).select();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        Amplitude.getInstance().logEvent(AmplitudaEvents.attempt_add_food);
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_search_food);

    }

    private void updateUI() {
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), createFragmentsList());
        viewPager.setAdapter(tabsAdapter);
    }

    private List<Fragment> createFragmentsList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentSearch());
        fragments.add(new FragmentFavorites());
        fragments.add(new FragmentFavorites());
        fragments.add(new ListAddedRecipeFragment());
        return fragments;
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // намерение для вызова формы обработки речи (ОР)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // сюда он слушает и запоминает
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What can you tell me?");
        startActivityForResult(intent, 1234); // вызываем активность ОР
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> commandList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            edtSearchField.setText(commandList.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindSpinnerChoiceEating() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.item_spinner_food_search, getResources().getStringArray(R.array.eatingList));
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search);
        spinner.setAdapter(adapter);
        spinner.setSelection(getIntent().getIntExtra(Config.TAG_CHOISE_EATING, 0));
        //spinnerId = spinner.getSelectedItemPosition();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @OnClick({R.id.ibActivityListAndSearchCollapsingCancelButton, R.id.ivBack, R.id.ibStartAction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibActivityListAndSearchCollapsingCancelButton:
                edtSearchField.setText("");
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ibStartAction:
                createPopUp();
                break;
        }
    }

    private void createPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, ibStartAction);
        popupMenu.inflate(R.menu.popup_search_food);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.userNote:
                        break;
                    case R.id.createFood:
                        break;
                    case R.id.createEating:
                        break;
                    case R.id.createTemplate:
                        break;
                    case R.id.createRecipe:
                        startActivity(new Intent(ActivityListAndSearch.this, AddingRecipeActivity.class));
                        break;
                }
                return false;
            }
        });
    }

}

