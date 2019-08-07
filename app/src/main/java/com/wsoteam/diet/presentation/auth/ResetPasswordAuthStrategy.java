package com.wsoteam.diet.presentation.auth;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.utils.RxFirebase;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ResetPasswordAuthStrategy extends AuthStrategy {

  private String email;
  private String code;
  private String password;

  public ResetPasswordAuthStrategy(Fragment fragment) {
    super(fragment);
  }

  public void setEmail(String email) {
    this.email = email;

    disposeOnRelease(RxFirebase.from(FirebaseAuth.getInstance()
        .sendPasswordResetEmail(email))
        .subscribe());
  }

  public void setVerficiationCode(String code) {
    this.code = code;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override public void enter() {
    disposeOnRelease(RxFirebase.from(FirebaseAuth.getInstance()
        .confirmPasswordReset(code, password))
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(task -> {
          final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

          if (user != null) onAuthenticated(user);
        })
        .subscribe());
  }
}
