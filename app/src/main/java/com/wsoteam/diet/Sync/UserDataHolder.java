package com.wsoteam.diet.Sync;

import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.utils.RxFirebase;
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
