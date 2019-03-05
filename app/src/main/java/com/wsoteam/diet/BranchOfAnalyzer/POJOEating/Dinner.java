package com.wsoteam.diet.BranchOfAnalyzer.POJOEating;

import com.orm.SugarRecord;

public class Dinner extends Eating {
    public Dinner() {
    }

    public Dinner(String name, String urlOfImages, int calories, int carbohydrates, int protein, int fat, int weight, int day, int month, int year) {
        super(name, urlOfImages, calories, carbohydrates, protein, fat, weight, day, month, year);
    }
}
