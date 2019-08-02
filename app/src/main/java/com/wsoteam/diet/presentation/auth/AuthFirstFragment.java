package com.wsoteam.diet.presentation.auth;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.collection.SparseArrayCompat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.IntentUtils;
import com.wsoteam.diet.utils.RichTextUtils.RichText;

import static android.text.TextUtils.concat;

public class AuthFirstFragment extends Fragment {

  public static AuthFirstFragment newInstance() {
    return new AuthFirstFragment();
  }

  private static final SparseArrayCompat<Class<? extends AuthStrategy>> strategy =
      new SparseArrayCompat<Class<? extends AuthStrategy>>() {{
        put(R.id.auth_strategy_google, GoogleAuthStrategy.class);
      }};

  private AuthStrategy authStrategy;
  private Observer<AuthStrategy.AuthenticationResult> userObserver = authenticationResult -> {
    if (authenticationResult == null) return;

    if (authenticationResult.isSuccessfull()) {
      if (BuildConfig.DEBUG) {
        Log.d("AuthStrategy", "Signed in as: " + authenticationResult.user().getDisplayName());
      }
    }
  };

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_auth_first, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final RichText actionOpenPrivacyPolicy =
        new RichText(getString(R.string.privacy_policy_link_span_text))
            .colorRes(requireContext(), R.color.blue)
            .underline()
            .onClick(v -> IntentUtils.openWebLink(v.getContext(), "https://google.com"));

    final TextView privacyPolicyView = view.findViewById(R.id.privacyPolicy);
    privacyPolicyView.setMovementMethod(LinkMovementMethod.getInstance());
    privacyPolicyView.setText(concat(getString(R.string.privacy_policy_title), "\n",
        actionOpenPrivacyPolicy.text()));

    final RichText actionSignIn = new RichText(getString(R.string.action_sign_in_label))
        .colorRes(requireContext(), R.color.blue)
        .onClick(v -> authorize(null));

    final TextView signInView = view.findViewById(R.id.signIn);
    signInView.setMovementMethod(LinkMovementMethod.getInstance());
    signInView.setText(concat(getString(R.string.sign_in_already_have_account), " ",
        actionSignIn.text()));

    for (int strategyViewIndex = 0; strategyViewIndex < strategy.size(); strategyViewIndex++) {

      final int viewId = strategy.keyAt(strategyViewIndex);
      final Class<? extends AuthStrategy> strategyType = strategy.get(viewId);

      view.findViewById(viewId).setOnClickListener(v -> authorize(strategyType));
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (authStrategy != null && authStrategy.isAuthRequest(requestCode)) {
      authStrategy.onActivityResult(data, resultCode);
    }
  }

  private void releaseCurrentAuthStrategy() {
    if (authStrategy != null) {
      authStrategy.liveAuthResult().removeObserver(userObserver);
      authStrategy.release();
      authStrategy = null;
    }
  }

  private void authorize(Class<? extends AuthStrategy> strategyType) {
    if (strategyType == GoogleAuthStrategy.class) {
      authStrategy = new GoogleAuthStrategy(this);
    } else {
      authStrategy = null;
    }

    if (authStrategy != null) {
      authStrategy.liveAuthResult().observe(getViewLifecycleOwner(), userObserver);
      authStrategy.enter();
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    releaseCurrentAuthStrategy();
  }
}