package com.wsoteam.diet.common.networking.food;

public class HeaderObj implements ISearchResult {
  private String title;

  public HeaderObj(String title) {
    this.title = title;
  }

  public HeaderObj() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
