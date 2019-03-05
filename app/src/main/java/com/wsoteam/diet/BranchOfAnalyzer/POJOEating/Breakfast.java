package com.wsoteam.diet.BranchOfAnalyzer.POJOEating;

import com.orm.SugarRecord;

public class Breakfast extends Eating {
    public Breakfast() {
    }

    public Breakfast(String name, String urlOfImages, int calories, int carbohydrates, int protein, int fat, int weight, int day, int month, int year) {
        super(name, urlOfImages, calories, carbohydrates, protein, fat, weight, day, month, year);
    }
}
