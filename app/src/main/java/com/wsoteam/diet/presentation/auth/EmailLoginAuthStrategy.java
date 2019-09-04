package com.wsoteam.diet.presentation.auth;

import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wsoteam.diet.utils.RxFirebase;

public class EmailLoginAuthStrategy extends AuthStrategy {

  private Account account;

  public EmailLoginAuthStrategy(Fragment fragment) {
    super(fragment);
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  @Override public void enter() {
    if (account == null) {
      throw new IllegalStateException("account not given");
    }

    Task<AuthResult> authTask;

    if (account.isExisting()) {
      authTask = FirebaseAuth.getInstance()
          .signInWithEmailAndPassword(account.email, account.password);
    } else {
      authTask = FirebaseAuth.getInstance()
          .createUserWithEmailAndPassword(account.email, account.password);
    }

    disposeOnRelease(RxFirebase.from(authTask).subscribe(
        this::onAuthenticated,
        this::onAuthException
    ));
  }

  public static class Account {
    private final String email;
    private final String password;
    private final boolean existing;

    public Account(String email, String password, boolean existing) {
      this.email = email;
      this.password = password;
      this.existing = existing;
    }

    public String getEmail() {
      return email;
    }

    public String getPassword() {
      return password;
    }

    public boolean isExisting() {
      return existing;
    }
  }
}
