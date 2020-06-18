package com.losing.weight.model;

import java.io.Serializable;

public class OpenArticles implements Serializable{

  private String id;
  private long date;
  private int unlockedArticles;

  public OpenArticles() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public int getUnlockedArticles() {
    return unlockedArticles;
  }

  public void setUnlockedArticles(int unlockedArticles) {
    this.unlockedArticles = unlockedArticles;
  }
}
