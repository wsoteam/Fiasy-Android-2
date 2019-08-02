package com.wsoteam.diet.presentation.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.RxFirebase;

public class GoogleAuthStrategy extends AuthStrategy {

  private GoogleSignInClient client;

  public GoogleAuthStrategy(@NonNull Fragment fragment) {
    super(fragment);
  }

  private GoogleSignInClient googleClient() {
    if (client != null) {
      return client;
    }

    final GoogleSignInOptions options =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getActivity().getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    client = GoogleSignIn.getClient(getActivity(), options);
    return client;
  }

  @Override public void onActivityResult(Intent resultData, int status) {
    super.onActivityResult(resultData, status);

    final Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(resultData);
    try {
      // Google Sign In was successful, authenticate with Firebase
      final GoogleSignInAccount account = task.getResult(ApiException.class);

      if (account == null) {
        onAuthException(new GoogleAuthException("Account not selected"));
        return;
      }

      final AuthCredential credential = GoogleAuthProvider
          .getCredential(account.getIdToken(), null);

      disposeOnRelease(RxFirebase.from(FirebaseAuth.getInstance()
          .signInWithCredential(credential))
          .map(AuthResult::getUser)
          .subscribe(this::onAuthenticated, this::onAuthException));

    } catch (ApiException e) {
      onAuthException(e);

      // Google Sign In failed, update UI appropriately
      if (BuildConfig.DEBUG) {
        Log.w("GoogleAuthStrategy", "Google sign in failed", e);
      }
    }
  }

  @Override public void enter() {
    startActivityForResult(googleClient().getSignInIntent());
  }
}
