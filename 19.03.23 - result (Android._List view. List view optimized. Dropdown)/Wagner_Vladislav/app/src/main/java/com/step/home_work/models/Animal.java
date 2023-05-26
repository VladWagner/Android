package com.step.home_work.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;

public class Animal implements Parcelable {

    //id животного
    private int id;

    //Порода
    private String breed;

    //Кличка
    private String name;

    //Возраст
    private int age;

    //вес
    private double weight;

    //ФИО владельца
    private String ownerSnp;

    //Имя файла
    //private String fileName;

    //Диета
    private boolean diet;

    //полу-вольное содержание
    private boolean specialCare;

    //Шаг инкремента/декремента
    public static int DELTA = 1;

    public Animal(String breed, String name, int age, double weight, String ownerSnp,boolean diet, boolean specialCare) {
        this.id = ++Parameters.lastAnimalId;
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.ownerSnp = ownerSnp;
        this.diet = diet;
        this.specialCare = specialCare;
    }

    //ctor для копирования объекта
    private Animal(int id,String breed, String name, int age, double weight, String ownerSnp,boolean diet, boolean specialCare) {
        this.id = id;
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.ownerSnp = ownerSnp;
        this.diet = diet;
        this.specialCare = specialCare;
    }


    public Animal() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Animal(Parcel in) {
        id = in.readInt();
        breed = in.readString();
        name = in.readString();
        age = in.readInt();
        weight = in.readDouble();
        ownerSnp = in.readString();
        diet = in.readBoolean();
        specialCare = in.readBoolean();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
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
        return new Animal(
                Utils.getBreed().value1,
                Utils.getAnimalName(),
                Utils.getRandom(1,17),
                Utils.getRandom(2d,10d),
                Utils.getOwnerSnp(),
                Utils.getRandom(0,2)>0,
                Utils.getRandom(0,2)>0
        );
    }

    public int getId() {
        return id;
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

    public void setName(String name) throws Exception {
        if (ownerSnp.isBlank())
            throw new Exception("Кличка задана некорректно!");

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) throws Exception {

        if (weight <= 0)
            throw new Exception("Вес должен быть > 0");

        this.weight = weight;
    }

    public String getOwnerSnp() {
        return ownerSnp;
    }

    public void setOwnerSnp(String ownerSnp) throws Exception {

        if (ownerSnp.isBlank())
            throw new Exception("ФИО владельца задано некорректно!");

        this.ownerSnp = ownerSnp;
    }

    //Выбор имени файла в зависимости от клички
    public String getFileName() {
        String fileName;

        switch (breed){
            case "мейн-кун":
                fileName = Parameters.MAINE_COON;
                break;
            case "скоттиш-фолд":
                fileName = Parameters.SCOTTISH;
                break;
            case "бенгальская":
                fileName = Parameters.BENGAL;
                break;
            case "абиссинская":
                fileName = Parameters.ABYSSINIAN;
                break;
            default:
                fileName = "abyssinian.jpg";
              break;
        }

        return fileName;
    }

    public boolean isDiet() {
        return diet;
    }

    public void setDiet(boolean diet) {
        this.diet = diet;
    }

    public boolean isSpecialCare() {
        return specialCare;
    }

    public void setSpecialCare(boolean specialCare) {
        this.specialCare = specialCare;
    }


    @NonNull
    public Animal clone(){

        return new Animal(
                this.id,
                this.breed,
                this.name,
                this.age,
                this.weight,
                this.ownerSnp,
                this.diet,
                this.specialCare);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Запись объекта в parcel-коллекцию
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(breed);
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeDouble(weight);
        parcel.writeString(ownerSnp);
        parcel.writeBoolean(diet);
        parcel.writeBoolean(specialCare);
    }
}
