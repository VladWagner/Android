package org.itstep.pd011.jsonparserexample.models;

import java.util.Locale;

// класс для сохранения в JSON, парсинга из JSON
public class Product {
    private String name;       // наименование товара
    private int    price;      // цена едининцы товара
    private int    amount;     // количество товара

    //region Конструкторы
    public Product() {
        this("мышь беспроводная", 1200, 1);
    }

    public Product(String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
    //endregion


    //region Аксессоры класса
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    //endregion


    // для вывода в ListView без применения адаптера
    @Override
    public String toString() {
        return String.format(Locale.UK, "%s, %d.00 руб., %d шт.", name, price, amount);
    } // toString
} // class Goods
