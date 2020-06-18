package com.losing.weight.InApp.properties;

public class SingletonMakePurchase {
    private static SingletonMakePurchase instance;
    private boolean isMakePurchaseNow = false;

    private SingletonMakePurchase() {
    }

    public static SingletonMakePurchase getInstance() {
        if (instance == null) {
            instance = new SingletonMakePurchase();
        }
        return instance;
    }

    public boolean isMakePurchaseNow() {
        return isMakePurchaseNow;
    }

    public void setMakePurchaseNow(boolean makePurchaseNow) {
        isMakePurchaseNow = makePurchaseNow;
    }
}
