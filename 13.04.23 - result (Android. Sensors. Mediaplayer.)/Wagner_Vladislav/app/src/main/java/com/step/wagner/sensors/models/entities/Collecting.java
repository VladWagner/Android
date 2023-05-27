package com.step.wagner.sensors.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.wagner.sensors.infrastructure.AppSettings;
import com.step.wagner.sensors.infrastructure.Utils;

import java.util.Date;

//Сущность таблицы сбора данных с датчиков
public class Collecting implements Parcelable {

    private final int id;

    //Дата и время начала сбора
    private Date startDateTime;

    //Объетк акселерометра. Связь 1 к 1
    private Accelerometer accelerometer;

    //Значение датчика приближения
    private float approximation;

    //Значение датчика освещённости
    private float light;

    //Время завершения опроса
    private Date receivingTime;

    //Заданные датчики для прослушивания
    private String sensorsTypes;


    public Collecting(int id, Date startDateTime, Date receivingTime, Accelerometer accelerometer, float approximation, float light, String sensorsTypes) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.accelerometer = accelerometer;
        this.approximation = approximation;
        this.light = light;
        this.sensorsTypes = sensorsTypes;
        this.receivingTime = receivingTime;
    }

    public Collecting(int id,
                      Date startDateTime, Date receivingTime,
                      int accelerometer_id,
                      float axisX,
                      float axisY,
                      float axisZ,
                      float approximation,
                      float light,
                      String sensorsTypes) {

        this(id, startDateTime, receivingTime,
                new Accelerometer(accelerometer_id, axisX, axisY, axisZ),
                approximation, light, sensorsTypes);
    }//ctor

    //По умолчанию
    public Collecting() {
        this.id = 0;
        this.startDateTime = null;
        this.receivingTime = null;
        this.accelerometer = new Accelerometer();
        this.approximation = 0;
        this.light = 0;
        this.sensorsTypes = "";
    }//ctor

    //region Parcelable
    //Чтение объекта из parcelable коллекции
    protected Collecting(Parcel in) {
        id = in.readInt();

        //Записать значения DateTime и time
        startDateTime = Utils.tryParseDbDateTime(in.readString());
        receivingTime = Utils.tryParseTime(in.readString());

        accelerometer = in.readParcelable(Accelerometer.class.getClassLoader());
        approximation = in.readFloat();
        light = in.readFloat();
        sensorsTypes = in.readString();
    }

    public static final Creator<Collecting> CREATOR = new Creator<Collecting>() {
        @Override
        public Collecting createFromParcel(Parcel in) {
            return new Collecting(in);
        }

        @Override
        public Collecting[] newArray(int size) {
            return new Collecting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);

        //Записать значения DateTime и time
        parcel.writeString(Utils.dbDateTimeFormat.format(this.startDateTime));
        parcel.writeString(Utils.timeFormat.format(this.receivingTime));

        parcel.writeParcelable(accelerometer, i);

        parcel.writeFloat(approximation);
        parcel.writeFloat(light);
        parcel.writeString(sensorsTypes);
    }//writeToParcel
    //endregion

    public static boolean hasNull(Collecting collectingItem){
        return (collectingItem.getAccelerometer() == null && AppSettings.listenAccelerometer) || collectingItem.getStartDateTime() == null || collectingItem.getReceivingTime() == null;
    }

    //region Accessors

    public int getId() {
        return id;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Accelerometer getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(Accelerometer accelerometer) {
        this.accelerometer = accelerometer;
    }

    public float getApproximation() {
        return approximation;
    }

    public void setApproximation(float approximation) {
        this.approximation = approximation;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public Date getReceivingTime() {
        return receivingTime;
    }

    public void setReceivingTime(Date receivingTime) {
        this.receivingTime = receivingTime;
    }

    public String getSensorsTypes() {
        return sensorsTypes;
    }

    public void setSensorsTypes(String sensorsTypes) {
        this.sensorsTypes = sensorsTypes;
    }

    //endregion
}
