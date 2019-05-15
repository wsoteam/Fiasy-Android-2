package com.wsoteam.diet.POJOProfile;

import java.io.Serializable;

public class SubInfo implements Serializable {
    private String orderId;
    private String packageName;
    private String productId;
    private long purchaseTime;
    private boolean autoRenewing;
    private String purchaseToken;

    public SubInfo(String orderId, String packageName, String productId, long purchaseTime, boolean autoRenewing, String purchaseToken) {
        this.orderId = orderId;
        this.packageName = packageName;
        this.productId = productId;
        this.purchaseTime = purchaseTime;
        this.autoRenewing = autoRenewing;
        this.purchaseToken = purchaseToken;
    }

    public SubInfo() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public boolean isAutoRenewing() {
        return autoRenewing;
    }

    public void setAutoRenewing(boolean autoRenewing) {
        this.autoRenewing = autoRenewing;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }
}
