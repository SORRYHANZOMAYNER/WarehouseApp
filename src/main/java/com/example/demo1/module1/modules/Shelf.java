package com.example.demo1.module1.modules;


import java.util.ArrayList;
import java.util.List;


public class Shelf {


    private long shelfId;

    private List<Detail> cells = new ArrayList<>();
    public Shelf() {
        for (int i = 0; i < 4; i++) {
            cells.add(new Detail());
        }
    }
    public long getShelfId() {
        return shelfId;
    }
    public void setShelfId(long shelfId) {
        this.shelfId = shelfId;
    }
    public List<Detail> getDetails() {
        return cells;
    }

    public void setDetails(List<Detail> cells) {
        this.cells = cells;
    }


}
