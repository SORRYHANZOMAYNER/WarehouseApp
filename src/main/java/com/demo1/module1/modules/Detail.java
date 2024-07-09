package com.demo1.module1.modules;
public class Detail {

    private long detailId;
    private String detailName;
    private int quantity;
    public long getDetailId() {
        return detailId;
    }

    public void setDetailId(long detailId) {
        this.detailId = detailId;
    }
    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Detail(){

    }
    public Detail(String name,int quantity){
        this.detailName = name;
        this.quantity = quantity;
    }
}
