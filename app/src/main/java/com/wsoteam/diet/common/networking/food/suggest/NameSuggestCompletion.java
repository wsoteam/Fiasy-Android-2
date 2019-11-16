
package com.wsoteam.diet.common.networking.food.suggest;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NameSuggestCompletion {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("length")
    @Expose
    private int length;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NameSuggestCompletion withText(String text) {
        this.text = text;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public NameSuggestCompletion withOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public NameSuggestCompletion withLength(int length) {
        this.length = length;
        return this;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public NameSuggestCompletion withOptions(List<Option> options) {
        this.options = options;
        return this;
    }

}
