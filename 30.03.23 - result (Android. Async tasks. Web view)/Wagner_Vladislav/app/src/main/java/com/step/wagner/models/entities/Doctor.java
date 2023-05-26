package com.step.wagner.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Doctor implements Parcelable{

    private final int id;
    private String surname;
    private String name;
    private String patronymic;
    private String speciality;
    private double percent;
    private int payment;

    public Doctor(int id, String surname, String name, String patronymic, String speciality, double percent, int payment) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.percent = percent;
        this.payment = payment;
    }

    protected Doctor(Parcel in) {
        id =         in.readInt();
        surname =    in.readString();
        name =       in.readString();
        patronymic = in.readString();
        speciality = in.readString();
        percent =    in.readDouble();
        payment =    in.readInt();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };


    //region Accessors
    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
    //endregion


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(surname);
        parcel.writeString(name);
        parcel.writeString(patronymic);
        parcel.writeString(speciality);
        parcel.writeDouble(percent);
        parcel.writeInt(payment);
    }
}
