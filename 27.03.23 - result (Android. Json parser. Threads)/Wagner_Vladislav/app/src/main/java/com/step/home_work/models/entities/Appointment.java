package com.step.home_work.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.step.home_work.infrastructure.Utils;

import java.text.ParseException;
import java.util.Date;

public class Appointment implements Parcelable{

    private final int id;

    private Date appointmentDate;

    //Пациент
    private Patient patient;

    private int patient_id;

    //Доктор
    private Doctor doctor;
    private int doctor_id;

    public Appointment(int id,
                       Date appointmentDate,
                       int patientId,
                       String patientSurname,
                       String patientName,
                       String patientPatronymic,
                       String address,
                       Date dob,
                       int doctorId,
                       String doctorSurname,
                       String doctorName,
                       String doctorPatronymic,
                       String speciality,
                       String passport,
                       double percent,
                       int price) {
        this.id = id;
        this.appointmentDate = appointmentDate;

        this.patient = new Patient(patientId,patientSurname,patientName,patientPatronymic,dob,address,passport);
        this.doctor = new Doctor(doctorId,doctorSurname,doctorName,doctorPatronymic,speciality,percent,price);
    }

    public Appointment(int id, Date appointmentDate, Patient patient, Doctor doctor) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.patient = patient;
        this.doctor = doctor;
    }

    //ctor с получением id справочных сущностей
    public Appointment(int id, Date appointmentDate, int patient_id, int doctor_id) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
    }

    protected Appointment(Parcel in) {
        id = in.readInt();

        //Здесь задать переменную, которая потом будет писаться в ctor patient

        //Получить даты
        try {
            appointmentDate = Utils.DBdateFormat.parse(in.readString());
        } catch (ParseException e) {
            throw new RuntimeException(String.format("Исключение при создании объекта appointment \n"));
        }


        patient = in.readParcelable(Patient.class.getClassLoader());

        doctor = in.readParcelable(Doctor.class.getClassLoader());
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {

        this.appointmentDate = appointmentDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
        parcel.writeString(Utils.DBdateFormat.format(appointmentDate));
        parcel.writeParcelable(patient,i);
        parcel.writeParcelable(doctor,i);
    }
}
