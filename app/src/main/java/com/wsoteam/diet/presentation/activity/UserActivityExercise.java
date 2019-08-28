package com.wsoteam.diet.presentation.activity;

public class UserActivityExercise {
  private final CharSequence title;
  private final long when;
  private final int burned;
  private final int duration;

  public UserActivityExercise(CharSequence title, int burned, int duration) {
    this(title, 0, burned, duration);
  }

  public UserActivityExercise(CharSequence title, long when, int burned, int duration) {
    this.title = title;
    this.when = when;
    this.burned = burned;
    this.duration = duration;
  }

  public long getWhen() {
    return when;
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
