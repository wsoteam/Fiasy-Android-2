package com.wsoteam.diet.BranchOfExercises;


import com.wsoteam.diet.POJOSExercises.GlobalObject;

public class ObjectHolder {
    private static GlobalObject globalObject;


    public void createObjFromGirebase(GlobalObject globalObject){
        if(this.globalObject == null){
            this.globalObject = globalObject;
        }
    }

    public static GlobalObject getGlobalObject(){
        return globalObject;
    }

}

