package com.step.home_work.models;


public class Product {
    private String name;

    private int amount;

    private int price;

    public Product(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public Product() {
    }

    public int countSum(){
        return amount*price;
    }

    //region Accessors

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name.isBlank())
            throw new Exception("Наименование не может быть пустым");
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    //endregion
}
