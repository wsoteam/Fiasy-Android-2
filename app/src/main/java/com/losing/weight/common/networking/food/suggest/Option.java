
package com.losing.weight.common.networking.food.suggest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("_index")
    @Expose
    private String index;
    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_score")
    @Expose
    private double score;
    @SerializedName("_source")
    @Expose
    private Source source;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Option withText(String text) {
        this.text = text;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Option withIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Option withType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Option withId(String id) {
        this.id = id;
        return this;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Option withScore(double score) {
        this.score = score;
        return this;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Option withSource(Source source) {
        this.source = source;
        return this;
    }

}
