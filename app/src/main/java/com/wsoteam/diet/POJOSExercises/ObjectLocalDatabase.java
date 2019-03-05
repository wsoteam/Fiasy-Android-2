package com.wsoteam.diet.POJOSExercises;

import com.orm.SugarRecord;

import java.util.ArrayList;

public class ObjectLocalDatabase extends SugarRecord {
    private int numberOfSelectedProgramm;
    private int numberOfSelectedItemOfList;

    public ObjectLocalDatabase() {
    }

    public ObjectLocalDatabase(int numberOfSelectedProgramm, int numberOfSelectedItemOfList) {
        this.numberOfSelectedProgramm = numberOfSelectedProgramm;
        this.numberOfSelectedItemOfList = numberOfSelectedItemOfList;
    }

    public int getNumberOfSelectedProgramm() {
        return numberOfSelectedProgramm;
    }

    public void setNumberOfSelectedProgramm(int numberOfSelectedProgramm) {
        this.numberOfSelectedProgramm = numberOfSelectedProgramm;
    }

    public int getNumberOfSelectedItemOfList() {
        return numberOfSelectedItemOfList;
    }

    public void setNumberOfSelectedItemOfList(int numberOfSelectedItemOfList) {
        this.numberOfSelectedItemOfList = numberOfSelectedItemOfList;
    }

    public static boolean isAddedInBase(int numberOfSelectedProgramm, int numberOfSelectedItemOfList) {
        boolean isAddedInBase = false;
        ArrayList<ObjectLocalDatabase> objectLocalDatabase =
                (ArrayList<ObjectLocalDatabase>) ObjectLocalDatabase.listAll(ObjectLocalDatabase.class);
        for (int i = 0; i < objectLocalDatabase.size(); i++) {
            if (numberOfSelectedProgramm == objectLocalDatabase.get(i).getNumberOfSelectedProgramm()
                    && numberOfSelectedItemOfList == objectLocalDatabase.get(i).numberOfSelectedItemOfList){
                isAddedInBase = true;
            }
        }
        return isAddedInBase;

    }
}
