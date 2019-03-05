package com.wsoteam.diet;

import com.wsoteam.diet.POJOS.GlobalObject;
import com.wsoteam.diet.POJOS.POJO;

import java.util.ArrayList;

public class ObjectHolder {
    private static GlobalObject globalObject;

    public void bindObjectWithHolder(GlobalObject globalObject){
        this.globalObject = globalObject;
    }

    public static GlobalObject getGlobalObject(){
        return globalObject;
    }
}
