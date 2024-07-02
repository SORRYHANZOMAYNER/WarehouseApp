package com.example.demo1.module1.DTO;

public class DetailDTO {
    private long detailId;
    private String detailName;
    private int quantity;
    public DetailDTO(){}

    public DetailDTO setDetailId(long detailId) {
        this.detailId = detailId;
        return this;
    }
    public long getDetailId() {
        return detailId;
    }
    public DetailDTO setDetailName(String detailName) {
        this.detailName = detailName;
        return this;
    }
    public String getDetailName(){return detailName;}

    public DetailDTO setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    public int getQuantity() {
        return quantity;
    }

}

