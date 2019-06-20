package com.wsoteam.diet.Recipes.adding;

public class MessageEvent {
    public final String message;
    public final int index;

    public MessageEvent(String message, int index) {
        this.message = message;
        this.index = index;
    }
}
