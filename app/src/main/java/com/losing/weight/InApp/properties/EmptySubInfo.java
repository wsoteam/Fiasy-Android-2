package com.losing.weight.InApp.properties;

import com.losing.weight.InApp.IDs;
import com.losing.weight.POJOProfile.SubInfo;
import com.losing.weight.Sync.WorkWithFirebaseDB;

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
