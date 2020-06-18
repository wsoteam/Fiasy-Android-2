package com.losing.weight.common.remote.POJO;

import java.io.Serializable;

public class StoreVersion implements Serializable {
    private int versionCode;
    private long timeUntilHardUpdate;
    private long weekPeriod;

    public StoreVersion() {
    }

    public StoreVersion(int versionCode, long timeUntilHardUpdate, long weekPeriod) {
        this.versionCode = versionCode;
        this.timeUntilHardUpdate = timeUntilHardUpdate;
        this.weekPeriod = weekPeriod;
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

    public long getWeekPeriod() {
        return weekPeriod;
    }

    public void setWeekPeriod(long weekPeriod) {
        this.weekPeriod = weekPeriod;
    }
}
