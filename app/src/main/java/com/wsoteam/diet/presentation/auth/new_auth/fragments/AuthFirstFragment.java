package com.wsoteam.diet.presentation.auth.new_auth.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.IntentUtils;
import com.wsoteam.diet.utils.RichTextUtils.RichText;

import static android.text.TextUtils.concat;

public class AuthFirstFragment extends Fragment {

  public static AuthFirstFragment newInstance() {
    return new AuthFirstFragment();
  }

  private TextView privacyPolicyView;
  private TextView signInView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_auth_first, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final RichText actionOpenPrivacyPolicy = new RichText(getString(R.string.privacy_policy_link_span_text))
        .colorRes(requireContext(), R.color.blue)
        .underline()
        .onClick(v -> IntentUtils.openWebLink(v.getContext(), "https://google.com"));

    privacyPolicyView = view.findViewById(R.id.privacyPolicy);
    privacyPolicyView.setMovementMethod(LinkMovementMethod.getInstance());
    privacyPolicyView.setText(concat(getString(R.string.privacy_policy_title), "\n",
        actionOpenPrivacyPolicy.text()));

    final RichText actionSignIn = new RichText(getString(R.string.action_sign_in_label))
        .colorRes(requireContext(), R.color.blue)
        .onClick(v -> authorize());

    signInView = view.findViewById(R.id.signIn);
    signInView.setMovementMethod(LinkMovementMethod.getInstance());
    signInView.setText(concat(getString(R.string.sign_in_already_have_account), " ",
        actionSignIn.text()));
  }

  private void authorize() {
    // todo add sign in behaviour
  }
}