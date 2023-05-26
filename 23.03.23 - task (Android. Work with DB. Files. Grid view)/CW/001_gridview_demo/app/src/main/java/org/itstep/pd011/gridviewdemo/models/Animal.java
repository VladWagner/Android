package org.itstep.pd011.gridviewdemo.models;

import org.itstep.pd011.gridviewdemo.R;

// описание животного
public class Animal {
    private String name;      // кличка животного
    private double weight;    // вес животного
    private int    photoId;   // ид ресурса с изображением животного

    //region Конструкторы
    public Animal() { new Animal("Барсик", 3.4, R.drawable.cat); } // Animal

    public Animal(String name, double weight, int photoId) {
        this.name = name;
        this.weight = weight;
        this.photoId = photoId;
    } // Animal
    //endregion

    //region Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
    //endregion

    // изменение веса животного на заданное значение
    void changeWeight(double delta) {
        double temp = weight + delta;
        if (weight + delta <= 0) return;

        weight += temp;
    } // changeWeight
} // class Animal

