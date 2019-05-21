package com.wsoteam.diet.InApp.properties;

import com.wsoteam.diet.InApp.IDs;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

public class EmptySubInfo {
    public static void setEmptySubInfo() {
        SubInfo subInfo = new SubInfo();
        subInfo.setOrderId(IDs.EMPTY_SUB);
        subInfo.setPackageName(IDs.EMPTY_SUB);
        subInfo.setProductId(IDs.EMPTY_SUB);
        subInfo.setPurchaseTime(IDs.EMPTY_SUB_TIME);
        subInfo.setAutoRenewing(false);
        subInfo.setPurchaseToken(IDs.EMPTY_SUB);
        WorkWithFirebaseDB.setSubInfo(subInfo);
    }
}
