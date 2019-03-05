package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;

public class Ex implements Serializable {
    private String title;
    private String basic_muscle;
    private String additional_muscle;
    private String complexity;
    private String img_url;
    private String for_man;
    private String for_woman;
    private String detail;
    private String main_chips;
    private String url_of_logo;

    public Ex() {
    }

    public Ex(String title, String basic_muscle, String additional_muscle, String complexity, String img_url, String for_man, String for_woman, String detail, String main_chips, String url_of_logo) {
        this.title = title;
        this.basic_muscle = basic_muscle;
        this.additional_muscle = additional_muscle;
        this.complexity = complexity;
        this.img_url = img_url;
        this.for_man = for_man;
        this.for_woman = for_woman;
        this.detail = detail;
        this.main_chips = main_chips;
        this.url_of_logo = url_of_logo;
    }

    public String getUrl_of_logo() {
        return url_of_logo;
    }

    public void setUrl_of_logo(String url_of_logo) {
        this.url_of_logo = url_of_logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBasic_muscle() {
        return basic_muscle;
    }

    public void setBasic_muscle(String basic_muscle) {
        this.basic_muscle = basic_muscle;
    }

    public String getAdditional_muscle() {
        return additional_muscle;
    }

    public void setAdditional_muscle(String additional_muscle) {
        this.additional_muscle = additional_muscle;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getFor_man() {
        return for_man;
    }

    public void setFor_man(String for_man) {
        this.for_man = for_man;
    }

    public String getFor_woman() {
        return for_woman;
    }

    public void setFor_woman(String for_woman) {
        this.for_woman = for_woman;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMain_chips() {
        return main_chips;
    }

    public void setMain_chips(String main_chips) {
        this.main_chips = main_chips;
    }
}
