package com.wsoteam.diet.presentation.activity;

public class UserActivityExercise {
  private final CharSequence title;
  private final int burned;
  private final int duration;

  public UserActivityExercise(CharSequence title, int burned, int duration) {
    this.title = title;
    this.burned = burned;
    this.duration = duration;
  }

  public CharSequence title() {
    return title;
  }

  public int burned() {
    return burned;
  }

  public int duration() {
    return duration;
  }
}
