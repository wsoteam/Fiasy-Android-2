package com.wsoteam.diet.Amplitude;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.wsoteam.diet.AmplitudaEvents;

public class AmplitudeUserProperties {

    public static void setUserProperties(String key, String value){
        Identify identify = new Identify().set(key, value);
        Amplitude.getInstance().identify(identify);
    }
}
