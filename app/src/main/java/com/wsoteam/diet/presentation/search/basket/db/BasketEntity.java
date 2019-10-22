package com.wsoteam.diet.presentation.search.basket.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;

@Entity
public class BasketEntity implements ISearchResult {
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
  private int eatingType;

  public BasketEntity() {
  }

  public BasketEntity(int serverId, int deepId, String name, String brand,
      boolean isLiquid,
      int weight, double kilojoules, double calories, double proteins, double carbohydrates,
      double sugar, double fats, double saturatedFats, double monoUnSaturatedFats,
      double polyUnSaturatedFats, double cholesterol, double cellulose, double sodium,
      double pottassium, int eatingType) {
    this.serverId = serverId;
    this.deepId = deepId;
    this.name = name;
    this.brand = brand;
    this.isLiquid = isLiquid;
    this.weight = weight;
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
    this.eatingType = eatingType;
  }


  public BasketEntity(Result result, int weight, int eatingType, int deepId) {
    name = result.getName();
    if (result.getBrand() != null) {
      brand = result.getBrand().getName();
    }
    this.weight = weight;
    isLiquid = result.isLiquid();
    kilojoules = result.getKilojoules();
    calories = result.getCalories();
    proteins = result.getProteins();
    carbohydrates = result.getCarbohydrates();
    sugar = result.getSugar();
    fats = result.getFats();
    saturatedFats = result.getSaturatedFats();
    monoUnSaturatedFats = result.getMonounsaturatedFats();
    polyUnSaturatedFats = result.getPolyunsaturatedFats();
    cholesterol = result.getCholesterol();
    cellulose = result.getCellulose();
    sodium = result.getSodium();
    pottassium = result.getPottasium();
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

  public void setId(long id) {
    this.id = id;
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


  public int getEatingType() {
    return eatingType;
  }

  public void setEatingType(int eatingType) {
    this.eatingType = eatingType;
  }

  @Override public String toString() {
    return "BasketEntity{" +
        "id=" + id +
        ", serverId=" + serverId +
        ", deepId=" + deepId +
        ", name='" + name + '\'' +
        ", brand='" + brand + '\'' +
        ", isLiquid=" + isLiquid +
        ", weight=" + weight +
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
        ", eatingType=" + eatingType +
        '}';
  }
}
