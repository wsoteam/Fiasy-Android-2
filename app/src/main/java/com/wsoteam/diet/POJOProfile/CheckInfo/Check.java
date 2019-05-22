package com.wsoteam.diet.POJOProfile.CheckInfo;

import java.io.Serializable;

public class Check implements Serializable {
    private long expiryTimeMillis;
    private long priceAmountMicros;
    private String priceCurrencyCode;


    public Check() {
    }

    public Check(long expiryTimeMillis, long priceAmountMicros, String priceCurrencyCode) {
        this.expiryTimeMillis = expiryTimeMillis;
        this.priceAmountMicros = priceAmountMicros;
        this.priceCurrencyCode = priceCurrencyCode;
    }

    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public void setExpiryTimeMillis(long expiryTimeMillis) {
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public long getPriceAmountMicros() {
        return priceAmountMicros;
    }

    public void setPriceAmountMicros(long priceAmountMicros) {
        this.priceAmountMicros = priceAmountMicros;
    }

    public String getPriceCurrencyCode() {
        return priceCurrencyCode;
    }

    public void setPriceCurrencyCode(String priceCurrencyCode) {
        this.priceCurrencyCode = priceCurrencyCode;
    }
}
