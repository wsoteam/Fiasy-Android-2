package com.wsoteam.diet.model;

import java.io.Serializable;

public class Snack extends Eating implements Serializable {
    public Snack() {
    }

    public Snack(String name, String urlOfImages, int calories, int carbohydrates, int protein, int fat, int weight, int day, int month, int year) {
        super(name, urlOfImages, calories, carbohydrates, protein, fat, weight, day, month, year);
    }
}
