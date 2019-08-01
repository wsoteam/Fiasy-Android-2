package com.wsoteam.diet.presentation.auth;

import android.app.Activity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class AuthStrategy {
  public final static int AUTH_REQUEST = 100;

  private final Activity context;
  private final CompositeDisposable disposables = new CompositeDisposable();

  private final MutableLiveData<AuthenticationResult> userStream = new MutableLiveData<>();

  public AuthStrategy(Activity context) {
    this.context = context;
  }

  protected Activity getActivity() {
    return context;
  }

  protected void disposeOnRelease(Disposable disposable) {
    disposables.add(disposable);
  }

  protected void startActivityForResult(Intent intent) {
    context.startActivityForResult(intent, AUTH_REQUEST);
  }

  public boolean isAuthRequest(int requestCode) {
    return requestCode == AUTH_REQUEST;
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

    public boolean isSuccessfull(){
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
