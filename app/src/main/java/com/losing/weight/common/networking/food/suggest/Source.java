
package com.losing.weight.common.networking.food.suggest;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_en")
    @Expose
    private Object nameEn;
    @SerializedName("name_de")
    @Expose
    private Object nameDe;
    @SerializedName("name_pt")
    @Expose
    private Object namePt;
    @SerializedName("name_es")
    @Expose
    private Object nameEs;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("full_info")
    @Expose
    private String fullInfo;
    @SerializedName("full_info_en")
    @Expose
    private Object fullInfoEn;
    @SerializedName("full_info_de")
    @Expose
    private Object fullInfoDe;
    @SerializedName("full_info_pt")
    @Expose
    private Object fullInfoPt;
    @SerializedName("full_info_es")
    @Expose
    private Object fullInfoEs;
    @SerializedName("measurement_units")
    @Expose
    private List<MeasurementUnit> measurementUnits = null;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("portion")
    @Expose
    private double portion;
    @SerializedName("is_liquid")
    @Expose
    private boolean isLiquid;
    @SerializedName("kilojoules")
    @Expose
    private double kilojoules;
    @SerializedName("calories")
    @Expose
    private double calories;
    @SerializedName("proteins")
    @Expose
    private double proteins;
    @SerializedName("carbohydrates")
    @Expose
    private double carbohydrates;
    @SerializedName("sugar")
    @Expose
    private double sugar;
    @SerializedName("fats")
    @Expose
    private double fats;
    @SerializedName("saturated_fats")
    @Expose
    private double saturatedFats;
    @SerializedName("monounsaturated_fats")
    @Expose
    private double monounsaturatedFats;
    @SerializedName("polyunsaturated_fats")
    @Expose
    private double polyunsaturatedFats;
    @SerializedName("cholesterol")
    @Expose
    private double cholesterol;
    @SerializedName("cellulose")
    @Expose
    private double cellulose;
    @SerializedName("sodium")
    @Expose
    private double sodium;
    @SerializedName("pottasium")
    @Expose
    private double pottasium;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Source withName(String name) {
        this.name = name;
        return this;
    }

    public Object getNameEn() {
        return nameEn;
    }

    public void setNameEn(Object nameEn) {
        this.nameEn = nameEn;
    }

    public Source withNameEn(Object nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public Object getNameDe() {
        return nameDe;
    }

    public void setNameDe(Object nameDe) {
        this.nameDe = nameDe;
    }

    public Source withNameDe(Object nameDe) {
        this.nameDe = nameDe;
        return this;
    }

    public Object getNamePt() {
        return namePt;
    }

    public void setNamePt(Object namePt) {
        this.namePt = namePt;
    }

    public Source withNamePt(Object namePt) {
        this.namePt = namePt;
        return this;
    }

    public Object getNameEs() {
        return nameEs;
    }

    public void setNameEs(Object nameEs) {
        this.nameEs = nameEs;
    }

    public Source withNameEs(Object nameEs) {
        this.nameEs = nameEs;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Source withCategory(Category category) {
        this.category = category;
        return this;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Source withBrand(Brand brand) {
        this.brand = brand;
        return this;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
    }

    public Source withFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
        return this;
    }

    public Object getFullInfoEn() {
        return fullInfoEn;
    }

    public void setFullInfoEn(Object fullInfoEn) {
        this.fullInfoEn = fullInfoEn;
    }

    public Source withFullInfoEn(Object fullInfoEn) {
        this.fullInfoEn = fullInfoEn;
        return this;
    }

    public Object getFullInfoDe() {
        return fullInfoDe;
    }

    public void setFullInfoDe(Object fullInfoDe) {
        this.fullInfoDe = fullInfoDe;
    }

    public Source withFullInfoDe(Object fullInfoDe) {
        this.fullInfoDe = fullInfoDe;
        return this;
    }

    public Object getFullInfoPt() {
        return fullInfoPt;
    }

    public void setFullInfoPt(Object fullInfoPt) {
        this.fullInfoPt = fullInfoPt;
    }

    public Source withFullInfoPt(Object fullInfoPt) {
        this.fullInfoPt = fullInfoPt;
        return this;
    }

    public Object getFullInfoEs() {
        return fullInfoEs;
    }

    public void setFullInfoEs(Object fullInfoEs) {
        this.fullInfoEs = fullInfoEs;
    }

    public Source withFullInfoEs(Object fullInfoEs) {
        this.fullInfoEs = fullInfoEs;
        return this;
    }

    public List<MeasurementUnit> getMeasurementUnits() {
        return measurementUnits;
    }

    public void setMeasurementUnits(List<MeasurementUnit> measurementUnits) {
        this.measurementUnits = measurementUnits;
    }

    public Source withMeasurementUnits(List<MeasurementUnit> measurementUnits) {
        this.measurementUnits = measurementUnits;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Source withId(int id) {
        this.id = id;
        return this;
    }

    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }

    public Source withPortion(double portion) {
        this.portion = portion;
        return this;
    }

    public boolean isIsLiquid() {
        return isLiquid;
    }

    public void setIsLiquid(boolean isLiquid) {
        this.isLiquid = isLiquid;
    }

    public Source withIsLiquid(boolean isLiquid) {
        this.isLiquid = isLiquid;
        return this;
    }

    public double getKilojoules() {
        return kilojoules;
    }

    public void setKilojoules(double kilojoules) {
        this.kilojoules = kilojoules;
    }

    public Source withKilojoules(double kilojoules) {
        this.kilojoules = kilojoules;
        return this;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Source withCalories(double calories) {
        this.calories = calories;
        return this;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public Source withProteins(double proteins) {
        this.proteins = proteins;
        return this;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Source withCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
        return this;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public Source withSugar(double sugar) {
        this.sugar = sugar;
        return this;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public Source withFats(double fats) {
        this.fats = fats;
        return this;
    }

    public double getSaturatedFats() {
        return saturatedFats;
    }

    public void setSaturatedFats(double saturatedFats) {
        this.saturatedFats = saturatedFats;
    }

    public Source withSaturatedFats(double saturatedFats) {
        this.saturatedFats = saturatedFats;
        return this;
    }

    public double getMonounsaturatedFats() {
        return monounsaturatedFats;
    }

    public void setMonounsaturatedFats(double monounsaturatedFats) {
        this.monounsaturatedFats = monounsaturatedFats;
    }

    public Source withMonounsaturatedFats(double monounsaturatedFats) {
        this.monounsaturatedFats = monounsaturatedFats;
        return this;
    }

    public double getPolyunsaturatedFats() {
        return polyunsaturatedFats;
    }

    public void setPolyunsaturatedFats(double polyunsaturatedFats) {
        this.polyunsaturatedFats = polyunsaturatedFats;
    }

    public Source withPolyunsaturatedFats(double polyunsaturatedFats) {
        this.polyunsaturatedFats = polyunsaturatedFats;
        return this;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Source withCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
        return this;
    }

    public double getCellulose() {
        return cellulose;
    }

    public void setCellulose(double cellulose) {
        this.cellulose = cellulose;
    }

    public Source withCellulose(double cellulose) {
        this.cellulose = cellulose;
        return this;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public Source withSodium(double sodium) {
        this.sodium = sodium;
        return this;
    }

    public double getPottasium() {
        return pottasium;
    }

    public void setPottasium(double pottasium) {
        this.pottasium = pottasium;
    }

    public Source withPottasium(double pottasium) {
        this.pottasium = pottasium;
        return this;
    }

}
