package org.itstep.pd011.uielemsmultiactivities.models;

import java.io.Serializable;
import java.util.Locale;

// класс для передачи в активность через Serializable
public class User implements Serializable {
    private String fileName;
    private int age;
    private double weight;

    public User() {
        this("image1,jpg", 12, 15.5);
    }

    public User(String fileName, int age, double weight) {
        this.fileName = fileName;
        this.age = age;
        this.weight = weight;
    }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format(Locale.UK, "Имя файла: '%s'\nВозраст, лет: %d\nВес, кг: %.3f",
                fileName, age, weight);
    }
}

