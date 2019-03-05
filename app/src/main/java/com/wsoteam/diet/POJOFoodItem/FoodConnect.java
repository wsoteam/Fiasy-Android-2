
package com.wsoteam.diet.POJOFoodItem;

import com.squareup.moshi.Json;

public class FoodConnect {

    @Json(name = "dbAnalyzer")
    private DbAnalyzer dbAnalyzer;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FoodConnect() {
    }

    /**
     * 
     * @param dbAnalyzer
     */
    public FoodConnect(DbAnalyzer dbAnalyzer) {
        super();
        this.dbAnalyzer = dbAnalyzer;
    }

    public DbAnalyzer getDbAnalyzer() {
        return dbAnalyzer;
    }

    public void setDbAnalyzer(DbAnalyzer dbAnalyzer) {
        this.dbAnalyzer = dbAnalyzer;
    }

}
