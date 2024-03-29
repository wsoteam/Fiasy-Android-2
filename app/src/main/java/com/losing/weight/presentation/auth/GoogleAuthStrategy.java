package com.losing.weight.presentation.auth;

import android.content.Context;
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
import com.google.firebase.auth.GoogleAuthProvider;
import com.losing.weight.BuildConfig;
import com.losing.weight.R;

public class GoogleAuthStrategy extends AuthStrategy {

  private GoogleSignInClient client;

  public GoogleAuthStrategy(@NonNull Fragment fragment) {
    super(fragment);
  }

  public static void signOut(Context context) {
    final GoogleSignInOptions options =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    GoogleSignIn.getClient(context, options).signOut();
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

      handleCredentials(GoogleAuthProvider.getCredential(account.getIdToken(), null));
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
