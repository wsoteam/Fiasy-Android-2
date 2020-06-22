package com.losing.weight.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.amplitude.api.Amplitude;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.losing.weight.ABConfig;
import com.losing.weight.AmplitudaEvents;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.DietPlans.POJO.DietModule;
import com.losing.weight.DietPlans.POJO.DietPlansHolder;
import com.losing.weight.EntryPoint.ActivitySplash;
import com.losing.weight.MainScreen.Dialogs.RateDialogs;
import com.losing.weight.MainScreen.Support.AsyncWriteFoodDB;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.GroupsHolder;
import com.losing.weight.Recipes.v2.GroupsFragment;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.articles.ArticleSeriesActivity;
import com.losing.weight.common.Analytics.ABLiveData;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.common.Analytics.SavedConst;
import com.losing.weight.common.helpers.BodyCalculates;
import com.losing.weight.common.remote.UpdateChecker;
import com.losing.weight.presentation.activity.UserActivityFragment;
import com.losing.weight.presentation.diary.DiaryViewModel;
import com.losing.weight.presentation.fab.FabMenuViewModel;
import com.losing.weight.presentation.fab.MainFabMenu;
import com.losing.weight.presentation.diary.DiaryFragment;
import com.losing.weight.model.ArticleViewModel;
import com.losing.weight.presentation.fab.SelectMealDialogFragment;
import com.losing.weight.presentation.measurment.MeasurmentActivity;
import com.losing.weight.presentation.plans.browse.BrowsePlansFragment;
import com.losing.weight.presentation.profile.section.ProfileFragment;

import com.losing.weight.presentation.teach.TeachActivity;
import com.losing.weight.presentation.teach.TeachUtil;

import com.losing.weight.presentation.search.ParentActivity;
import com.losing.weight.utils.BlurBuilder;
import com.losing.weight.utils.Subscription;
import com.losing.weight.views.fabmenu.FloatingActionMenu;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  private final long TIMESTAMP = 1577705306338l; //30.12.2019

  @BindView(R.id.flFragmentContainer)
  FrameLayout flFragmentContainer;
  @BindView(R.id.bnv_main)
  BottomNavigationView bnvMain;
  @BindView(R.id.bottom_sheet)
  LinearLayout bottomSheet;
  @BindView(R.id.floatingActionButton)
  FloatingActionButton fab;
  @BindView(R.id.fabBackground)
  FrameLayout fabBackground;
  @BindView(R.id.backgroundImg)
  ImageView fabBackgroundImg;
  @BindView(R.id.mainConstraint)
  ConstraintLayout constraintLayout;

  private FragmentTransaction transaction;
  private BottomSheetBehavior bottomSheetBehavior;
  private boolean isMainFragment = true;
  private Window window;

  private FloatingActionMenu fabMenu;
  private LiveData<String> abLiveData;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = item -> setActiveTab(item.getItemId());

  private int interCounter = 0;

  private Observer<String> abObserver = new Observer<String>() {
    @Override public void onChanged(String s) {
        checkSub();
        Log.e("LOL", "вызов из mainActivity");
    }
  };

  private void hideFab() {
    hideFabMenu();
    fab.hide();
  }

  private void hideFabMenu() {
    if (fabMenu != null) {
      fabMenu.close(true);
    }
  }

  private void showFab() {
    if (Locale.getDefault().getLanguage().equals("ru")) {
      fab.show();
    }
  }

  FloatingActionMenu.MenuStateChangeListener menuStateChangeListener =
      new FloatingActionMenu.MenuStateChangeListener() {
        @Override
        public void onMenuOpened(FloatingActionMenu menu) {
          fabBackgroundImg.setImageBitmap(
              BlurBuilder.blur(getApplicationContext(), constraintLayout));
          fabBackground.setVisibility(View.VISIBLE);

          if (isMainFragment) {
          } else {
            menu.close(true);
          }
        }

        @Override
        public void onMenuClosed(FloatingActionMenu menu) {
          fabBackground.setVisibility(View.GONE);
        }
      };

  private boolean setActiveTab(int id) {
    isMainFragment = false;
    transaction = getSupportFragmentManager().beginTransaction();
    window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    Box box = new Box();
    box.setSubscribe(false);
    box.setOpenFromPremPart(true);
    box.setOpenFromIntrodaction(false);

    getSupportFragmentManager().popBackStack(Config.RECIPE_BACK_STACK,
        FragmentManager.POP_BACK_STACK_INCLUSIVE);

    hideFab();
    interstitialFromMainScreen();
    switch (id) {
      case R.id.bnv_main_diary: {
        isMainFragment = true;
        DiaryFragment fragment = new DiaryFragment();
        transaction.replace(R.id.flFragmentContainer, fragment).commit();
        //                window.setStatusBarColor(Color.parseColor("#AE6A23"));
        showFab();
        return true;
      }
      case R.id.bnv_main_articles: {
        Amplitude.getInstance().logEvent(Events.CHOOSE_ARTICLES);
        box.setComeFrom(AmplitudaEvents.view_prem_content);
        box.setBuyFrom(EventProperties.trial_from_articles);
        isMainFragment = false;
        window.setStatusBarColor(getResources().getColor(R.color.highlight_line_color));
        switch (Locale.getDefault().getLanguage()) {
          case "ru": {
            transaction.replace(R.id.flFragmentContainer,
                new com.losing.weight.articles.ListArticlesFragment()).commit();
            return true;
          }
          default: {
            com.losing.weight.articles.BurlakovAuthorFragment burlakovAuthorFragment =
                new com.losing.weight.articles.BurlakovAuthorFragment();
            //                            burlakovAuthorFragment.setClickListener(v -> {
            //                                    transaction.replace(R.id.flFragmentContainer,
            //                                        new ArticleSeriesFragment()).commit();
            //                            });
            if (UserDataHolder.getUserData().getArticleSeries() != null &&
                UserDataHolder.getUserData().getArticleSeries().containsKey("burlakov")) {
              transaction.replace(R.id.flFragmentContainer,
                  new com.losing.weight.articles.ArticleSeriesFragment()).commit();
            } else {
              transaction.replace(R.id.flFragmentContainer,
                  burlakovAuthorFragment).commit();
            }
            return true;
          }
        }
      }
      case R.id.bnv_main_trainer:
        isMainFragment = false;
        transaction.replace(R.id.flFragmentContainer, new BrowsePlansFragment()).commit();
        return true;
      case R.id.bnv_main_recipes:
        isMainFragment = false;
        transaction.replace(R.id.flFragmentContainer, new GroupsFragment()).commit();
        return true;
      case R.id.bnv_main_profile:
        Amplitude.getInstance().logEvent(Events.VIEW_PROFILE);
        isMainFragment = false;
        transaction.replace(R.id.flFragmentContainer, new ProfileFragment()).commit();
        window.setStatusBarColor(Color.parseColor("#AE6A23"));
        return true;
    }
    return false;
  }

  private void startPrem() {
    Box box = new Box();
    box.setSubscribe(false);
    box.setOpenFromPremPart(true);
    box.setOpenFromIntrodaction(false);
    //TODO trial_from_* метка для амплитуды
    box.setComeFrom(AmplitudaEvents.view_prem_content);
    box.setBuyFrom("main_screen");
    Intent intent = new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box);
    startActivity(intent);
  }

  private String getABVersion() {
    return getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).getString(
        ABConfig.KEY_FOR_SAVE_STATE, "default");
  }

  @Override
  protected void onResume() {
    super.onResume();

    FiasyAds.adStatus.setValue(!Subscription.check(this));

    handlGrade(Calendar.getInstance().getTimeInMillis());
    new UpdateChecker(this).runChecker();

    Log.e("LOL", FirebaseAuth.getInstance().getCurrentUser().getUid());
    Log.e("LOL", String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp()));
    hideFabMenu();


  }


  private void handlGrade(long currentTime) {
    long timeStartingPoint =
        getSharedPreferences(Config.STARTING_POINT, MODE_PRIVATE).getLong(Config.STARTING_POINT, 0);
    boolean isAddedFoodEarly =
        getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).getBoolean(Config.IS_ADDED_FOOD,
            false);
    int gradeStatus = getSharedPreferences(Config.IS_GRADE_APP, MODE_PRIVATE).
        getInt(Config.IS_GRADE_APP, Config.NOT_VIEW_GRADE_DIALOG);
    if ((currentTime - timeStartingPoint) >= Config.ONE_DAY && gradeStatus != Config.GRADED) {
      if (isAddedFoodEarly) {
        RateDialogs.showWhiteDialog(this);
      }
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_drawer);

    ButterKnife.bind(this);
    abLiveData = ABLiveData.getInstance().getData();
    abLiveData.observeForever(abObserver);
    bnvMain.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    bnvMain.setOnNavigationItemReselectedListener(menuItem -> {
      if (menuItem.getItemId() != R.id.bnv_main_diary) {
        FabMenuViewModel.isNeedClose.setValue(true);
      }
    });

    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    DiaryFragment fragment = new DiaryFragment();
    if (getSupportFragmentManager().findFragmentByTag("diary") == null) {
      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.flFragmentContainer, fragment, "diary")
          .commit();
    }

    //checkForcedGrade();
    new AsyncWriteFoodDB().execute(MainActivity.this);
    if (GroupsHolder.getGroupsRecipes() == null) {
      loadRecipes();
    }

    ViewModelProviders.of(this).get(ArticleViewModel.class).getData();

    if (DietPlansHolder.get() == null) {
      loadDietPlans();
    }

    logEvents();

    BodyCalculates.handleProfile();

    checkDeepLink(getApplicationContext());
//TODO
    if (/*true ||*/ TeachUtil.isNeedOpen(getApplicationContext()) && Locale.getDefault()
        .getLanguage()
        .equals("ru")) {

      View rootView = constraintLayout;
      rootView.getViewTreeObserver().addOnGlobalLayoutListener(
              new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                  //Remove the listener before proceeding
                  rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                  Handler h = new Handler();
                  Runnable r = new Runnable() {
                    @Override
                    public void run() {
                      try {
                        TeachUtil.blurBitmap.setValue(BlurBuilder.blur(getApplicationContext(), rootView));
                        startActivity(new Intent(MainActivity.this, TeachActivity.class));
                      } catch (Exception e) {
                        e.printStackTrace();
                      }

                    }
                  };
                  h.postDelayed(r, 1_000);

                }
              }
      );
    }

    if (Locale.getDefault().getLanguage().equals("ru")) {
      fabMenu = MainFabMenu.Companion.initFabMenu(this, fab, menuStateChangeListener,
          activityListener, measurementListener, mealListener, waterListener, fabButtonListener);
      FabMenuViewModel.isNeedClose.observe(this, fabMenuStatusObserver);
      FabMenuViewModel.fabState.observe(this, fabState);
    } else {
      fabBackground.setVisibility(View.GONE);
      fab.hide();
    }


    Events.logReview(5, "kek");

    FiasyAds.openInter(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    abLiveData.removeObserver(abObserver);
  }


  private void checkSub() {
    if (!isSub() && isStopVersion() && isNewUser()) {
      Intent intent = new Intent(this, ActivitySubscription.class);
      Box box = new Box();
      box.setBuyFrom(EventProperties.trial_from_nec);
      box.setComeFrom(EventProperties.trial_from_nec);
      box.setOpenFromPremPart(false);
      box.setOpenFromIntrodaction(false);
      intent.putExtra(Config.TAG_BOX, box);
      startActivity(intent);
    }
  }

  private boolean isNewUser() {
    return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp() > TIMESTAMP;
  }

  private boolean isStopVersion() {
    String version = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
        getString(ABConfig.KEY_FOR_SAVE_STATE, "default");
    return version.equals(ABConfig.C)
        || version.equals(ABConfig.D)
        || version.equals(ABConfig.F)
        || version.equals(ABConfig.H);
  }

  private boolean isSub() {
    return getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE).getBoolean(Config.STATE_BILLING,
        false);
  }

  private void checkDeepLink(Context context) {

    String deepLinkAction = DeepLink.getAction(context);
    if (deepLinkAction != null) {
      switch (deepLinkAction) {
        case DeepLink.Start.PREMIUM: {
          startPrem();
          DeepLink.deleteAction(context);
          break;
        }

        case DeepLink.Start.ARTICLE: {
          bnvMain.setSelectedItemId(R.id.bnv_main_articles);
          DeepLink.deleteAction(context);
          break;
        }
        case DeepLink.Start.RECIPE: {
          bnvMain.setSelectedItemId(R.id.bnv_main_recipes);
          DeepLink.deleteAction(context);
          break;
        }

        case DeepLink.Start.DIETS: {
          bnvMain.setSelectedItemId(R.id.bnv_main_trainer);
          DeepLink.deleteAction(context);
          break;
        }

        case DeepLink.Start.NUTRITIONIST: {
          startActivity(new Intent(context, ArticleSeriesActivity.class));
          DeepLink.deleteAction(context);
          break;
        }
        case DeepLink.Start.MEASUREMENT: {
          startActivity(new Intent(context, MeasurmentActivity.class));
          DeepLink.deleteAction(context);
          break;
        }
        case DeepLink.Start.ADD_FOOD: {
          startActivity(new Intent(context, ParentActivity.class)
              .putExtra(Config.INTENT_DATE_FOR_SAVE,
                  ParentActivity.prepareDate(Calendar.getInstance())));
          DeepLink.deleteAction(context);
          break;
        }
      }
    }
  }

  private void logEvents() {
    if (getSharedPreferences(SavedConst.SEE_PREMIUM, MODE_PRIVATE).getBoolean(
        SavedConst.SEE_PREMIUM, false)) {
      Events.logSuccessOnboarding(
          getSharedPreferences(SavedConst.HOW_END, MODE_PRIVATE).getString(SavedConst.HOW_END,
              EventProperties.onboarding_success_reopen));
      getSharedPreferences(SavedConst.SEE_PREMIUM, MODE_PRIVATE).edit()
          .remove(SavedConst.SEE_PREMIUM)
          .commit();
      getSharedPreferences(SavedConst.HOW_END, MODE_PRIVATE).edit()
          .remove(SavedConst.HOW_END)
          .commit();
    }
  }

  private void checkForcedGrade() {
    if (getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE).getBoolean(
        Config.IS_NEED_SHOW_GRADE_DIALOG, false)) {
      RateDialogs.showGradeDialog(this, true);
      getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE).
          edit().putBoolean(Config.IS_NEED_SHOW_GRADE_DIALOG, false).apply();
    }
  }

  private void loadRecipes() {
    GroupsHolder.loadRecipes(getApplicationContext(), null);
  }

  @OnClick({ R.id.ibSheetClose, R.id.btnReg })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ibSheetClose:
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        break;
      case R.id.btnReg:
        startActivity(new Intent(MainActivity.this, ActivitySplash.class)
            .putExtra(Config.IS_NEED_REG, true)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        break;
    }
  }

  private void loadDietPlans() {
    //DatabaseReference myRef = database.getReference("PLANS");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    switch (Locale.getDefault().getLanguage().toUpperCase()) {
      case "EN":
      case "ES":
      case "PT":
      case "DE": {
        myRef = database.getReference(Locale.getDefault().getLanguage().toUpperCase() + "/plans");
        break;
      }
      case "RU": {
        myRef = database.getReference("PLANS");
        break;
      }
      default: {
        myRef = database.getReference("EN/plans");
      }
    }

    myRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        DietModule dietModule = dataSnapshot.getValue(DietModule.class);
        DietPlansHolder.bind(dietModule);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }

  @Override
  public void onBackPressed() {

    if (fabMenu != null && fabMenu.isOpen()) {
      hideFab();
      return;
    }

    if (isMainFragment) {
      super.onBackPressed();
    } else {
      isMainFragment = true;
      getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentContainer,
          new DiaryFragment()).commitNowAllowingStateLoss();
      window.setStatusBarColor(Color.parseColor("#AE6A23"));
      bnvMain.setSelectedItemId(R.id.bnv_main_diary);
    }
  }

  private View.OnClickListener activityListener = v -> {

    final UserActivityFragment target = new UserActivityFragment();

    getSupportFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, target, target.getClass().getSimpleName())
        .addToBackStack(null)
        .commitAllowingStateLoss();
    hideFabMenu();
  };

  private View.OnClickListener measurementListener = v -> {
    startActivity(new Intent(getApplicationContext(), MeasurmentActivity.class));
    hideFabMenu();
  };

  private View.OnClickListener mealListener = v -> {
    new SelectMealDialogFragment().show(getSupportFragmentManager(),
        SelectMealDialogFragment.class.getName());
    hideFabMenu();
  };

  private View.OnClickListener waterListener = v -> {
    hideFabMenu();
    DiaryViewModel.Companion.getScrollToPosition().setValue(1);
  };

  private View.OnClickListener fabButtonListener = v -> {

  };

  private Observer<Boolean> fabMenuStatusObserver = isNeedClose -> {
    if (isNeedClose) hideFab();
  };

  private Observer<String> fabState = state -> {
    if (state != null) {
      switch (state) {
        case FabMenuViewModel.FAB_HIDE: {
          fab.hide();
          break;
        }
        case FabMenuViewModel.FAB_SHOW: {
          fab.show();
          break;
        }
      }
    }
  };

  private void interstitialFromMainScreen(){
    interCounter++;

    if (interCounter > 2){
      FiasyAds.openInter(this);
      interCounter = 0;
    }
  }
}
