package com.losing.weight.Sync;

import com.losing.weight.Sync.POJO.UserData;

import io.reactivex.Single;

public class UserDataHolder {
  private static UserData userData;

  public void bindObjectWithHolder(UserData globalObject) {
    this.userData = globalObject;
  }

  public static Single<UserData> user() {
    if (userData == null) {
      return Single.error(new IllegalStateException("User not cached"));
    }
    return Single.just(userData);
  }

  public static UserData getUserData() {
    return userData;
  }

  public static void clearObject() {
    userData = new UserData();
  }
}
