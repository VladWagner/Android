package com.step.home_work.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.infrastructure.Utils;

import java.text.ParseException;
import java.util.Date;

public class Patient implements Parcelable {

    private final int id;
    private String surname;
    private String name;
    private String patronymic;
    private Date date;
    private String address;
    private String passport;

    public Patient(int id,
                   String surname,
                   String name,
                   String patronymic,
                   Date date,
                   String address,
                   String passport) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.date = date;
        this.address = address;
        this.passport = passport;
    }


    //region Parcelable
    protected Patient(Parcel in) {
        id = in.readInt();
        surname = in.readString();
        name = in.readString();
        patronymic = in.readString();

        //Получение даты
        try {
            date = Utils.DBdateFormat.parse(in.readString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        address = in.readString();
        passport = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

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
        parcel.writeString(Utils.DBdateFormat.format(date));
        parcel.writeString(address);
        parcel.writeString(passport);
    }
    //endregion

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
    //endregion
}
