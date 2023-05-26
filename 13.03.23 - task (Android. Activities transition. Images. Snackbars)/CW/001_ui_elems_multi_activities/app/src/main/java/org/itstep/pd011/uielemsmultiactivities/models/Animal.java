package org.itstep.pd011.uielemsmultiactivities.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// пример объекта для передачи в активнось при помощи интерфейса Parcelable
public class Animal implements Parcelable {
    private String fileName;
    private int age;
    private double weight;

    public Animal() { this("image1,jpg", 12, 15.5); }

    public Animal(String fileName, int age, double weight) {
        this.fileName = fileName;
        this.age = age;
        this.weight = weight;
    }

    protected Animal(Parcel in) {
        fileName = in.readString();
        age = in.readInt();
        weight = in.readDouble();
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

    @Override
    public String toString() {
        return "Animal{" +
                "fileName='" + fileName + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                '}';
    }

    public String getFileName() {
        return fileName;
    }

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeInt(age);
        parcel.writeDouble(weight);
    }
}
