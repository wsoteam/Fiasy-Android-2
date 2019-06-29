package com.wsoteam.diet.BranchOfAnalyzer.CustomFood;

import java.io.Serializable;

public class CustomFood implements Serializable {
    private String name;
    private String brand;
    private double calories;
    private double proteins;
    private double carbohydrates;
    private double sugar;
    private double fats;
    private double saturatedFats;
    private double monoUnSaturatedFats;
    private double polyUnSaturatedFats;
    private double cholesterol;
    private double cellulose;
    private double sodium;
    private double pottassium;
    private String barcode;

    public CustomFood() {
    }

    public CustomFood(String name, String brand, double calories, double proteins, double carbohydrates, double sugar, double fats, double saturatedFats, double monoUnSaturatedFats, double polyUnSaturatedFats, double cholesterol, double cellulose, double sodium, double pottassium, String barcode) {
        this.name = name;
        this.brand = brand;
        this.calories = calories;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.fats = fats;
        this.saturatedFats = saturatedFats;
        this.monoUnSaturatedFats = monoUnSaturatedFats;
        this.polyUnSaturatedFats = polyUnSaturatedFats;
        this.cholesterol = cholesterol;
        this.cellulose = cellulose;
        this.sodium = sodium;
        this.pottassium = pottassium;
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getSaturatedFats() {
        return saturatedFats;
    }

    public void setSaturatedFats(double saturatedFats) {
        this.saturatedFats = saturatedFats;
    }

    public double getMonoUnSaturatedFats() {
        return monoUnSaturatedFats;
    }

    public void setMonoUnSaturatedFats(double monoUnSaturatedFats) {
        this.monoUnSaturatedFats = monoUnSaturatedFats;
    }

    public double getPolyUnSaturatedFats() {
        return polyUnSaturatedFats;
    }

    public void setPolyUnSaturatedFats(double polyUnSaturatedFats) {
        this.polyUnSaturatedFats = polyUnSaturatedFats;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getCellulose() {
        return cellulose;
    }

    public void setCellulose(double cellulose) {
        this.cellulose = cellulose;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getPottassium() {
        return pottassium;
    }

    public void setPottassium(double pottassium) {
        this.pottassium = pottassium;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "CustomFood{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", calories=" + calories +
                ", proteins=" + proteins +
                ", carbohydrates=" + carbohydrates +
                ", sugar=" + sugar +
                ", fats=" + fats +
                ", saturatedFats=" + saturatedFats +
                ", monoUnSaturatedFats=" + monoUnSaturatedFats +
                ", polyUnSaturatedFats=" + polyUnSaturatedFats +
                ", cholesterol=" + cholesterol +
                ", cellulose=" + cellulose +
                ", sodium=" + sodium +
                ", pottassium=" + pottassium +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}
