package com.wsoteam.diet.presentation.search.basket.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wsoteam.diet.common.networking.food.POJO.Result;

@Entity
public class BasketEntity {
  @PrimaryKey(autoGenerate = true)
  private long id;
  private int serverId;
  private int deepId;
  private String name;
  private String brand;
  private boolean isLiquid;
  private int weight;

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

  private boolean isCustomPortion;
  //if custom portion, else - -1
  private int customPortion;
  //Dinner - 0, Lunch - 1, Dinner - 2, Snack - 3
  private int eatingType;

  public BasketEntity() {
  }

  public BasketEntity(Result result, int weight, int customPortion, int eatingType, int deepId) {
    name = result.getName();
    if (result.getBrand() != null) {
      brand = result.getBrand().getName();
    }
    this.weight = weight;
    isLiquid = result.isLiquid();
    kilojoules = result.getKilojoules() * weight;
    calories = result.getCalories() * weight;
    proteins = result.getProteins() * weight;
    carbohydrates = result.getCarbohydrates() + weight;
    sugar = result.getSugar() * weight;
    fats = result.getFats() * weight;
    saturatedFats = result.getSaturatedFats() * weight;
    monoUnSaturatedFats = result.getMonounsaturatedFats() * weight;
    polyUnSaturatedFats = result.getPolyunsaturatedFats() * weight;
    cholesterol = result.getCholesterol() * weight;
    cellulose = result.getCellulose() * weight;
    sodium = result.getSodium() * weight;
    pottassium = result.getPottasium() * weight;
    isCustomPortion = true;
    this.customPortion = customPortion;
    this.eatingType = eatingType;
    serverId = result.getId();
    this.deepId = deepId;
  }

  public BasketEntity(Result result, int weight, int eatingType, int deepId) {
    name = result.getName();
    if (result.getBrand() != null) {
      brand = result.getBrand().getName();
    }
    this.weight = weight;
    isLiquid = result.isLiquid();
    kilojoules = result.getKilojoules() * weight;
    calories = result.getCalories() * weight;
    proteins = result.getProteins() * weight;
    carbohydrates = result.getCarbohydrates() + weight;
    sugar = result.getSugar() * weight;
    fats = result.getFats() * weight;
    saturatedFats = result.getSaturatedFats() * weight;
    monoUnSaturatedFats = result.getMonounsaturatedFats() * weight;
    polyUnSaturatedFats = result.getPolyunsaturatedFats() * weight;
    cholesterol = result.getCholesterol() * weight;
    cellulose = result.getCellulose() * weight;
    sodium = result.getSodium() * weight;
    pottassium = result.getPottasium() * weight;
    isCustomPortion = false;
    customPortion = -1;
    this.eatingType = eatingType;
    serverId = result.getId();
    this.deepId = deepId;
  }

  public int getServerId() {
    return serverId;
  }

  public void setServerId(int serverId) {
    this.serverId = serverId;
  }

  public int getDeepId() {
    return deepId;
  }

  public void setDeepId(int deepId) {
    this.deepId = deepId;
  }

  public long getId() {
    return id;
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

  public boolean isLiquid() {
    return isLiquid;
  }

  public void setLiquid(boolean liquid) {
    isLiquid = liquid;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
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

  public boolean isCustomPortion() {
    return isCustomPortion;
  }

  public void setCustomPortion(boolean customPortion) {
    isCustomPortion = customPortion;
  }

  public int getCustomPortion() {
    return customPortion;
  }

  public void setCustomPortion(int customPortion) {
    this.customPortion = customPortion;
  }

  public int getEatingType() {
    return eatingType;
  }

  public void setEatingType(int eatingType) {
    this.eatingType = eatingType;
  }
}
