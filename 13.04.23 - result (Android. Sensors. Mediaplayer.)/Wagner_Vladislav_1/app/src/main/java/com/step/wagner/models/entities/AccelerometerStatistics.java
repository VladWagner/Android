package com.step.wagner.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

//Класс, хранящий отдельную статистику по акселерометрам, значения которых были получены во врем сеанса сбора параметров
public class AccelerometerStatistics implements Parcelable {

    private final int id;

    //Оси ускорения
    private float axisX_min;
    private float axisX_avg;
    private float axisX_max;

    private float axisY_min;
    private float axisY_avg;
    private float axisY_max;

    private float axisZ_min;
    private float axisZ_avg;
    private float axisZ_max;

    public AccelerometerStatistics() {
        id  = 0;
       axisX_min = 0;
       axisX_avg = 0;
       axisX_max = 0;

       axisY_min = 0;
       axisY_avg = 0;
       axisY_max = 0;

       axisZ_min = 0;
       axisZ_avg = 0;
       axisZ_max = 0;
    }

    public AccelerometerStatistics(int id,
                                   float axisX_min,
                                   float axisX_avg,
                                   float axisX_max,
                                   float axisY_min,
                                   float axisY_avg,
                                   float axisY_max,
                                   float axisZ_min,
                                   float axisZ_avg,
                                   float axisZ_max) {
        this.id = id;
        this.axisX_min = axisX_min;
        this.axisX_avg = axisX_avg;
        this.axisX_max = axisX_max;
        this.axisY_min = axisY_min;
        this.axisY_avg = axisY_avg;
        this.axisY_max = axisY_max;
        this.axisZ_min = axisZ_min;
        this.axisZ_avg = axisZ_avg;
        this.axisZ_max = axisZ_max;
    }

    //region Parcelable
    protected AccelerometerStatistics(Parcel in) {
        id = in.readInt();
        axisX_min = in.readFloat();
        axisX_avg = in.readFloat();
        axisX_max = in.readFloat();
        axisY_min = in.readFloat();
        axisY_avg = in.readFloat();
        axisY_max = in.readFloat();
        axisZ_min = in.readFloat();
        axisZ_avg = in.readFloat();
        axisZ_max = in.readFloat();
    }

    public static final Creator<AccelerometerStatistics> CREATOR = new Creator<AccelerometerStatistics>() {
        @Override
        public AccelerometerStatistics createFromParcel(Parcel in) {
            return new AccelerometerStatistics(in);
        }

        @Override
        public AccelerometerStatistics[] newArray(int size) {
            return new AccelerometerStatistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeFloat(axisX_min);
        parcel.writeFloat(axisX_avg);
        parcel.writeFloat(axisX_max);
        parcel.writeFloat(axisY_min);
        parcel.writeFloat(axisY_avg);
        parcel.writeFloat(axisY_max);
        parcel.writeFloat(axisZ_min);
        parcel.writeFloat(axisZ_avg);
        parcel.writeFloat(axisZ_max);
    }
    //endregion


    //region Accessors
    public int getId() {
        return id;
    }

    public float getAxisX_min() {
        return axisX_min;
    }

    public void setAxisX_min(float axisX_min) {
        this.axisX_min = axisX_min;
    }

    public float getAxisX_avg() {
        return axisX_avg;
    }

    public void setAxisX_avg(float axisX_avg) {
        this.axisX_avg = axisX_avg;
    }

    public float getAxisX_max() {
        return axisX_max;
    }

    public void setAxisX_max(float axisX_max) {
        this.axisX_max = axisX_max;
    }

    public float getAxisY_min() {
        return axisY_min;
    }

    public void setAxisY_min(float axisY_min) {
        this.axisY_min = axisY_min;
    }

    public float getAxisY_avg() {
        return axisY_avg;
    }

    public void setAxisY_avg(float axisY_avg) {
        this.axisY_avg = axisY_avg;
    }

    public float getAxisY_max() {
        return axisY_max;
    }

    public void setAxisY_max(float axisY_max) {
        this.axisY_max = axisY_max;
    }

    public float getAxisZ_min() {
        return axisZ_min;
    }

    public void setAxisZ_min(float axisZ_min) {
        this.axisZ_min = axisZ_min;
    }

    public float getAxisZ_avg() {
        return axisZ_avg;
    }

    public void setAxisZ_avg(float axisZ_avg) {
        this.axisZ_avg = axisZ_avg;
    }

    public float getAxisZ_max() {
        return axisZ_max;
    }

    public void setAxisZ_max(float axisZ_max) {
        this.axisZ_max = axisZ_max;
    }
    //endregion
}
