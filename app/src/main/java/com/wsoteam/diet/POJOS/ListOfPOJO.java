package com.wsoteam.diet.POJOS;

import java.io.Serializable;
import java.util.List;

public class ListOfPOJO implements Serializable {
    private String name;
    private List<POJO> listOFPOJO;

    public ListOfPOJO() {
    }

    public ListOfPOJO(String name, List<POJO> listOFPOJO) {
        this.name = name;
        this.listOFPOJO = listOFPOJO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<POJO> getListOFPOJO() {
        return listOFPOJO;
    }

    public void setListOFPOJO(List<POJO> listOFPOJO) {
        this.listOFPOJO = listOFPOJO;
    }
}
