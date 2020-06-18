package com.losing.weight.POJOProfile;

import java.io.Serializable;

public class FavoriteFood implements Serializable {
    private long id;
    private String fullInfo;
    private String key;

    private String name;
    private String brand;
    private double portion;

    private boolean isLiquid;

    private double kilojoules;
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

    private int percentCarbohydrates;
    private int percentFats;
    private int percentProteins;

    public FavoriteFood() {
    }

    public FavoriteFood(long id, String fullInfo, String key) {
        this.id = id;
        this.fullInfo = fullInfo;
        this.key = key;
    }

    public FavoriteFood(long id, String fullInfo, String key, String name, String brand, double portion, boolean isLiquid, double kilojoules, double calories, double proteins, double carbohydrates, double sugar, double fats, double saturatedFats, double monoUnSaturatedFats, double polyUnSaturatedFats, double cholesterol, double cellulose, double sodium, double pottassium, int percentCarbohydrates, int percentFats, int percentProteins) {
        this.id = id;
        this.fullInfo = fullInfo;
        this.key = key;
        this.name = name;
        this.brand = brand;
        this.portion = portion;
        this.isLiquid = isLiquid;
        this.kilojoules = kilojoules;
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
        this.percentCarbohydrates = percentCarbohydrates;
        this.percentFats = percentFats;
        this.percentProteins = percentProteins;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }

    public boolean isLiquid() {
        return isLiquid;
    }

    public void setLiquid(boolean liquid) {
        isLiquid = liquid;
    }

    public double getKilojoules() {
        return kilojoules;
    }

    public void setKilojoules(double kilojoules) {
        this.kilojoules = kilojoules;
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

    public int getPercentCarbohydrates() {
        return percentCarbohydrates;
    }

    public void setPercentCarbohydrates(int percentCarbohydrates) {
        this.percentCarbohydrates = percentCarbohydrates;
    }

    public int getPercentFats() {
        return percentFats;
    }

    public void setPercentFats(int percentFats) {
        this.percentFats = percentFats;
    }

    public int getPercentProteins() {
        return percentProteins;
    }

    public void setPercentProteins(int percentProteins) {
        this.percentProteins = percentProteins;
    }

    @Override
    public String toString() {
        return "FavoriteFood{" +
                "id=" + id +
                ", fullInfo='" + fullInfo + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", portion=" + portion +
                ", isLiquid=" + isLiquid +
                ", kilojoules=" + kilojoules +
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
                ", percentCarbohydrates=" + percentCarbohydrates +
                ", percentFats=" + percentFats +
                ", percentProteins=" + percentProteins +
                '}';
    }
}
