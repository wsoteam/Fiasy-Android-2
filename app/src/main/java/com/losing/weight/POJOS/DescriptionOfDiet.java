package com.losing.weight.POJOS;

import java.io.Serializable;

public class DescriptionOfDiet implements Serializable {
    private String name;
    private String main;
    private String faq;
    private String exit;
    private String contraindications;

    public DescriptionOfDiet() {
    }

    public DescriptionOfDiet(String name, String main, String faq, String exit, String contraindications) {
        this.name = name;
        this.main = main;
        this.faq = faq;
        this.exit = exit;
        this.contraindications = contraindications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }
}
