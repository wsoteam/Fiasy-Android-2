package com.losing.weight.revenue;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.losing.weight.InApp.IDs;
import com.losing.weight.InApp.properties.PlayService;
import com.losing.weight.POJOProfile.SubInfo;

public class GetPurchaseInfoAndSet extends AsyncTask<String, Void, Void> {
    private static final String TAG = "HARVESTER";

    @Override
    protected Void doInBackground(String... strings) {
        String productId = strings[0];
        String token = strings[1];
        String packageName = strings[2];
        String idOfProfile = strings[3];
        PlayService playServices = new PlayService();
        try {
            WorkWithDB.setSubInfo(getAllSubInfo(packageName, productId, token,
                    playServices.getSubscription(packageName, productId, token)), idOfProfile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, idOfProfile + " -- crash" + "-- " + e.toString());
        }
        return null;
    }

    private SubInfo getAllSubInfo(String packageName, String productId, String token, SubscriptionPurchase subscription) {
        if (subscription.getPaymentState() == null) {
            return getEmptySubInfo();
        } else {
            SubInfo subInfo = new SubInfo();
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
            return subInfo;
        }
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
