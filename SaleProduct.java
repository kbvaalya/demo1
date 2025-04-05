package com.example.demo1;

public class SaleProduct {
    private int id;
    private String productName;
    private double price;
    private int quantity;
    private int finalPrice;
    private String date;

    public SaleProduct(int id, String productName, double price, int quantity, int finalPrice, String date) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
        this.date = date;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getFinalPrice() {
        return finalPrice;
    }
    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String date() {
        return null;
    }

}
