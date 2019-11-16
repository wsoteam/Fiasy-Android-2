package com.wsoteam.diet.common.networking.food;

public class HeaderObj implements ISearchResult {
  //0 - breakfast, 1 - lunch ....
  private int type;
  private String title;
  private boolean isNeedIcon;

  public HeaderObj(String title, boolean isNeedIcon) {
    this.title = title;
    this.isNeedIcon = isNeedIcon;
  }

  public HeaderObj(int type, String title, boolean isNeedIcon) {
    this.type = type;
    this.title = title;
    this.isNeedIcon = isNeedIcon;
  }

  public HeaderObj() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isNeedIcon() {
    return isNeedIcon;
  }

  public void setNeedIcon(boolean needIcon) {
    isNeedIcon = needIcon;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}

