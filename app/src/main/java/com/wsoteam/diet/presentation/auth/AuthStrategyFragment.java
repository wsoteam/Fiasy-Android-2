package com.wsoteam.diet.presentation.auth;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.R;

public abstract class AuthStrategyFragment extends Fragment {

  protected static final SparseArrayCompat<Class<? extends AuthStrategy>> strategy =
      new SparseArrayCompat<Class<? extends AuthStrategy>>() {{
        put(R.id.auth_strategy_google, GoogleAuthStrategy.class);
      }};

  protected final Observer<AuthStrategy.AuthenticationResult> userObserver =
      authenticationResult -> {
        if (authenticationResult == null) return;

        if (authenticationResult.isSuccessfull()) {
          onAuthorized(authenticationResult.user());
        } else {
          onAuthException(authenticationResult.error());
        }
      };

  private AuthStrategy authStrategy;

  protected <T extends AuthStrategy> T getStrategyByType(Class<T> strategyType) {
    final AuthStrategy strategy;
    if (strategyType == GoogleAuthStrategy.class) {
      strategy = new GoogleAuthStrategy(this);
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

  }

  protected void onAuthorized(FirebaseUser user){

  }

  protected void onAuthException(Throwable error) {

  }

  protected void authorize(Class<? extends AuthStrategy> strategyType) {
    final AuthStrategy targetStrategy = getStrategyByType(strategyType);

    setAuthStrategy(targetStrategy);

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

  public AuthStrategy getAuthStrategy() {
    return authStrategy;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    releaseCurrentAuthStrategy();
  }
}
