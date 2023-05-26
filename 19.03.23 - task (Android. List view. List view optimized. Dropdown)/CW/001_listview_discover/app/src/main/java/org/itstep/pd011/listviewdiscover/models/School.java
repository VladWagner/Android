package org.itstep.pd011.listviewdiscover.models;

// описание предмета из учебных принадлежностей
public class School {
    private String name;      // название школьной канцелярии
    private double cost;      // цена
    private int    amount;    // количество
    private int    idImage;   // идентификатор ресурса - картинки

    public School() {}
    public School(String name, double cost, int amount, int idImage) {
        this.name = name;
        this.cost = cost;
        this.amount = amount;
        this.idImage = idImage;
    }

    //region Геттеры/сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }
    //endregion
} // class School

