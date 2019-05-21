package com.wsoteam.diet.Amplitude;

import com.adjust.sdk.AdjustAttribution;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;

public class SetUserProperties {

    public static void setUserProperties(AdjustAttribution userProperties) {
        Identify identify = new Identify();
        if (userProperties.trackerToken != null) {
            identify.set(AtributionsIds.tt, userProperties.trackerToken);
        }

        if (userProperties.trackerName != null) {
            identify.set(AtributionsIds.tn, userProperties.trackerName);
        }
        if (userProperties.network != null) {
            identify.set(AtributionsIds.net, userProperties.network);
        }
        if (userProperties.campaign != null) {
            identify.set(AtributionsIds.cam, userProperties.campaign);
        }
        if (userProperties.adgroup != null) {
            identify.set(AtributionsIds.adg, userProperties.adgroup);
        }
        if (userProperties.creative != null) {
            identify.set(AtributionsIds.cre, userProperties.creative);
        }
        if (userProperties.clickLabel != null) {
            identify.set(AtributionsIds.cl, userProperties.clickLabel);
        }
        if (userProperties.adid != null) {
            identify.set(AtributionsIds.adid, userProperties.adid);
        }
        Amplitude.getInstance().identify(identify);
        Amplitude.getInstance().logEvent("set_attr");
    }
}
