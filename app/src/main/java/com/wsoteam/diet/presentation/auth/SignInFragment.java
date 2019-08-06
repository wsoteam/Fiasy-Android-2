package com.wsoteam.diet.presentation.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.collection.SparseArrayCompat;
import com.google.android.material.textfield.TextInputLayout;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.InputValidation;
import com.wsoteam.diet.utils.InputValidation.EmailValidation;
import com.wsoteam.diet.utils.InputValidation.MinLengthValidation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignInFragment extends AuthStrategyFragment {
  private static final SparseArrayCompat<List<InputValidation>> formValidators =
      new SparseArrayCompat<>();

  static {
    formValidators.put(R.id.username, Arrays.asList(
        new MinLengthValidation(R.string.constraint_error_username_min_length, 5),
        new EmailValidation(R.string.constraint_error_username_email)
    ));

    formValidators.put(R.id.password, Arrays.asList(
        new MinLengthValidation(R.string.constraint_error_password_min_length, 8)
    ));
  }

  private CardView signInButton;

  private final List<TextInputLayout> formInputs = new ArrayList<>();
  private final Runnable validateForm = () -> {
    signInButton.setEnabled(validateForm(false));
  };

  private final TextWatcher validateOnChange = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      signInButton.removeCallbacks(validateForm);
      signInButton.postDelayed(validateForm, 200);
    }
  };

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_auth_login, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    for (int i = 0; i < formValidators.size(); i++) {
      final TextInputLayout target = view.findViewById(formValidators.keyAt(i));

      if (target == null) {
        continue;
      }

      formInputs.add(target);

      target.getEditText().addTextChangedListener(validateOnChange);
    }

    bindAuthStrategies();

    signInButton = view.findViewById(R.id.auth_strategy_login);
    signInButton.setEnabled(false);
    signInButton.setOnClickListener(v -> {
      if (validateForm(true)) {
        authorize(strategy.get(R.id.auth_strategy_login));
      }
    });
  }

  private boolean validateForm(boolean displayError) {
    if (getView() == null) {
      return false;
    }

    for (TextInputLayout target : formInputs) {
      final List<InputValidation> validators = formValidators.get(target.getId());

      if (validators == null) {
        continue;
      }

      for (final InputValidation validator : validators) {
        final CharSequence error = validator.validate(target.getEditText());

        if (!TextUtils.isEmpty(error)) {
          if (displayError) {
            target.setError(error);
          }

          return false;
        }
      }
    }

    return true;
  }
}
