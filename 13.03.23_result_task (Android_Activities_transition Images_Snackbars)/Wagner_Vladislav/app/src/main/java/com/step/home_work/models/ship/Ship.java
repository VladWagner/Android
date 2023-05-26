package com.step.home_work.models.ship;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.models.FileName;
import com.step.home_work.utils.Utils;

import java.util.Locale;

public class Ship implements Parcelable {

    //Тип судна
    private String shipType;

    //Грузоподъемность
    private int loadCapacity;

    //Пункт назначения
    private String destination;

    //Тип груза
    private String cargoType;

    //Вес груза
    private int cargoWeight;

    //Стоимость 1 тонны
    private int tonPrice;

    //Имя файла
    private String fileName;

    public static int WEIGHT_DELTA = 20;

    public Ship(String shipType, int loadCapacity, String destination, String cargoType, int cargoWeight, int tonPrice, String fileName) {
        this.shipType = shipType;
        this.loadCapacity = loadCapacity;
        this.destination = destination;
        this.cargoType = cargoType;
        this.cargoWeight = cargoWeight;
        this.tonPrice = tonPrice;
        this.fileName = fileName;
    }

    public Ship() {
    }

    protected Ship(Parcel in) {
        shipType = in.readString();
        loadCapacity = in.readInt();
        destination = in.readString();
        cargoType = in.readString();
        cargoWeight = in.readInt();
        tonPrice = in.readInt();
        fileName = in.readString();
    }

    public static final Creator<Ship> CREATOR = new Creator<Ship>() {
        @Override
        public Ship createFromParcel(Parcel in) {
            return new Ship(in);
        }

        @Override
        public Ship[] newArray(int size) {
            return new Ship[size];
        }
    };

    //Фабричный метод
    public static Ship factory(){

        FileName shipImg = Utils.getShipType();
        CargoType cargo = Utils.getCargoType();
        int capacity = Utils.getRandom(120_000,300_000);

        return new Ship(shipImg.field,
                capacity,
                Utils.getDestination(),
                cargo.type,
                Utils.getRandom(100_000,capacity),
                cargo.tonPrice,
                shipImg.fileName);
    }

    //Стоимость груза
    public static long countCargoPrice(int weight, int tonPrice){

        if (weight <= 0 || tonPrice <= 0)
            return 0;
        return (long) weight * tonPrice;
    }

    //region Accessors
    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public int getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(int loadCapacity) throws Exception {

        if (loadCapacity <= 0)
            throw new Exception("Грузоподъемность должна быть > 0");

        this.loadCapacity = loadCapacity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public int getCargoWeight() {

        return cargoWeight;
    }

    public void setCargoWeight(int cargoWeight) throws Exception {
        if (loadCapacity <= 0)
            throw new Exception("Вес груза должен быть > 0");
        this.cargoWeight = cargoWeight;
    }

    public int getTonPrice() {

        return tonPrice;
    }

    public void setTonPrice(int tonPrice) throws Exception {
        if (loadCapacity <= 0)
            throw new Exception("Цена за 1 т. должна быть > 0");

        this.tonPrice = tonPrice;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameFromType() {
        String resFileName = "bulk.jpg";

        if (shipType.toLowerCase().contains("контейнер") || shipType.toLowerCase().contains("container"))
            resFileName = "container.jpg";

        return resFileName;
    }


    //endregion


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(shipType);
        parcel.writeInt(loadCapacity);
        parcel.writeString(destination);
        parcel.writeString(cargoType);
        parcel.writeInt(cargoWeight);
        parcel.writeInt(tonPrice);
        parcel.writeString(fileName);
    }

}
