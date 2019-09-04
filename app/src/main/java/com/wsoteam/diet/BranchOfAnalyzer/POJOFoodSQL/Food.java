package com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Food implements Serializable {
    @PrimaryKey private long id;
    private String name;
    private String brand;
    private String fullInfo;
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

    public Food() {
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

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", fullInfo='" + fullInfo + '\'' +
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
