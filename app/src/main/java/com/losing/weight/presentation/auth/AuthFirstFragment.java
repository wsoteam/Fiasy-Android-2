package com.losing.weight.presentation.auth;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.utils.IntentUtils;
import com.losing.weight.utils.RichTextUtils.RichText;

import static android.text.TextUtils.concat;

public class AuthFirstFragment extends AuthStrategyFragment {

  public static AuthFirstFragment newInstance() {
    return new AuthFirstFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_auth_first, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final RichText actionOpenPrivacyPolicy =
        new RichText(getString(R.string.privacy_policy_link_span_text))
            .underline()
            .onClick(v -> IntentUtils.openWebLink(v.getContext(), getString(R.string.url_privacy_police)))
            .color(0xa63a3a3a);

    final TextView privacyPolicyView = view.findViewById(R.id.privacyPolicy);
    privacyPolicyView.setMovementMethod(LinkMovementMethod.getInstance());
    privacyPolicyView.setText(concat(getString(R.string.privacy_policy_title), "\n",
        actionOpenPrivacyPolicy.text()));

    final RichText actionSignIn = new RichText(getString(R.string.signIn))
        .onClick(v -> signIn())
        .textScale(1.2f)
        .colorRes(requireContext(), R.color.orange_light);

    final TextView signInView = view.findViewById(R.id.signIn);
    signInView.setMovementMethod(LinkMovementMethod.getInstance());
    signInView.setText(concat(getString(R.string.account_available), " ",
        actionSignIn.text()));

    view.findViewById(R.id.signUp).setOnClickListener(v -> signUp());

    bindAuthStrategies();
  }

  protected void signUp(){
    Events.logPushButtonReg(EventProperties.enter_push_button_email);
    requireFragmentManager()
        .beginTransaction()
        .replace(R.id.container, new SignUpFragment())
        .addToBackStack(SignUpFragment.class.getName())
        .commitAllowingStateLoss();
  }

  protected void signIn(){
      Log.d("kkk", "signIn()");
    Events.logPushButtonReg(EventProperties.enter_push_button_enter);
    requireFragmentManager()
        .beginTransaction()
        .replace(R.id.container, new SignInFragment())
        .addToBackStack(SignInFragment.class.getName())
        .commitAllowingStateLoss();
  }
}