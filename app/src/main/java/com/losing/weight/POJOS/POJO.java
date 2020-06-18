package com.losing.weight.POJOS;

import java.io.Serializable;

public class POJO implements Serializable {
    private String name;
    private String bodyOfText;
    private String url_title;
    private String url_head;

    public POJO(String name, String bodyOfText, String url_title, String url_head) {
        this.name = name;
        this.bodyOfText = bodyOfText;
        this.url_title = url_title;
        this.url_head = url_head;
    }

    public POJO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodyOfText() {
        return bodyOfText;
    }

    public void setBodyOfText(String bodyOfText) {
        this.bodyOfText = bodyOfText;
    }

    public String getUrl_title() {
        return url_title;
    }

    public void setUrl_title(String url_title) {
        this.url_title = url_title;
    }

    public String getUrl_head() {
        return url_head;
    }

    public void setUrl_head(String url_head) {
        this.url_head = url_head;
    }
}
