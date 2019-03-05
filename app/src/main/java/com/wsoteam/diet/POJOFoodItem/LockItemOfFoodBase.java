package com.wsoteam.diet.POJOFoodItem;

import com.orm.SugarRecord;

public class LockItemOfFoodBase extends SugarRecord {
    private String nameOfUnLockGroup;

    public LockItemOfFoodBase() {
    }

    public LockItemOfFoodBase(String nameOfUnLockGroup) {
        this.nameOfUnLockGroup = nameOfUnLockGroup;
    }

    public String getNameOfUnLockGroup() {
        return nameOfUnLockGroup;
    }

    public void setNameOfUnLockGroup(String nameOfUnLockGroup) {
        this.nameOfUnLockGroup = nameOfUnLockGroup;
    }
}
