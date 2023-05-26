package com.step.home_work.models.queries;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.infrastructure.Utils;

import java.text.ParseException;
import java.util.Date;

public class Query5 implements Parcelable {

    private final int id;
    private Date date;
    private String doctorSurname;
    private String doctorName;
    private String doctorPatronymic;
    private String speciality;
    private int price;
    private double percent;
    private double salary;

    public Query5(int id,
                  Date date,
                  String doctorSurname,
                  String doctorName,
                  String doctorPatronymic,
                  String speciality,
                  int price,
                  double percent,
                  double salary) {
        this.id = id;
        this.date = date;
        this.doctorSurname = doctorSurname;
        this.doctorName = doctorName;
        this.doctorPatronymic = doctorPatronymic;
        this.speciality = speciality;
        this.price = price;
        this.percent = percent;
        this.salary = salary;
    }

    //region Parcelable
    protected Query5(Parcel in) {
        id = in.readInt();

        //Получение даты
        try {
            date = Utils.DBdateFormat.parse(in.readString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        doctorSurname = in.readString();
        doctorName = in.readString();
        doctorPatronymic = in.readString();
        speciality = in.readString();
        price = in.readInt();
        percent = in.readDouble();
        salary = in.readDouble();
    }

    public static final Creator<Query5> CREATOR = new Creator<Query5>() {
        @Override
        public Query5 createFromParcel(Parcel in) {
            return new Query5(in);
        }

        @Override
        public Query5[] newArray(int size) {
            return new Query5[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(Utils.DBdateFormat.format(date));
        parcel.writeString(doctorSurname);
        parcel.writeString(doctorName);
        parcel.writeString(doctorPatronymic);
        parcel.writeString(speciality);
        parcel.writeInt(price);
        parcel.writeDouble(percent);
        parcel.writeDouble(salary);
    }
    //endregion


    //region Accessors

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        this.doctorSurname = doctorSurname;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPatronymic() {
        return doctorPatronymic;
    }

    public void setDoctorPatronymic(String doctorPatronymic) {
        this.doctorPatronymic = doctorPatronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    //endregion
}
