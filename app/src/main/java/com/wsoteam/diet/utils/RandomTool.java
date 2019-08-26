package com.wsoteam.diet.utils;

import java.util.Random;

public class RandomTool {
  private static final Random random = new Random();

  public static int getAnythingRandomItDoesntMatter(int[] items) {
    final int id = random.nextInt(items.length);
    return items[id];
  }

  public static <T> T getAnythingRandomItDoesntMatter(T[] items) {
    final int id = random.nextInt(items.length);
    return items[id];
  }
}
