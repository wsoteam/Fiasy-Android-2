package com.wsoteam.diet.InApp.properties;

import android.os.AsyncTask;
import android.util.Log;

import com.android.billingclient.api.Purchase;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Amplitude.AmplitudeUserProperties;
import com.wsoteam.diet.InApp.IDs;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.Analytics.UserProperty;

import io.intercom.android.sdk.Intercom;


public class CheckAndSetPurchase extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... strings) {
        String productId = strings[0];
        String token = strings[1];
        String packageName = strings[2];
        PlayService playServices = new PlayService();
        try {
            WorkWithFirebaseDB.setSubInfo(getAllSubInfo(packageName, productId, token,
                    playServices.getSubscription(packageName, productId, token)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubInfo getAllSubInfo(String packageName, String productId, String token, SubscriptionPurchase subscription) {
        SubInfo subInfo = new SubInfo();
        if (subscription.getPaymentState() == null) {
            UserProperty.setPremStatus(UserProperty.not_buy);
            return getEmptySubInfo();
        } else {
            subInfo.setOrderId(subscription.getOrderId());
            subInfo.setPackageName(packageName);
            subInfo.setProductId(productId);
            subInfo.setPurchaseTime(subscription.getStartTimeMillis());
            subInfo.setAutoRenewing(subscription.getAutoRenewing());
            subInfo.setPurchaseToken(token);
            subInfo.setExpiryTimeMillis(subscription.getExpiryTimeMillis());
            subInfo.setPaymentState(subscription.getPaymentState());
            subInfo.setCurrency(subscription.getPriceCurrencyCode());
            subInfo.setPrice(subscription.getPriceAmountMicros());
        }
        return subInfo;
    }


    private SubInfo getEmptySubInfo() {
        SubInfo subInfo = new SubInfo();
        subInfo.setOrderId(IDs.EMPTY_SUB);
        subInfo.setPackageName(IDs.EMPTY_SUB);
        subInfo.setProductId(IDs.EMPTY_SUB);
        subInfo.setPurchaseTime(IDs.EMPTY_SUB_TIME);
        subInfo.setAutoRenewing(false);
        subInfo.setPurchaseToken(IDs.EMPTY_SUB);
        return subInfo;
    }
}
