package org.itstep.pd011.recyclerviewintro.models;

import android.os.Parcelable;
import android.os.Parcel;

import org.itstep.pd011.recyclerviewintro.R;

// описание животного
public class Animal implements Parcelable {
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

    protected Animal(Parcel in) {
        name = in.readString();
        weight = in.readDouble();
        photoId = in.readInt();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(weight);
        dest.writeInt(photoId);
    }
} // class Animal

