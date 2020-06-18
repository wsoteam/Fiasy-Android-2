package com.losing.weight.presentation.profile.questions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.squareup.picasso.Picasso;
import com.losing.weight.ABConfig;
import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.POJOProfile.Profile;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.GroupsHolder;
import com.losing.weight.Sync.WorkWithFirebaseDB;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.UserProperty;
import com.losing.weight.common.helpers.BodyCalculates;
import com.losing.weight.presentation.premium.AnastasiaStoryFragment;
import com.losing.weight.presentation.premium.GraphPrePremium;
import com.losing.weight.presentation.premium.WheelFortuneActivity;
import com.losing.weight.utils.NetworkService;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;

public class QuestionsCalculationsActivity extends AppCompatActivity {
  @BindView(R.id.loader)
  ImageView loader;

  private boolean isNeedShowOnboard, createUser;
  private CompositeDisposable disposable = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_questions_calculations);
    ButterKnife.bind(this);

    final Drawable drawable = loader.getDrawable();
    if (drawable instanceof Animatable) {
      ((Animatable) drawable).start();
    }

    final SharedPreferences sp = getSharedPreferences(Config.ONBOARD_PROFILE, MODE_PRIVATE);

    final boolean isFemale = sp.getBoolean(Config.ONBOARD_PROFILE_SEX, true);
    final int age = sp.getInt(Config.ONBOARD_PROFILE_YEARS, BodyCalculates.DEFAULT_AGE);
    final int height = sp.getInt(Config.ONBOARD_PROFILE_HEIGHT, BodyCalculates.DEFAULT_HEIGHT);
    final double weight =
        (double) sp.getInt(Config.ONBOARD_PROFILE_WEIGHT, (int) BodyCalculates.DEFAULT_WEIGHT);
    final String activity =
        sp.getString(Config.ONBOARD_PROFILE_ACTIVITY, getString(R.string.level_none));
    final String diff =
        sp.getString(Config.ONBOARD_PROFILE_PURPOSE, getString(R.string.dif_level_easy));

    final Profile profileFinal =
        BodyCalculates.calculate(this, weight, height, age, isFemale, activity, diff);

    profileFinal.setFirstName(sp.getString(Config.ONBOARD_PROFILE_NAME,
        BodyCalculates.DEFAULT_FIRST_NAME));

    if (getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).getBoolean(
        Config.IS_NEED_SHOW_ONBOARD, false)) {
      isNeedShowOnboard = true;
      getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).edit()
          .putBoolean(Config.IS_NEED_SHOW_ONBOARD, false)
          .apply();
    }
    createUser = getIntent().getBooleanExtra(Config.CREATE_PROFILE, true);

    final Disposable d = NetworkService.getInstance().getApi()
        .getArticles()
        .flatMap(response -> Observable.fromIterable(response.getResults()))
        .doOnNext(article ->
            Picasso.get()
                .load(article.getImage())
                .resizeDimen(R.dimen.article_card_width, R.dimen.article_card_height)
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .fetch()
        )
        .subscribe(Functions.emptyConsumer(), Throwable::printStackTrace);

    disposable.add(d);

    GroupsHolder.loadRecipes(this, receipts -> {
      final Disposable r = Flowable.fromIterable(receipts.getListrecipes())
          .doOnNext(receipt -> {
            Picasso.get()
                .load(receipt.getUrl())
                .resizeDimen(R.dimen.receipt_container_width, R.dimen.receipt_container_height)
                .centerCrop()
                .fetch();
          })
          .subscribe();

      disposable.add(r);
    });

    loader.postDelayed(() -> saveProfile(isNeedShowOnboard, profileFinal, createUser), 4000);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposable.clear();
  }

  void saveProfile(boolean isNeedShowOnboard, Profile profile, boolean createProfile) {
    if (createProfile) {
      //Intent intent = new Intent(this, MainAuthNewActivity.class);
      Intent intent = new Intent(this, AfterQuestionsActivity.class);
      if (isNeedShowOnboard) {
        Box box = new Box();
        box.setBuyFrom(EventProperties.trial_from_onboard);
        box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
        box.setOpenFromIntrodaction(true);
        box.setOpenFromPremPart(false);
        intent.putExtra(Config.CREATE_PROFILE, true)
            .putExtra(Config.INTENT_PROFILE, profile);
      } else {
        intent.putExtra(Config.INTENT_PROFILE, profile);
      }
      //startActivity(intent);
      moveNext(profile);
      finish();
    }

    if (profile != null) {
      UserProperty.setUserProperties(profile, this, false);
      WorkWithFirebaseDB.putProfileValue(profile);
    }
  }

  private void moveNext(Profile profile) {
    Box box = new Box();
    box.setBuyFrom(EventProperties.trial_from_onboard);
    box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
    box.setOpenFromIntrodaction(true);
    box.setOpenFromPremPart(false);
    box.setProfile(profile);
    Intent intent = new Intent();

    String abVersion = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
        getString(ABConfig.KEY_FOR_SAVE_STATE, "default");

    switch (abVersion) {
      case ABConfig.A:
      case ABConfig.B:
      case ABConfig.C:
      case ABConfig.D:
        intent = new Intent(this, ActivitySubscription.class);
        break;
      case ABConfig.E:
      case ABConfig.F:
        intent = new Intent(this, AnastasiaStoryFragment.class);
        break;
      case ABConfig.G:
      case ABConfig.H:
        intent = new Intent(this, WheelFortuneActivity.class);
        break;
      case ABConfig.I:
        intent = new Intent(this, GraphPrePremium.class);
        break;
    }
    intent.putExtra(Config.TAG_BOX, box);
    startActivity(intent);
  }

  private void markAfterPremRoad() {
    getSharedPreferences(Config.AFTER_PREM_ROAD, MODE_PRIVATE).edit()
        .putBoolean(Config.AFTER_PREM_ROAD, true)
        .commit();
  }
}