package com.wsoteam.diet.Amplitude;

import com.adjust.sdk.AdjustAttribution;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.wsoteam.diet.POJOProfile.TrackInfo;

public class SetStrangesProperties {

    public static void setStrangerUserProperties(TrackInfo trackInfo) {
        Identify identify = new Identify();
        if (trackInfo.getTt()!= null) {
            identify.set(AtributionsIds.tt, trackInfo.getTt());
        }
        if (trackInfo.getTn() != null) {
            identify.set(AtributionsIds.tn, trackInfo.getTn());
        }
        if (trackInfo.getNet() != null) {
            identify.set(AtributionsIds.net, trackInfo.getNet());
        }
        if (trackInfo.getCam() != null) {
            identify.set(AtributionsIds.cam, trackInfo.getCam());
        }
        if (trackInfo.getAdg() != null) {
            identify.set(AtributionsIds.adg, trackInfo.getAdg());
        }
        if (trackInfo.getCre() != null) {
            identify.set(AtributionsIds.cre, trackInfo.getCre());
        }
        if (trackInfo.getCl() != null) {
            identify.set(AtributionsIds.cl, trackInfo.getCl());
        }
        if (trackInfo.getAdid() != null) {
            identify.set(AtributionsIds.adid, trackInfo.getAdid());
        }
        Amplitude.getInstance().identify(identify);
        Amplitude.getInstance().logEvent("set_attr");
    }
}
