package com.wsoteam.diet.presentation.auth;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FacebookAuthProvider;
import java.util.Arrays;
import java.util.concurrent.CancellationException;

public class FacebookAuthStrategy extends AuthStrategy {
  private final static int FACEBOOK_REQUEST_CODE = 0xface;

  private final CallbackManager callbackManager;
  private final LoginManager loginManager;

  public FacebookAuthStrategy(Fragment fragment) {
    super(fragment);

    callbackManager = CallbackManager.Factory.create();

    loginManager = LoginManager.getInstance();
    loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override public void onSuccess(LoginResult loginResult) {
        final String fbAuthToken = loginResult.getAccessToken().getToken();
        handleCredentials(FacebookAuthProvider.getCredential(fbAuthToken));
      }

      @Override public void onCancel() {
        onAuthException(new CancellationException());
      }

      @Override public void onError(FacebookException error) {
        onAuthException(error);
      }
    });
  }

  @Override public boolean isAuthRequest(int requestCode) {
    return FACEBOOK_REQUEST_CODE == requestCode;
  }

  @Override public void onActivityResult(Intent resultData, int status) {
    super.onActivityResult(resultData, status);

    callbackManager.onActivityResult(FACEBOOK_REQUEST_CODE, status, resultData);
  }

  @Override public void release() {
    super.release();

    loginManager.unregisterCallback(callbackManager);
  }

  @Override public void enter() {
    loginManager.logInWithReadPermissions(getFragment(),
        Arrays.asList("email", "public_profile"));
  }
}
