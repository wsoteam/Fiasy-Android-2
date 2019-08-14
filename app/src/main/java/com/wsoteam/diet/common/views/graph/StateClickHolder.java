package com.wsoteam.diet.common.views.graph;

public class StateClickHolder {
    private static boolean isClickBar = false;

    public static boolean isIsClickBar() {
        return isClickBar;
    }

    public static void setIsClickBar(boolean isClickBar) {
        StateClickHolder.isClickBar = isClickBar;
    }
}
