package com.wsoteam.diet.POJOProfile;

import java.io.Serializable;

public class TrackInfo implements Serializable {
    private String amplitudeUserId;
    private String amplitudeDeviceId;
    private String adjustId;

    public TrackInfo() {
    }

    public TrackInfo(String amplitudeUserId, String amplitudeDeviceId, String adjustId) {
        this.amplitudeUserId = amplitudeUserId;
        this.amplitudeDeviceId = amplitudeDeviceId;
        this.adjustId = adjustId;
    }

    public String getAmplitudeUserId() {
        return amplitudeUserId;
    }

    public void setAmplitudeUserId(String amplitudeUserId) {
        this.amplitudeUserId = amplitudeUserId;
    }

    public String getAmplitudeDeviceId() {
        return amplitudeDeviceId;
    }

    public void setAmplitudeDeviceId(String amplitudeDeviceId) {
        this.amplitudeDeviceId = amplitudeDeviceId;
    }

    public String getAdjustId() {
        return adjustId;
    }

    public void setAdjustId(String adjustId) {
        this.adjustId = adjustId;
    }
}
