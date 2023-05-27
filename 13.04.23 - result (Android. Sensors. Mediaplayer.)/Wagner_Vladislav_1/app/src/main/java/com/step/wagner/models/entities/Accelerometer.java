package com.step.wagner.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Accelerometer implements Parcelable {

    private final int id;

    //Оси ускорения
    private float axisX;
    private float axisY;
    private float axisZ;

    public Accelerometer() {
        id  = 0;
        axisX = 0;
        axisY = 0;
        axisZ = 0;
    }

    public Accelerometer(int id, float axisX, float axisY, float axisZ) {
        this.id = id;
        this.axisX = axisX;
        this.axisY = axisY;
        this.axisZ = axisZ;
    }

    //region Parcelable

    //Формирование объекта из parcelable коллекции
    protected Accelerometer(Parcel in) {
        id = in.readInt();
        axisX = in.readFloat();
        axisY = in.readFloat();
        axisZ = in.readFloat();
    }

    public static final Creator<Accelerometer> CREATOR = new Creator<Accelerometer>() {
        @Override
        public Accelerometer createFromParcel(Parcel in) {
            return new Accelerometer(in);
        }

        @Override
        public Accelerometer[] newArray(int size) {
            return new Accelerometer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //Запись в parcelable коллекцию
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeFloat(axisX);
        parcel.writeFloat(axisY);
        parcel.writeFloat(axisZ);
    }
    //endregion

    //region Accessors

    public int getId() {
        return id;
    }

    public float getAxisX() {
        return axisX;
    }

    public void setAxisX(float axisX) {
        this.axisX = axisX;
    }

    public float getAxisY() {
        return axisY;
    }

    public void setAxisY(float axisY) {
        this.axisY = axisY;
    }

    public float getAxisZ() {
        return axisZ;
    }

    public void setAxisZ(float axisZ) {
        this.axisZ = axisZ;
    }
    //endregion
}
