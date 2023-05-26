package org.itstep.pd011.jsonparserexample.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Store {
    private List<Product> productList;

    //region Конструкторы
    public Store() {
        this(new ArrayList<>());
        initializer();
    } // Store

    public Store(List<Product> productList) {
        this.productList = productList;
    } // Store
    //endregion


    public List<Product> getProductList() {
        return productList;
    }
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    // заполнение коллекции начальными данными
    private void initializer() {
        productList.clear();
        productList.addAll(Arrays.asList(
            new Product("Ручка шариковая", 35, 11),
            new Product("Крышка пластиковая", 1, 230),
            new Product("Ручка гелевая", 54, 6),
            new Product("Зонтик автоматчиеский", 1230, 2),
            new Product("Портсигар импортный", 670, 3),
            new Product("Зажигалка газовая", 60, 22),
            new Product("Зажигалка бензиновая", 50, 30),
            new Product("Спички", 10, 300),
            new Product("Лампа светодиодная", 120, 6),
            new Product("Стакан картонный", 9, 230)
        ));
    } // initializer
} // class Store