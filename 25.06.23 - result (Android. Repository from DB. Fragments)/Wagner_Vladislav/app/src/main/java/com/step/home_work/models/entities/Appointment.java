package com.step.home_work.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.infrastructure.Utils;

import java.text.ParseException;
import java.util.Date;

public class Appointment implements Parcelable{

    private final int id;

    private Date date;
    private String patientSurname;
    private String patientName;
    private String patientPatronymic;
    private Date dob;
    private String address;
    private String doctorSurname;
    private String doctorName;
    private String doctorPatronymic;
    private String speciality;
    private String passport;
    private double percent;
    private int price;

    public Appointment(int id,
                       Date date,
                       Date dob,
                       String patientSurname,
                       String patientName,
                       String patientPatronymic,
                       String address,
                       String doctorSurname,
                       String doctorName,
                       String doctorPatronymic,
                       String speciality,
                       String passport,
                       double percent,
                       int price) {
        this.id = id;
        this.date = date;
        this.patientSurname = patientSurname;
        this.patientName = patientName;
        this.patientPatronymic = patientPatronymic;
        this.dob = dob;
        this.address = address;
        this.doctorSurname = doctorSurname;
        this.doctorName = doctorName;
        this.doctorPatronymic = doctorPatronymic;
        this.speciality = speciality;
        this.passport = passport;
        this.percent = percent;
        this.price = price;
    }


    protected Appointment(Parcel in) {
        id = in.readInt();

        //Получить даты
        try {
            date = Utils.DBdateFormat.parse(in.readString());
            dob = Utils.DBdateFormat.parse(in.readString());
        } catch (ParseException e) {
            throw new RuntimeException(String.format("Исключение при создании объекта appointment \n"));
        }

        patientSurname = in.readString();
        patientName = in.readString();
        patientPatronymic = in.readString();
        address = in.readString();
        doctorSurname = in.readString();
        doctorName = in.readString();
        doctorPatronymic = in.readString();
        speciality = in.readString();
        passport = in.readString();
        percent = in.readDouble();
        price = in.readInt();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

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

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPatronymic() {
        return patientPatronymic;
    }

    public void setPatientPatronymic(String patientPatronymic) {
        this.patientPatronymic = patientPatronymic;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    //endregion


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);

        //Записать дату
        parcel.writeString(Utils.DBdateFormat.format(date));
        parcel.writeString(Utils.DBdateFormat.format(dob));

        parcel.writeString(patientSurname);
        parcel.writeString(patientName);
        parcel.writeString(patientPatronymic);
        parcel.writeString(address);
        parcel.writeString(doctorSurname);
        parcel.writeString(doctorName);
        parcel.writeString(doctorPatronymic);
        parcel.writeString(speciality);
        parcel.writeString(passport);
        parcel.writeDouble(percent);
        parcel.writeInt(price);
    }
}
