package com.wsoteam.diet.model;

import com.google.gson.annotations.SerializedName;

public class Author {

  private int id;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  private String bio;

  public Author() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }
}
