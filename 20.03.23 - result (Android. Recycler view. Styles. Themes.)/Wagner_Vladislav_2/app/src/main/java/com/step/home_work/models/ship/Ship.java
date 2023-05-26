package com.step.home_work.models.ship;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.SimpleTuple;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;

public class Ship implements Parcelable {

    //id судна
    private int id;

    //Тип судна
    private ShipType shipType;

    //Название судна
    private String shipName;

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
    //Нужна ли якорная стоянка
    private boolean anchorage;

    //Нужна ли якорная дозаправка
    private boolean refueling;

    //Нужен ли лоцман
    private boolean dock;

    public static int WEIGHT_DELTA = 20;
    public static int PRICE_DELTA = 10;

    public Ship(ShipType shipType,String shipName, int loadCapacity, String destination, String cargoType,
                int cargoWeight, int tonPrice,
                boolean anchorage, boolean refueling, boolean dock) {

        //Установить id
        this.id = ++Parameters.lastShipId;
        this.shipType = shipType;
        this.shipName = shipName;
        this.loadCapacity = loadCapacity;
        this.destination = destination;
        this.cargoType = cargoType;
        this.cargoWeight = cargoWeight;
        this.tonPrice = tonPrice;
        //this.fileName = fileName;
        this.anchorage = anchorage;
        this.refueling = refueling;
        this.dock = dock;
    }


    public Ship(int id, ShipType shipType,String shipName, int loadCapacity, String destination, String cargoType,
                int cargoWeight, int tonPrice,
                boolean anchorage, boolean refueling, boolean dock) {

        this.id = id;
        this.shipType = shipType;
        this.shipName = shipName;
        this.loadCapacity = loadCapacity;
        this.destination = destination;
        this.cargoType = cargoType;
        this.cargoWeight = cargoWeight;
        this.tonPrice = tonPrice;
        //this.fileName = fileName;
        this.anchorage = anchorage;
        this.refueling = refueling;
        this.dock = dock;
    }

    public Ship() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Ship(Parcel in) {
        id = in.readInt();
        shipType = in.readParcelable(ShipType.class.getClassLoader());
        shipName = in.readString();
        loadCapacity = in.readInt();
        destination = in.readString();
        cargoType = in.readString();
        cargoWeight = in.readInt();
        tonPrice = in.readInt();
        anchorage = in.readBoolean();
        dock = in.readBoolean();
        refueling = in.readBoolean();
    }

    public static final Creator<Ship> CREATOR = new Creator<Ship>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
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

        ShipType shipType = Utils.getShipType();

        //Получить груз в зависимости от типа судна
        CargoType cargo = Utils.getSpecificCargo(shipType.getTypeId());
        int capacity = Utils.getRandom(120_000,300_000);

        return new Ship(shipType,
                Utils.getShipName(),
                capacity,
                Utils.getDestination(),
                cargo.type,
                Utils.getRandom(100_000,capacity),
                cargo.tonPrice,
                Utils.getRandom(0,2)>0,
                Utils.getRandom(0,2)>0,
                Utils.getRandom(0,2)>0
        );
    }

    //Стоимость груза
    public static long countCargoPrice(int weight, int tonPrice){

        if (weight <= 0 || tonPrice <= 0)
            return 0;
        return (long) weight * tonPrice;
    }

    //Создание копии
    @NonNull
    public Ship clone(){
        return new Ship(this.id,
                this.shipType,
                this.shipName,
                this.loadCapacity,
                this.destination,
                this.cargoType,
                this.cargoWeight,
                this.tonPrice,
                this.anchorage,
                this.refueling,
                this.dock);
    }

    //region Accessors


    public int getId() {
        return id;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) throws Exception {
        if (shipType == null)
            throw new Exception("Тип судна задан некорректно!");

        this.shipType = shipType;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) throws Exception {

        if (shipName.isBlank())
            throw new Exception("Название судна задано некорректно!");

        this.shipName = shipName;
    }

    public int getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(int loadCapacity) throws Exception {

        if (loadCapacity <= 0 || loadCapacity < cargoWeight)
            throw new Exception("Грузоподъемность должна быть > 0 и больше веса груза");

        this.loadCapacity = loadCapacity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) throws Exception {
        if (destination.isBlank())
            throw new Exception("Пункт назначения задан некорректно!");

        this.destination = destination;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) throws Exception {
        if (cargoType.isBlank())
            throw new Exception("Тип груза задан некорректно!");

        this.cargoType = cargoType;
    }

    public int getCargoWeight() {

        return cargoWeight;
    }

    public void setCargoWeight(int cargoWeight) throws Exception {
        if (loadCapacity < cargoWeight || cargoWeight <= 0)
            throw new Exception(String.format("Вес груза должен быть > 0 и < %d",loadCapacity));

        this.cargoWeight = cargoWeight;
    }

    public int getTonPrice() {

        return tonPrice;
    }

    public void setTonPrice(int tonPrice) throws Exception {
        if (tonPrice <= 0)
            throw new Exception("Цена за 1 т. должна быть > 0");

        this.tonPrice = tonPrice;
    }

    public String getFileNameFromType() {
        String resFileName;

        switch (shipType.getTypeId()){
            case Parameters.BULK_ID:
                resFileName = Parameters.BULK;
                break;
            case Parameters.CONTAINERS_CARRIER_ID:
                resFileName = Parameters.CONTAINERS_CARRIER;
                break;
            default:
                resFileName = Parameters.BULK;
                break;
        }
        return resFileName;
    }

    public boolean isAnchorage() {
        return anchorage;
    }

    public void setAnchorage(boolean anchorage) {
        this.anchorage = anchorage;
    }

    public boolean isRefuelingNeeds() {
        return refueling;
    }

    public void setRefueling(boolean refueling) {
        this.refueling = refueling;
    }

    public boolean isDockNeeds() {
        return dock;
    }

    public void setDock(boolean dock) {
        this.dock = dock;
    }

    //endregion


    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(shipType,i);
        parcel.writeString(shipName);
        parcel.writeInt(loadCapacity);
        parcel.writeString(destination);
        parcel.writeString(cargoType);
        parcel.writeInt(cargoWeight);
        parcel.writeInt(tonPrice);
        parcel.writeBoolean(anchorage);
        parcel.writeBoolean(dock);
        parcel.writeBoolean(refueling);
    }

}
