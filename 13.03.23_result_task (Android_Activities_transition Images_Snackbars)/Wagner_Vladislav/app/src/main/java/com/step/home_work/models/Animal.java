package com.step.home_work.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.utils.Utils;

public class Animal implements Parcelable {

    //Порода
    private String breed;

    //Кличка
    private String name;

    //Возраст
    private int age;

    //вес
    private int weight;

    //ФИО владельца
    private String ownerSnp;

    //Имя файла
    private String fileName;

    //Шаг инкремента/декремента
    public static int DELTA = 1;

    public Animal(String breed, String name, int age, int weight, String ownerSnp,String fileName) {
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.ownerSnp = ownerSnp;
        this.fileName = fileName;
    }

    public Animal() {
    }

    protected Animal(Parcel in) {
        breed = in.readString();
        name = in.readString();
        age = in.readInt();
        weight = in.readInt();
        ownerSnp = in.readString();
        fileName = in.readString();
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

    //Фабричный метод
    public static Animal factory(){
        FileName breedImg = Utils.getBreed();
        return new Animal(
                breedImg.field,
                Utils.getAnimalName(),
                Utils.getRandom(1,17),
                Utils.getRandom(2,10),
                Utils.getOwnerSnp(),
                breedImg.fileName
        );
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws Exception {

        if (age <= 0)
            throw new Exception("Возраст должен быть > 0");

        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) throws Exception {

        if (weight <= 0)
            throw new Exception("Вес должен быть > 0");

        this.weight = weight;
    }

    public String getOwnerSnp() {
        return ownerSnp;
    }

    public void setOwnerSnp(String ownerSnp) {
        this.ownerSnp = ownerSnp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Запись объекта в parcel-коллекцию
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(breed);
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeInt(weight);
        parcel.writeString(ownerSnp);
        parcel.writeString(fileName);
    }
}
