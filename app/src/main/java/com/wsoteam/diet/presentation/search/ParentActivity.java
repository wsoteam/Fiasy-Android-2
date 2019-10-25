package com.wsoteam.diet.presentation.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.diary.FoodWork;
import com.wsoteam.diet.common.networking.food.FoodResultAPI;
import com.wsoteam.diet.common.networking.food.FoodSearch;
import com.wsoteam.diet.common.networking.food.suggest.Option;
import com.wsoteam.diet.common.networking.food.suggest.Suggest;
import com.wsoteam.diet.presentation.search.results.ResultsFragment;
import com.wsoteam.diet.presentation.search.results.ResultsView;
import com.wsoteam.diet.presentation.search.sections.SectionFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

public class ParentActivity extends AppCompatActivity {
  @BindView(R.id.ibStartAction) ImageButton ibStartAction;
  @BindView(R.id.spnEatingList) Spinner spnEatingList;
  @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) public EditText
      edtSearch;
  @BindView(R.id.ibActivityListAndSearchCollapsingCancelButton) ImageView
      ibCancel;
  public int spinnerId = 0;
  private boolean isCanSpeak = true;
  private FragmentManager fragmentManager;
  private final String BS_TAG = "BS_TAG";
  private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent);
    FoodWork.clearBasket();
    ButterKnife.bind(this);
    bindSpinnerChoiceEating();
    fragmentManager = getSupportFragmentManager();
    edtSearch.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        if (fragmentManager.getBackStackEntryCount() == 0) {
          fragmentManager.beginTransaction()
              .replace(R.id.searchFragmentContainer, new ResultsFragment())
              .addToBackStack(BS_TAG)
              .commit();
        }
        return false;
      }
    });

    edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          if (fragmentManager.findFragmentById(
              R.id.searchFragmentContainer) instanceof ResultsView) {
            ((ResultsView) fragmentManager.findFragmentById(
                R.id.searchFragmentContainer)).sendSearchQuery(
                edtSearch.getText().toString().replaceAll("\\s+", " "));
          }
          /*((TabsFragment) tabsAdapter.getItem(viewPager.getCurrentItem())).
              sendString(edtSearch.getText().toString().replaceAll("\\s+", " "));*/
          edtSearch.clearFocus();
          InputMethodManager inputManager =
              (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          inputManager.hideSoftInputFromWindow(edtSearch.getWindowToken(),
              InputMethodManager.HIDE_NOT_ALWAYS);
          return true;
        }
        return false;
      }
    });

    edtSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        changeSpeakButton(charSequence);
        if (fragmentManager.findFragmentById(
            R.id.searchFragmentContainer) instanceof ResultsView) {
          ((ResultsView) fragmentManager.findFragmentById(
              R.id.searchFragmentContainer)).updateSearchField(
              charSequence.toString().replaceAll("\\s+", " "));
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  @Override protected void onStart() {
    super.onStart();
    if (!isNeedContinue()) {
      fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
      fragmentManager.beginTransaction()
          .replace(R.id.searchFragmentContainer, new SectionFragment())
          .commit();
      FoodWork.clearBasket();
    }
  }

  private boolean isNeedContinue() {
    return getSharedPreferences(Config.BASKET_CONTINUE, MODE_PRIVATE).getBoolean(
        Config.BASKET_CONTINUE, false);
  }

  private void bindSpinnerChoiceEating() {
    ArrayAdapter<String> adapter = new ArrayAdapter(this,
        R.layout.item_spinner_food_search, getResources().getStringArray(R.array.eatingList));
    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_food_search);
    spnEatingList.setAdapter(adapter);
    spnEatingList.setSelection(getIntent().getIntExtra(Config.TAG_CHOISE_EATING, 0));
    spinnerId = spnEatingList.getSelectedItemPosition();
    spnEatingList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerId = position;
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void changeSpeakButton(CharSequence charSequence) {
    if (charSequence.length() > 0 && isCanSpeak) {
      isCanSpeak = false;
      Glide.with(this).load(R.drawable.ic_cancel).into(ibCancel);
    } else if (charSequence.length() == 0 && !isCanSpeak) {
      isCanSpeak = true;
      Glide.with(this).load(R.drawable.ic_speak).into(ibCancel);
    }
  }

  private void speak() {
    Intent intent = new Intent(
        RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // намерение для вызова формы обработки речи (ОР)
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // сюда он слушает и запоминает
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите!");
    startActivityForResult(intent, 1234); // вызываем активность ОР
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == 1234 && resultCode == RESULT_OK) {
      ArrayList<String> commandList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      edtSearch.setText(commandList.get(0));
      if (fragmentManager.findFragmentById(R.id.searchFragmentContainer) instanceof ResultsView) {
        ((ResultsView) fragmentManager.findFragmentById(
            R.id.searchFragmentContainer)).sendSearchQuery(
            edtSearch.getText().toString().replaceAll("\\s+", " "));
      }
      edtSearch.clearFocus();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void createPopUp() {
    PopupMenu popupMenu = new PopupMenu(this, ibStartAction);
    popupMenu.inflate(R.menu.popup_search_food);
    popupMenu.show();
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
                    /*case R.id.userNote:
                        break;*/
          case R.id.createFood:
            /*startActivity(new Intent(ActivityListAndSearch.this, ActivityCreateFood.class).putExtra(
                EventProperties.product_from, EventProperties.product_from_plus));*/
            break;

          case R.id.createTemplate:
            /*startActivity(new Intent(ActivityListAndSearch.this, CreateFoodTemplateActivity.class)
                .putExtra(Config.EATING_SPINNER_POSITION, (int) spinner.getSelectedItemId())
                .putExtra(EventProperties.template_from, EventProperties.template_from_plus));*/
            break;
          case R.id.createRecipe:
            /*startActivity(
                new Intent(ActivityListAndSearch.this, AddingRecipeActivity.class).putExtra(
                    EventProperties.recipe_from, EventProperties.recipe_from_plus));*/
            break;
        }
        return false;
      }
    });
  }

  @OnClick({
      R.id.ibActivityListAndSearchCollapsingCancelButton, R.id.ivBack, R.id.ibStartAction
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ibActivityListAndSearchCollapsingCancelButton:
        if (isCanSpeak) {
          speak();
        } else {
          edtSearch.setText("");
          /*((TabsFragment) tabsAdapter.getItem(viewPager.getCurrentItem())).
              sendClearSearchField();*/
          //call clear and show history
          edtSearch.clearFocus();
          InputMethodManager inputManager =
              (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          inputManager.hideSoftInputFromWindow(edtSearch.getWindowToken(),
              InputMethodManager.HIDE_NOT_ALWAYS);
        }
        break;
      case R.id.ivBack:
        onBackPressed();
        break;

      case R.id.ibStartAction:
        createPopUp();
        break;
    }
  }

  @Override public void onBackPressed() {
    edtSearch.setText("");
    edtSearch.clearFocus();
    if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentById(
        R.id.searchFragmentContainer) instanceof ResultsFragment && isVisibleBasket()) {
      askExit();
    } else {
      super.onBackPressed();
    }
  }

  private boolean isVisibleBasket() {
    return ((ResultsFragment) fragmentManager.findFragmentById(
        R.id.searchFragmentContainer)).getView().findViewById(R.id.cvBasket).getVisibility()
        == View.VISIBLE;
  }

  private void askExit() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    AlertDialog alertDialog = builder.create();
    LayoutInflater layoutInflater =
        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.alert_dialog_basket_exit, null);
    TextView tvCancel = view.findViewById(R.id.tvCancel);
    TextView tvExit = view.findViewById(R.id.tvExit);

    tvCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        alertDialog.cancel();
      }
    });
    tvExit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        alertDialog.cancel();
        ParentActivity.super.onBackPressed();
      }
    });

    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    alertDialog.setView(view);
    alertDialog.show();
  }
}
