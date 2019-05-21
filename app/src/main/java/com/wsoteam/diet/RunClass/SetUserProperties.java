package com.wsoteam.diet.RunClass;

import com.adjust.sdk.AdjustAttribution;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;

public class SetUserProperties {

    public static void setUserProperties(AdjustAttribution userProperties){
        Identify identify = new Identify();
        identify.set(AtributionsIds.tt, userProperties.trackerToken);
        identify.set(AtributionsIds.tn, userProperties.trackerName);
        identify.set(AtributionsIds.net, userProperties.network);
        identify.set(AtributionsIds.cam, userProperties.campaign);
        identify.set(AtributionsIds.adg, userProperties.adgroup);
        identify.set(AtributionsIds.cre, userProperties.creative);
        identify.set(AtributionsIds.cl, userProperties.clickLabel);
        identify.set(AtributionsIds.adid, userProperties.adid);
        Amplitude.getInstance().identify(identify);
        Amplitude.getInstance().logEvent("set_attr");
    }
}
