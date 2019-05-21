package com.wsoteam.diet.Amplitude;

import com.adjust.sdk.AdjustAttribution;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;

public class SetUserProperties {

    public static void setUserProperties(AdjustAttribution userProperties) {
        Identify identify = new Identify();
        if (!userProperties.trackerToken.equals(null)) {
            identify.set(AtributionsIds.tt, userProperties.trackerToken);
        }

        if (!userProperties.trackerName.equals(null)) {
            identify.set(AtributionsIds.tn, userProperties.trackerName);
        }
        if (!userProperties.network.equals(null)) {
            identify.set(AtributionsIds.net, userProperties.network);
        }
        if (!userProperties.campaign.equals(null)) {
            identify.set(AtributionsIds.cam, userProperties.campaign);
        }
        if (!userProperties.adgroup.equals(null)) {
            identify.set(AtributionsIds.adg, userProperties.adgroup);
        }
        if (!userProperties.creative.equals(null)) {
            identify.set(AtributionsIds.cre, userProperties.creative);
        }
        if (!userProperties.clickLabel.equals(null)) {
            identify.set(AtributionsIds.cl, userProperties.clickLabel);
        }
        if (!userProperties.adid.equals(null)) {
            identify.set(AtributionsIds.adid, userProperties.adid);
        }
        Amplitude.getInstance().identify(identify);
        Amplitude.getInstance().logEvent("set_attr");
    }
}
