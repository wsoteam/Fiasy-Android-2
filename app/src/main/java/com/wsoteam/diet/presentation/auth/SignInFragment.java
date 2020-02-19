package com.wsoteam.diet.presentation.auth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.auth.EmailLoginAuthStrategy.Account;
import com.wsoteam.diet.utils.InputValidation;
import com.wsoteam.diet.utils.InputValidation.EmailValidation;
import com.wsoteam.diet.utils.InputValidation.MinLengthValidation;
import com.wsoteam.diet.utils.ViewsExtKt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignInFragment extends AuthStrategyFragment {
  private static final SparseArrayCompat<List<InputValidation>> formValidators =
      new SparseArrayCompat<>();

  private InternetBad internetBad;

  static {
    formValidators.put(R.id.username, Arrays.asList(
        new MinLengthValidation(R.string.constraint_error_username_min_length, 5),
        new EmailValidation(R.string.write_email)
    ));

    formValidators.put(R.id.password, Arrays.asList(
        new MinLengthValidation(R.string.constraint_error_password_min_length, 5)
    ));

    formValidators.put(R.id.password2, Arrays.asList(
        new MinLengthValidation(R.string.constraint_error_password_min_length, 5)
    ));
  }

  private CardView signInButton;

  private final List<TextInputLayout> formInputs = new ArrayList<>();
  private final Runnable validateForm = () -> {
    signInButton.setActivated(validateForm(false));
    signInButton.setClickable(true);
  };

  private final TextWatcher validateOnChange = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      clearInputErrors();

      signInButton.removeCallbacks(validateForm);
      signInButton.postDelayed(validateForm, 200);
    }
  };

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_auth_login, container, false);
  }

  protected List<TextInputLayout> getFormInputs() {
    return formInputs;
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    for (int i = 0; i < formValidators.size(); i++) {
      final TextInputLayout target = view.findViewById(formValidators.keyAt(i));

      if (target == null) {
        continue;
      }

      formInputs.add(target);

      target.getEditText().setOnEditorActionListener((v, actionId, event) -> {
        if(actionId == EditorInfo.IME_ACTION_DONE){
          ViewsExtKt.hideKeyboard(v);
          return true;
        } else {
          return false;
        }
      });
      target.getEditText().addTextChangedListener(validateOnChange);
    }

    bindAuthStrategies();

    final TextView forgetPassword = view.findViewById(R.id.resetPass);

    if (forgetPassword != null) {
      forgetPassword.setOnClickListener( v -> resetPass());
    }

    view.findViewById(R.id.backButton)
        .setOnClickListener(v -> getFragmentManager().popBackStack());


    signInButton = view.findViewById(R.id.auth_strategy_login);
    signInButton.setClickable(true);
    signInButton.setActivated(false);
    signInButton.setOnClickListener(v -> {
      if (validateForm(true) && isInternet()) {
        clearInputErrors();

        authorize(strategy.get(R.id.auth_strategy_login));
      }
    });


  }

  private boolean isInternet(){
    if (hasNetwork(getContext())){
      return true;
    }else {
      if (internetBad != null) internetBad.show();
      return false;
    }
  }

  private boolean hasNetwork(Context context) {
    if (context == null) return false;
    final NetworkInfo activeNetwork =
            ContextCompat.getSystemService(context, ConnectivityManager.class)
                    .getActiveNetworkInfo();

    return activeNetwork != null && activeNetwork.isConnected();
  }

  private void clearInputErrors() {
    for (TextInputLayout target : formInputs) {
      target.setError(null);
    }
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    if(getActivity() instanceof InternetBad){
      internetBad = ((InternetBad)getActivity());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    internetBad = null;
  }

  @Override protected void prepareAuthStrategy(AuthStrategy strategy) {
    super.prepareAuthStrategy(strategy);

    if (strategy instanceof EmailLoginAuthStrategy) {
      final EmailLoginAuthStrategy prep = (EmailLoginAuthStrategy) strategy;
      prep.setAccount(getUserInputAccount());
    }
  }

  @Override protected void onAuthException(Throwable error) {
    error.printStackTrace();
    //Events.logEnterError();
    if (getView() == null) {
      return;
    }

    if (error instanceof FirebaseAuthInvalidUserException) {
      setInputException(R.id.username, getString(R.string.auth_user_not_found));
    } else if (error instanceof FirebaseAuthInvalidCredentialsException) {
      setInputException(R.id.password, getString(R.string.auth_user_password_missmatch));
      setInputException(R.id.username, getString(R.string.auth_user_email_missmatch));
    } else {
      handleDefaultErrors(error);
    }
  }

  protected void setInputException(@IdRes int targetId, @NonNull CharSequence message) {
    removeCurrentNotification();

    final TextInputLayout target = getView().findViewById(targetId);
    target.setError(message);
  }

  protected Account getUserInputAccount() {
    String username = null;
    String password = null;

    for (TextInputLayout formInput : formInputs) {
      if (formInput.getId() == R.id.username) {
        username = formInput.getEditText().getText().toString();
      } else if (formInput.getId() == R.id.password) {
        password = formInput.getEditText().getText().toString();
      }
    }

    return new Account(username, password, true);
  }

  protected boolean validateForm(boolean displayError) {
    if (getView() == null) {
      return false;
    }

    boolean hasErrors = false;

    for (TextInputLayout target : formInputs) {
      final List<InputValidation> validators = formValidators.get(target.getId());

      if (validators == null) {
        continue;
      }

      for (final InputValidation validator : validators) {
        final CharSequence error = validator.validate(target.getEditText());

        if (!TextUtils.isEmpty(error)) {
          if (BuildConfig.DEBUG) {
            Log.d("ManualAuth", String.format("%s throws an error: %s",
                validator.getClass().getSimpleName(), error));
          }

          if (displayError) {
            target.setError(error);
            hasErrors = true;
          } else {
            return false;
          }
        } else {
          if (BuildConfig.DEBUG) {
            Log.d("ManualAuth", String.format("%s passed",
                validator.getClass().getSimpleName()));
          }
        }
      }
    }

    return !hasErrors;
  }

  private void resetPass(){
//    Events.logPushButtonReg(EventProperties.enter_push_button_enter);
    requireFragmentManager()
            .beginTransaction()
            .replace(R.id.container, new ResetPassFragment())
            .addToBackStack(ResetPassFragment.class.getName())
            .commitAllowingStateLoss();
  }
}
