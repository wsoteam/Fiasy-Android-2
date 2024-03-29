package com.losing.weight.presentation.auth;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.losing.weight.utils.RxFirebase;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ResetPasswordAuthStrategy extends AuthStrategy {

  private String code;
  private String password;

  public ResetPasswordAuthStrategy(Fragment fragment) {
    super(fragment);
  }

  public Single<Void> sendVerificationCode(String email) {
    return RxFirebase.from(FirebaseAuth.getInstance()
        .sendPasswordResetEmail(email));
  }

  public Single<String> checkValidCode(String code) {
    return RxFirebase.from(FirebaseAuth.getInstance()
        .verifyPasswordResetCode(code));
  }

  @Override public void enter() {
    disposeOnRelease(RxFirebase.from(FirebaseAuth.getInstance()
        .confirmPasswordReset(code, password))
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(task -> {
          final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

          if (user != null) onAuthenticated(new AuthenticationResult(user, null, false));
        })
        .subscribe());
  }
}
