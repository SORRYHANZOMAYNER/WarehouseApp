package com.demo1.module1.modules;
import java.util.ArrayList;
import java.util.List;
public class Shelf {
    private long shelfId;

    private List<Detail> detailsOnShelf = new ArrayList<>();

    public Shelf() {
        for (int i = 0; i < 4; i++) {
            detailsOnShelf.add(new Detail());
        }
    }

    public long getShelfId() {
        return shelfId;
    }

    public void setShelfId(long shelfId) {
        this.shelfId = shelfId;
    }

    public List<Detail> getDetails() {
        return detailsOnShelf;
    }

    public void setDetails(List<Detail> detailsOnShelf) {
        this.detailsOnShelf = detailsOnShelf;
    }


}
