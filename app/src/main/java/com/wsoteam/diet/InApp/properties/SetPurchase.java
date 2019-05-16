package com.wsoteam.diet.InApp.properties;

import android.os.AsyncTask;
import android.util.Log;

import com.android.billingclient.api.Purchase;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;


public class SetPurchase extends AsyncTask<Purchase, Void, Void> {
    @Override
    protected Void doInBackground(Purchase... purchases) {
        String productId = purchases[0].getSku();
        String token = purchases[0].getPurchaseToken();
        String packageName = purchases[0].getPackageName();
        PlayService playServices = new PlayService();
        try {
            Log.e("LOL", playServices.getSubscription(packageName, productId, token).toString());
            WorkWithFirebaseDB.setSubInfo(getAllSubInfo(purchases[0], playServices.getSubscription(packageName, productId, token)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubInfo getAllSubInfo(Purchase purchase, SubscriptionPurchase subscription) {
        SubInfo subInfo = new SubInfo();
        subInfo.setOrderId(subscription.getOrderId());
        subInfo.setPackageName(purchase.getPackageName());
        subInfo.setProductId(purchase.getSku());
        subInfo.setPurchaseTime(subscription.getStartTimeMillis());
        subInfo.setAutoRenewing(subscription.getAutoRenewing());
        subInfo.setPurchaseToken(purchase.getPurchaseToken());
        subInfo.setExpiryTimeMillis(subscription.getExpiryTimeMillis());
        subInfo.setPaymentState(subscription.getPaymentState());
        return subInfo;
    }
}
