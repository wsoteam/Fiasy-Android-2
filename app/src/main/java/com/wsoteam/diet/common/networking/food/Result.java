
package com.wsoteam.diet.common.networking.food;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

    private Integer id;
    private String name;
    private String fullInfo;
    private Object barcode;
    private Integer portion;
    private Boolean isLiquid;
    private Double kilojoules;
    private Double calories;
    private Double proteins;
    private Double carbohydrates;
    private Double sugar;
    private Double fats;
    private Double saturatedFats;
    private Double monounsaturatedFats;
    private Double polyunsaturatedFats;
    private Double cholesterol;
    private Integer cellulose;
    private Double sodium;
    private Double pottasium;
    private Object category;
    private Object brand;
    private List<Object> measurementUnits = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
    }

    public Object getBarcode() {
        return barcode;
    }

    public void setBarcode(Object barcode) {
        this.barcode = barcode;
    }

    public Integer getPortion() {
        return portion;
    }

    public void setPortion(Integer portion) {
        this.portion = portion;
    }

    public Boolean getIsLiquid() {
        return isLiquid;
    }

    public void setIsLiquid(Boolean isLiquid) {
        this.isLiquid = isLiquid;
    }

    public Double getKilojoules() {
        return kilojoules;
    }

    public void setKilojoules(Double kilojoules) {
        this.kilojoules = kilojoules;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getSaturatedFats() {
        return saturatedFats;
    }

    public void setSaturatedFats(Double saturatedFats) {
        this.saturatedFats = saturatedFats;
    }

    public Double getMonounsaturatedFats() {
        return monounsaturatedFats;
    }

    public void setMonounsaturatedFats(Double monounsaturatedFats) {
        this.monounsaturatedFats = monounsaturatedFats;
    }

    public Double getPolyunsaturatedFats() {
        return polyunsaturatedFats;
    }

    public void setPolyunsaturatedFats(Double polyunsaturatedFats) {
        this.polyunsaturatedFats = polyunsaturatedFats;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Integer getCellulose() {
        return cellulose;
    }

    public void setCellulose(Integer cellulose) {
        this.cellulose = cellulose;
    }

    public Double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public Double getPottasium() {
        return pottasium;
    }

    public void setPottasium(Double pottasium) {
        this.pottasium = pottasium;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getBrand() {
        return brand;
    }

    public void setBrand(Object brand) {
        this.brand = brand;
    }

    public List<Object> getMeasurementUnits() {
        return measurementUnits;
    }

    public void setMeasurementUnits(List<Object> measurementUnits) {
        this.measurementUnits = measurementUnits;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
