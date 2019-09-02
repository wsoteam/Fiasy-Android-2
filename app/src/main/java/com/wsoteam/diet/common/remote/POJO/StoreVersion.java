package com.wsoteam.diet.common.remote.POJO;

import java.io.Serializable;

public class StoreVersion implements Serializable {
    private int versionCode;
    private long timeUntilHardUpdate;

    public StoreVersion() {
    }

    public StoreVersion(int versionCode, long timeUntilHardUpdate) {
        this.versionCode = versionCode;
        this.timeUntilHardUpdate = timeUntilHardUpdate;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public long getTimeUntilHardUpdate() {
        return timeUntilHardUpdate;
    }

    public void setTimeUntilHardUpdate(long timeUntilHardUpdate) {
        this.timeUntilHardUpdate = timeUntilHardUpdate;
    }
}
