package com.losing.weight.DietPlans.POJO;

import com.losing.weight.Recipes.POJO.plan.RecipeForDay;
import com.losing.weight.presentation.plans.DateHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DietPlan implements Serializable {
  private String name;
  private int countDays;
  private String text;
  private String urlImage;
  private String flag;
  private boolean premium;
  private String startDate;

  private int recipeCount;
  private List<RecipeForDay> recipeForDays;

  public DietPlan() {
  }

  public DietPlan(String name, int countDays, String text, String urlImage, String flag,
      boolean premium, String startDate) {
    this.name = name;
    this.countDays = countDays;
    this.text = text;
    this.urlImage = urlImage;
    this.flag = flag;
    this.premium = premium;
    this.startDate = startDate;
  }

  public void setRecipes(List<RecipeForDay> recipeForDays, int recipeCount) {
    this.recipeForDays = recipeForDays;
    this.recipeCount = recipeCount;
  }

  public int getDaysAfterStart(){
    int daysAfterStart;
    Date currentDate = new Date();
    Date startDate = DateHelper.stringToDate(getStartDate());
    if (startDate != null){
      long milliseconds = currentDate.getTime() - startDate.getTime();
      // 24 часа = 1 440 минут = 1 день
      daysAfterStart = ((int) (milliseconds / (24 * 60 * 60 * 1000)));
      //Log.d("kkk", "" + milliseconds +"\nДней: " + daysAfterStart);
    } else return -1;

    return daysAfterStart;
  }

  public int getRecipeCount() {
    return recipeCount;
  }

  public List<RecipeForDay> getRecipeForDays() {
    return recipeForDays;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCountDays() {
    return countDays;
  }

  public void setCountDays(int countDays) {
    this.countDays = countDays;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getUrlImage() {
    return urlImage;
  }

  public void setUrlImage(String urlImage) {
    this.urlImage = urlImage;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public boolean isPremium() {
    return premium;
  }

  public void setPremium(boolean premium) {
    this.premium = premium;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
}
