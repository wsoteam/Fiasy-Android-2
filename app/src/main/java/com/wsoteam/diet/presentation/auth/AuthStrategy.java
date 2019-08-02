package com.wsoteam.diet.presentation.auth;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wsoteam.diet.utils.RxFirebase;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class AuthStrategy {
  public final static int AUTH_REQUEST = 100;

  private final Fragment fragment;
  private final CompositeDisposable disposables = new CompositeDisposable();

  private final MutableLiveData<AuthenticationResult> userStream = new MutableLiveData<>();

  public AuthStrategy(Fragment fragment) {
    this.fragment = fragment;
  }

  protected Activity getActivity() {
    return fragment.requireActivity();
  }

  protected Fragment getFragment() {
    return fragment;
  }

  protected void disposeOnRelease(Disposable disposable) {
    disposables.add(disposable);
  }

  protected void startActivityForResult(Intent intent) {
    fragment.startActivityForResult(intent, AUTH_REQUEST);
  }

  public boolean isAuthRequest(int requestCode) {
    return requestCode == AUTH_REQUEST;
  }

  protected void handleCredentials(AuthCredential credential){
    disposeOnRelease(RxFirebase.from(FirebaseAuth.getInstance()
        .signInWithCredential(credential))
        .map(AuthResult::getUser)
        .subscribe(this::onAuthenticated, this::onAuthException));
  }

  /**
   * Public usage to observe user
   */
  public LiveData<AuthenticationResult> liveAuthResult() {
    return userStream;
  }

  public void onActivityResult(Intent resultData, int status) {

  }

  protected void onAuthenticated(FirebaseUser user) {
    userStream.postValue(new AuthenticationResult(user, null));
  }

  protected void onAuthException(Throwable throwable) {
    userStream.postValue(new AuthenticationResult(null, throwable));
  }

  public abstract void enter();

  public void release() {
    disposables.clear();
  }

  public static class AuthenticationResult {
    private final FirebaseUser user;
    private final Throwable error;

    public AuthenticationResult(FirebaseUser user, Throwable error) {
      this.user = user;
      this.error = error;
    }

    public boolean isSuccessfull() {
      return user != null;
    }

    public FirebaseUser user() {
      return user;
    }

    public Throwable error() {
      return error;
    }
  }
}
