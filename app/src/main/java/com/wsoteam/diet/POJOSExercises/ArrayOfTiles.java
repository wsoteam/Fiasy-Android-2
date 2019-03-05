package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class ArrayOfTiles implements Serializable {
    private String title;
    private List<Tile> tileList;

    public ArrayOfTiles() {
    }

    public ArrayOfTiles(String title, List<Tile> tileList) {
        this.title = title;
        this.tileList = tileList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Tile> getTileList() {
        return tileList;
    }

    public void setTileList(List<Tile> tileList) {
        this.tileList = tileList;
    }
}
