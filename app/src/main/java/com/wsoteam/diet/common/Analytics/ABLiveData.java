package com.wsoteam.diet.common.Analytics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ABLiveData {
  private static ABLiveData instance;
  MutableLiveData<String> abVersion = new MutableLiveData<>();

  private ABLiveData() {
  }

  public static ABLiveData getInstance() {
    if (instance == null) {
      instance = new ABLiveData();
    }
    return instance;
  }

  public MutableLiveData<String> getData() {
    return abVersion;
  }

  public void setData(String data) {
    abVersion.postValue(data);
  }
}
