package com.losing.weight.model;

import java.util.Locale;

public class Article {

  private int id;
  private String title;
  private String title_ru;
  private String title_en;
  private String title_de;
  private String title_pt;
  private String title_es;
  private String body;
  private String body_ru;
  private String body_en;
  private String body_de;
  private String body_pt;
  private String body_es;
  private String image;
  private String date;
  private Author author;
  private boolean premium;
  private int day_in_series;
  private String title_color;
  private ArticleSeries series;
  private ArticleCategory category;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getTitle(Locale locale){
    switch (locale.getLanguage()){
      case "ru": return getTitle_ru();
      case "pt": return getTitle_pt();
      case "es": return getTitle_es();
      case "de": return getTitle_de();
      default: return getTitle_en();
    }
  }

  public String getTitle_color() {
    return title_color;
  }

  public void setTitle_color(String title_color) {
    this.title_color = title_color;
  }

  public int getDay_in_series() {
    return day_in_series;
  }

  public void setDay_in_series(int day_in_series) {
    this.day_in_series = day_in_series;
  }

  public boolean isPremium() {
    return premium;
  }

  public void setPremium(boolean premium) {
    this.premium = premium;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle_ru() {
    return title_ru;
  }

  public void setTitle_ru(String title_ru) {
    this.title_ru = title_ru;
  }

  public String getTitle_en() {
    return title_en;
  }

  public void setTitle_en(String title_en) {
    this.title_en = title_en;
  }

  public String getTitle_de() {
    return title_de;
  }

  public void setTitle_de(String title_de) {
    this.title_de = title_de;
  }

  public String getTitle_pt() {
    return title_pt;
  }

  public void setTitle_pt(String title_pt) {
    this.title_pt = title_pt;
  }

  public String getTitle_es() {
    return title_es;
  }

  public void setTitle_es(String title_es) {
    this.title_es = title_es;
  }

  public String getBody() {
    return body;
  }

  public String getBody(Locale locale){
    switch (locale.getLanguage()){
      case "ru": return getBody_ru();
      case "pt": return getBody_pt();
      case "es": return getBody_es();
      case "de": return getBody_de();
      default: return getBody_en();
    }
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getBody_ru() {
    return body_ru;
  }

  public void setBody_ru(String body_ru) {
    this.body_ru = body_ru;
  }

  public String getBody_en() {
    return body_en;
  }

  public void setBody_en(String body_en) {
    this.body_en = body_en;
  }

  public String getBody_de() {
    return body_de;
  }

  public void setBody_de(String body_de) {
    this.body_de = body_de;
  }

  public String getBody_pt() {
    return body_pt;
  }

  public void setBody_pt(String body_pt) {
    this.body_pt = body_pt;
  }

  public String getBody_es() {
    return body_es;
  }

  public void setBody_es(String body_es) {
    this.body_es = body_es;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public ArticleSeries getSeries() {
    return series;
  }

  public void setSeries(ArticleSeries series) {
    this.series = series;
  }

  public ArticleCategory getCategory() {
    return category;
  }

  public void setCategory(ArticleCategory category) {
    this.category = category;
  }
}
