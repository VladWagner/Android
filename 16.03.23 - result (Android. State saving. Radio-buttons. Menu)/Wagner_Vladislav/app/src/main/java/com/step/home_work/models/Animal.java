package com.step.home_work.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;

public class Animal implements Parcelable {

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
    private boolean keeping;

    //Шаг инкремента/декремента
    public static int DELTA = 1;

    public Animal(String breed, String name, int age, double weight, String ownerSnp/*,String fileName*/,boolean diet, boolean keeping) {
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.ownerSnp = ownerSnp;
        //this.fileName = fileName;
        this.diet = diet;
        this.keeping = keeping;
    }

    public Animal() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Animal(Parcel in) {
        breed = in.readString();
        name = in.readString();
        age = in.readInt();
        weight = in.readDouble();
        ownerSnp = in.readString();
        //fileName = in.readString();
        diet = in.readBoolean();
        keeping = in.readBoolean();
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
        FileName breedImg = Utils.getBreed();
        return new Animal(
                breedImg.field,
                Utils.getAnimalName(),
                Utils.getRandom(1,17),
                Utils.getRandom(2d,10d),
                Utils.getOwnerSnp(),
               //breedImg.fileName,
                Utils.getRandom(0,2)>0,
                Utils.getRandom(0,2)>0
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

    public void setOwnerSnp(String ownerSnp) {
        this.ownerSnp = ownerSnp;
    }

    /*public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }*/


    //Выбор имени файла в зависимости от клички
    public String getFileName() {
        String fileName;

        switch (breed){
            case "Мейн-кун":
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
                fileName = "cat.jpg";
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

    public boolean isFreeKeeping() {
        return keeping;
    }

    public void setKeeping(boolean keeping) {
        this.keeping = keeping;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Запись объекта в parcel-коллекцию
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(breed);
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeDouble(weight);
        parcel.writeString(ownerSnp);
        //parcel.writeString(fileName);
        parcel.writeBoolean(diet);
        parcel.writeBoolean(keeping);
    }
}
