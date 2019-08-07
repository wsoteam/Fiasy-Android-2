package com.wsoteam.diet.presentation.auth;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.IntentUtils;
import com.wsoteam.diet.utils.RichTextUtils.RichText;

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
            .colorRes(requireContext(), R.color.blue)
            .underline()
            .onClick(v -> IntentUtils.openWebLink(v.getContext(), "http://fiasy.com/PrivacyPolice.html"));

    final TextView privacyPolicyView = view.findViewById(R.id.privacyPolicy);
    privacyPolicyView.setMovementMethod(LinkMovementMethod.getInstance());
    privacyPolicyView.setText(concat(getString(R.string.privacy_policy_title), "\n",
        actionOpenPrivacyPolicy.text()));

    final RichText actionSignIn = new RichText(getString(R.string.action_sign_in_label))
        .colorRes(requireContext(), R.color.blue)
        .onClick(v -> signIn());

    final TextView signInView = view.findViewById(R.id.signIn);
    signInView.setMovementMethod(LinkMovementMethod.getInstance());
    signInView.setText(concat(getString(R.string.sign_in_already_have_account), " ",
        actionSignIn.text()));

    view.findViewById(R.id.signUp).setOnClickListener(v -> signUp());

    bindAuthStrategies();
  }

  protected void signUp(){
    requireFragmentManager()
        .beginTransaction()
        .replace(R.id.container, new SignUpFragment())
        .addToBackStack(SignUpFragment.class.getName())
        .commitAllowingStateLoss();
  }

  protected void signIn(){
    requireFragmentManager()
        .beginTransaction()
        .replace(R.id.container, new SignInFragment())
        .addToBackStack(SignInFragment.class.getName())
        .commitAllowingStateLoss();
  }
}