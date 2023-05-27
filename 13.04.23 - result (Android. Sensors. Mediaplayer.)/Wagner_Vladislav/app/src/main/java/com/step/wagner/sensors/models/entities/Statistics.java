package com.step.wagner.sensors.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.wagner.sensors.infrastructure.Utils;

import java.util.Date;

public class Statistics implements Parcelable {

    private final int id;

    //Количество записей
    private int amount;

    //Статистика по значениям акселерометров
    private AccelerometerStatistics accelerometerStatistics;

    //Агрегатные значения по приближению
    private float approximationMin;
    private float approximationAvg;
    private float approximationMax;

    //Агрегатные значения по датчику освещения
    private float lightMin;
    private float lightAvg;
    private float lightMax;

    //Дата и время начала сбора
    private Date collectingStartTime;

    //Дата и время начала обработки
    private Date handlingTime;

    //Заданные датчики для прослушивания
    private String sensorsTypes;


    //region Конструкторы
    public Statistics(int id,
                      int amount,
                      AccelerometerStatistics accelerometerStatistics,
                      float approximationMin,
                      float approximationAvg,
                      float approximationMax,
                      float lightMin,
                      float lightAvg,
                      float lightMax,
                      Date collectingStartTime,
                      Date handlingTime,
                      String sensorsTypes) {
        this.id = id;
        this.amount = amount;
        this.accelerometerStatistics = accelerometerStatistics;
        this.approximationMin = approximationMin;
        this.approximationAvg = approximationAvg;
        this.approximationMax = approximationMax;
        this.lightMin = lightMin;
        this.lightAvg = lightAvg;
        this.lightMax = lightMax;
        this.collectingStartTime = collectingStartTime;
        this.handlingTime = handlingTime;
        this.sensorsTypes = sensorsTypes;
    }//ctor

    public Statistics(int id,
                      int amount,
                      int accelerometerStatId,
                      float axisX_min,
                      float axisX_avg,
                      float axisX_max,
                      float axisY_min,
                      float axisY_avg,
                      float axisY_max,
                      float axisZ_min,
                      float axisZ_avg,
                      float axisZ_max,
                      float approximationMin,
                      float approximationAvg,
                      float approximationMax,
                      float lightMin,
                      float lightAvg,
                      float lightMax,
                      Date collectingStartTime,
                      Date handlingTime,
                      String sensorsTypes) {
        this.id = id;
        this.amount = amount;

        this.accelerometerStatistics = new AccelerometerStatistics(accelerometerStatId,axisX_min,axisX_avg,axisX_max,
                axisY_min,axisY_avg,axisY_max,
                axisZ_min,axisZ_avg,axisZ_max);

        this.approximationMin = approximationMin;
        this.approximationAvg = approximationAvg;
        this.approximationMax = approximationMax;
        this.lightMin = lightMin;
        this.lightAvg = lightAvg;
        this.lightMax = lightMax;
        this.collectingStartTime = collectingStartTime;
        this.handlingTime = handlingTime;
        this.sensorsTypes = sensorsTypes;
    }//ctor

    public Statistics() {
        this.id = 0;
        this.amount = 0;

        this.accelerometerStatistics = new AccelerometerStatistics(0,0,0,0,
                0,0,0,
                0,0,0);

        this.approximationMin = 0;
        this.approximationAvg = 0;
        this.approximationMax = 0;
        this.lightMin = 0;
        this.lightAvg = 0;
        this.lightMax = 0;
        this.collectingStartTime = null;
        this.handlingTime = null;
        this.sensorsTypes = "";
    }//ctor
    //endregion

    //region Parcelable
    protected Statistics(Parcel in) {
        id = in.readInt();
        amount = in.readInt();
        accelerometerStatistics = in.readParcelable(AccelerometerStatistics.class.getClassLoader());
        approximationMin = in.readFloat();
        approximationAvg = in.readFloat();
        approximationMax = in.readFloat();
        lightMin = in.readFloat();
        lightAvg = in.readFloat();
        lightMax = in.readFloat();
        sensorsTypes = in.readString();

        collectingStartTime = Utils.tryParseDbDateTime(in.readString());
        handlingTime = Utils.tryParseDbDateTime(in.readString());
    }

    public static final Creator<Statistics> CREATOR = new Creator<Statistics>() {
        @Override
        public Statistics createFromParcel(Parcel in) {
            return new Statistics(in);
        }

        @Override
        public Statistics[] newArray(int size) {
            return new Statistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(amount);
        parcel.writeParcelable(accelerometerStatistics, i);
        parcel.writeFloat(approximationMin);
        parcel.writeFloat(approximationAvg);
        parcel.writeFloat(approximationMax);
        parcel.writeFloat(lightMin);
        parcel.writeFloat(lightAvg);
        parcel.writeFloat(lightMax);
        parcel.writeString(sensorsTypes);

        parcel.writeString(Utils.dbDateTimeFormat.format(this.collectingStartTime));
        parcel.writeString(Utils.dbDateTimeFormat.format(this.handlingTime));
    }
    //endregion

    //region Accessors

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public AccelerometerStatistics getAccelerometerStatistics() {
        return accelerometerStatistics;
    }

    public void setAccelerometerStatistics(AccelerometerStatistics accelerometerStatistics) {
        this.accelerometerStatistics = accelerometerStatistics;
    }

    public float getApproximationMin() {
        return approximationMin;
    }

    public void setApproximationMin(float approximationMin) {
        this.approximationMin = approximationMin;
    }

    public float getApproximationAvg() {
        return approximationAvg;
    }

    public void setApproximationAvg(float approximationAvg) {
        this.approximationAvg = approximationAvg;
    }

    public float getApproximationMax() {
        return approximationMax;
    }

    public void setApproximationMax(float approximationMax) {
        this.approximationMax = approximationMax;
    }

    public float getLightMin() {
        return lightMin;
    }

    public void setLightMin(float lightMin) {
        this.lightMin = lightMin;
    }

    public float getLightAvg() {
        return lightAvg;
    }

    public void setLightAvg(float lightAvg) {
        this.lightAvg = lightAvg;
    }

    public float getLightMax() {
        return lightMax;
    }

    public void setLightMax(float lightMax) {
        this.lightMax = lightMax;
    }

    public Date getCollectingStartTime() {
        return collectingStartTime;
    }

    public void setCollectingStartTime(Date collectingStartTime) {
        this.collectingStartTime = collectingStartTime;
    }

    public Date getHandlingTime() {
        return handlingTime;
    }

    public void setHandlingTime(Date handlingTime) {
        this.handlingTime = handlingTime;
    }

    public String getSensorsTypes() {
        return sensorsTypes;
    }

    public void setSensorsTypes(String sensorsTypes) {
        this.sensorsTypes = sensorsTypes;
    }
    //endregion
}
