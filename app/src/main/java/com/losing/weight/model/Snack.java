package com.losing.weight.model;

import com.losing.weight.presentation.search.basket.db.BasketEntity;
import java.io.Serializable;

public class Snack extends Eating implements Serializable {
    public Snack() {
    }

    public Snack(BasketEntity basketEntity, int day,
        int month, int year, int type) {
        super(basketEntity, day, month, year, type);
    }

    public Snack(String name, String urlOfImages, int calories, int carbohydrates, int protein, int fat, int weight, int day, int month, int year) {
        super(name, urlOfImages, calories, carbohydrates, protein, fat, weight, day, month, year);
    }
}
