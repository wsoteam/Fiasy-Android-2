package com.wsoteam.diet.Articles.POJO;

import java.io.Serializable;

public class Article implements Serializable {

    private boolean premium;
    private String imgUrl;
    private String title;
    private String introPart;
    private String mainPart;
    private String section;

    public Article() {
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroPart() {
        return introPart;
    }

    public void setIntroPart(String introPart) {
        this.introPart = introPart;
    }

    public String getMainPart() {
        return mainPart;
    }

    public void setMainPart(String mainPart) {
        this.mainPart = mainPart;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
