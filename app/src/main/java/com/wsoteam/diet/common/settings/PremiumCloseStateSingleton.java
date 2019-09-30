package com.wsoteam.diet.common.settings;


public class PremiumCloseStateSingleton {
    private static PremiumCloseStateSingleton instance;
    private boolean isClosePremium = false;

    private PremiumCloseStateSingleton() {
    }

    public static PremiumCloseStateSingleton getInstance() {
        if (instance == null) {
            instance = new PremiumCloseStateSingleton();
        }
        return instance;
    }

    public boolean isClosePremium() {
        return isClosePremium;
    }

    public void setClosePremium(boolean closePremium) {
        isClosePremium = closePremium;
    }
}
