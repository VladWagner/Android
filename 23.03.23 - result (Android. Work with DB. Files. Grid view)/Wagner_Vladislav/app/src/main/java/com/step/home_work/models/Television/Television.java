package com.step.home_work.models.Television;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.infrastructure.SimpleTuple;
import com.step.home_work.infrastructure.Utils;

import java.io.Serializable;

public class Television implements Parcelable, Serializable {


    //Производитель
    private String producer;


    //Имя файла
    private String fileName;

    //Диагональ экрана
    private double diagonal;

    //разрешение по вертикали
    private int resolutionVert;

    //разрешение по горизонтали
    private int resolutionHorizon;

    //Цена
    private int price;

    public Television(String producer, String fileName, double diagonal, int resolutionVert, int resolutionHorizon, int price) {
        this.producer = producer;
        this.fileName = fileName;
        this.diagonal = diagonal;
        this.resolutionVert = resolutionVert;
        this.resolutionHorizon = resolutionHorizon;
        this.price = price;
    }

    protected Television(Parcel in) {
        producer = in.readString();
        fileName = in.readString();
        diagonal = in.readDouble();
        resolutionVert = in.readInt();
        resolutionHorizon = in.readInt();
        price = in.readInt();
    }

    public static final Creator<Television> CREATOR = new Creator<Television>() {
        @Override
        public Television createFromParcel(Parcel in) {
            return new Television(in);
        }

        @Override
        public Television[] newArray(int size) {
            return new Television[size];
        }
    };

    //Фабричный метод
    public static Television factory(){

        SimpleTuple<String,String> producer = Utils.getBreed();

        return new Television(
                producer.value1,
                producer.value2,
                Utils.getRandom(25d,60d),
                Utils.getRandom(720,1080),
                Utils.getRandom(1080,1920),
                Utils.getRandom(60_000,120_000)
        );
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(double diagonal) {
        this.diagonal = diagonal;
    }

    public int getResolutionVert() {
        return resolutionVert;
    }

    public void setResolutionVert(int resolutionVert) {
        this.resolutionVert = resolutionVert;
    }

    public int getResolutionHorizon() {
        return resolutionHorizon;
    }

    public void setResolutionHorizon(int resolutionHorizon) {
        this.resolutionHorizon = resolutionHorizon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(producer);
        parcel.writeString(fileName);
        parcel.writeDouble(diagonal);
        parcel.writeInt(resolutionVert);
        parcel.writeInt(resolutionHorizon);
        parcel.writeInt(price);
    }
}
