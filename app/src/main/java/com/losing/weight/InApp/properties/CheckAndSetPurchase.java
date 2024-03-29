package com.losing.weight.InApp.properties;

import android.content.Context;
import android.os.AsyncTask;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.losing.weight.InApp.IDs;
import com.losing.weight.POJOProfile.SubInfo;
import com.losing.weight.Sync.WorkWithFirebaseDB;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.common.Analytics.UserProperty;

import java.lang.ref.WeakReference;


public class CheckAndSetPurchase extends AsyncTask<String, Void, Void> {
    private boolean isNeedTrackPurchase = false;
    private final int PAYMENT_RECIEVED = 1;
    private final int LENGHT_WHEN_NEED_TRACK_BUY = 3;

    private WeakReference<Context> contextRef;

    public CheckAndSetPurchase(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String productId = strings[0];
        String token = strings[1];
        String packageName = strings[2];
        if (strings.length > LENGHT_WHEN_NEED_TRACK_BUY) {
            isNeedTrackPurchase = true;
        }
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
            if (isNeedTrackPurchase && subscription.getPaymentState() == PAYMENT_RECIEVED){
                logPayment();
                Events.logPurchaseSuccess();
                try{
                    Events.logTrackRevenue(subscription.getPriceAmountMicros());
                }catch (Exception ex){

                }
            }
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

    private void logPayment() {
        Context context = contextRef.get();
        if (context != null){
            AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(context);
            appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED);
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
