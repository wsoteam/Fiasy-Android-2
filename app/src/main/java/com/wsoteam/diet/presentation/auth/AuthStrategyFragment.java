package com.wsoteam.diet.presentation.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.questions.QuestionsActivity;
import com.wsoteam.diet.utils.RichTextUtils.RichText;
import com.wsoteam.diet.views.InAppNotification;
import java.io.IOException;

public abstract class AuthStrategyFragment extends Fragment {

  protected static final SparseArrayCompat<Class<? extends AuthStrategy>> strategy =
      new SparseArrayCompat<Class<? extends AuthStrategy>>() {{
        put(R.id.auth_strategy_google, GoogleAuthStrategy.class);
        put(R.id.auth_strategy_facebook, FacebookAuthStrategy.class);
        put(R.id.auth_strategy_login, EmailLoginAuthStrategy.class);
        put(R.id.auth_strategy_reset, ResetPasswordAuthStrategy.class);
      }};

  protected final Observer<AuthStrategy.AuthenticationResult> userObserver =
      authenticationResult -> {
        if (authenticationResult == null) return;

        if (authenticationResult.isSuccessful()) {
          onAuthorized(authenticationResult.user(), authenticationResult.isNewUser());
        } else {
          onAuthException(authenticationResult.error());
        }
      };

  private AuthStrategy authStrategy;
  private InAppNotification notification;

  protected <T extends AuthStrategy> T getStrategyByType(Class<T> strategyType) {
    final AuthStrategy strategy;
    if (strategyType == GoogleAuthStrategy.class) {
      strategy = new GoogleAuthStrategy(this);
    } else if (strategyType == FacebookAuthStrategy.class) {
      strategy = new FacebookAuthStrategy(this);
    } else if (strategyType == EmailLoginAuthStrategy.class) {
      strategy = new EmailLoginAuthStrategy(this);
    } else {
      strategy = null;
    }

    return (T) strategy;
  }

  protected void bindAuthStrategies() {
    if (getView() == null) {
      return;
    }

    for (int strategyViewIndex = 0; strategyViewIndex < strategy.size(); strategyViewIndex++) {

      final int viewId = strategy.keyAt(strategyViewIndex);

      final View authStrategyButton = getView().findViewById(viewId);

      if (authStrategyButton == null) {
        continue;
      }

      final Class<? extends AuthStrategy> strategyType = strategy.get(viewId);
      getView().findViewById(viewId).setOnClickListener(v -> authorize(strategyType));
    }
  }

  protected void prepareAuthStrategy(AuthStrategy strategy) {
    final InAppNotification notification = getNotification();

    notification.setText(getString(R.string.auth_state_logging_in));
    notification.setProgressVisible(true, false);
    notification.show(getView(), InAppNotification.DURATION_FOREVER);
  }

  protected void onAuthorized(FirebaseUser user, boolean newUser) {
    if (BuildConfig.DEBUG) {
      Log.d("AuthStrategy", "Logged in as: " + user.getDisplayName());
    }

    final InAppNotification notification = getNotification();
    notification.setProgressVisible(false, true);

    if (TextUtils.isEmpty(user.getDisplayName())) {
      notification.setText("Добро пожаловать :)");
    } else {
      notification.setText(TextUtils.concat("Привет, ", new RichText(user.getDisplayName())
          .bold()
          .text()));
    }

    notification.show(getView(), InAppNotification.DURATION_QUICK);

    if (getActivity() != null) {
      final Activity activity = requireActivity();

      if (newUser) {
        activity.startActivity(new Intent(requireContext(), QuestionsActivity.class));
      } else {
        activity.startActivity(new Intent(requireContext(), MainActivity.class));
      }

      activity.finish();
    }
  }

  protected void onAuthException(Throwable error) {
    error.printStackTrace();

    handleDefaultErrors(error);
  }

  protected void handleDefaultErrors(Throwable error) {
    getNotification().setProgressVisible(false, false);

    final InAppNotification notification = getNotification();

    if (error instanceof FirebaseAuthUserCollisionException) {
      notification.setText(getString(R.string.auth_user_using_existing_account));
    } else if (error instanceof FirebaseAuthInvalidCredentialsException) {
      notification.setText("Сессия подключения просрочена");
    } else if (error instanceof FirebaseAuthInvalidUserException) {
      notification.setText(getString(R.string.auth_user_not_found));
    } else if (error instanceof IOException) {
      notification.setText("Нет подключения к интернету :(");
    } else {
      notification.setText("Не удалось войти");
    }

    notification.show(getView(), InAppNotification.DURATION_LONG);
  }

  protected void authorize(Class<? extends AuthStrategy> strategyType) {
    if (getNotification().isAttached()) {
      getNotification().dismiss();
    }

    final AuthStrategy targetStrategy;

    if (getAuthStrategy() != null && getAuthStrategy().getClass() == strategyType) {
      targetStrategy = getAuthStrategy();
    } else {
      targetStrategy = getStrategyByType(strategyType);
      setAuthStrategy(targetStrategy);
    }

    if (targetStrategy != null) {
      prepareAuthStrategy(targetStrategy);

      targetStrategy.enter();
    } else if (BuildConfig.DEBUG) {
      Log.e("AuthStrategyFragment", strategyType.getSimpleName() + " not implemented");
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (authStrategy != null && authStrategy.isAuthRequest(requestCode)) {
      authStrategy.onActivityResult(data, resultCode);
    }
  }

  protected void releaseCurrentAuthStrategy() {
    if (authStrategy != null) {
      authStrategy.liveAuthResult().removeObserver(userObserver);
      authStrategy.release();
      authStrategy = null;
    }
  }

  protected void setAuthStrategy(AuthStrategy authStrategy) {
    releaseCurrentAuthStrategy();

    if (authStrategy == null) {
      return;
    }

    this.authStrategy = authStrategy;
    this.authStrategy.liveAuthResult().observe(getViewLifecycleOwner(), userObserver);
  }

  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);

    // Facebook and third-party auth providers, might destroy instance of fragment,
    // therefore we have to restore auth-strategy instance

    if (savedInstanceState != null) {
      final String className = savedInstanceState.getString("currentAuthStrategy");

      if (TextUtils.isEmpty(className)) {
        return;
      }

      assert className != null;

      try {
        //noinspection unchecked
        Class<? extends AuthStrategy> currentAuthStrategy =
            (Class<? extends AuthStrategy>) Class.forName(className);

        setAuthStrategy(getStrategyByType(currentAuthStrategy));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  protected void removeCurrentNotification() {
    if (notification != null && notification.isAttached()) {
      notification.dismiss();
      notification = null;
    }
  }

  protected InAppNotification getNotification() {
    removeCurrentNotification();

    return notification = new InAppNotification(requireContext());
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    if (authStrategy != null) {
      outState.putString("currentAuthStrategy", authStrategy.getClass().getCanonicalName());
    }

    super.onSaveInstanceState(outState);
  }

  public AuthStrategy getAuthStrategy() {
    return authStrategy;
  }

  @Override public void onDestroy() {
    super.onDestroy();

    releaseCurrentAuthStrategy();
  }
}
