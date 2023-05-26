package com.step.home_work.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.DBHelper;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.queries.Query6;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentsRepository {

    // Рабта с БД
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public AppointmentsRepository(Context context) {

        this.dbHelper = new DBHelper(context);
        dbHelper.createDb();
    }

    //Открыть подключение к локальной БД
    public AppointmentsRepository open() {

        //db = dbHelper.getWritableDatabase();
        db = dbHelper.open();
        return this;
    }

    //Закрыть подключение к БД
    public void close(){
        dbHelper.close();
    }

    public List<Appointment> getAll() {


        List<Appointment> appointments = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");

        querySb.append(" view_appointments.id");
        querySb.append(" , view_appointments.appointment_date");
        querySb.append(" , view_appointments.doctor_surname");
        querySb.append(" , view_appointments.doctor_name");
        querySb.append(" , view_appointments.doctor_patronymic");
        querySb.append(" , view_appointments.speciality");
        querySb.append(" , view_appointments.price");
        querySb.append(" , view_appointments.percent");
        querySb.append(" , view_appointments.patient_surname");
        querySb.append(" , view_appointments.patient_name");
        querySb.append(" , view_appointments.patient_patronymic");
        querySb.append(" , view_appointments.born_date");
        querySb.append(" , view_appointments.address");
        querySb.append(" , view_appointments.passport");

        querySb.append(" from");
        querySb.append(" view_appointments");

        Cursor cursor = db.rawQuery(querySb.toString(), null);


        if (cursor.moveToFirst()) {

            //Индексы столбцов


            //Получение значений
            do {

                //Добавить объект в список
                appointments.add(readAppointmentFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return appointments;
    }//getAll


    public Appointment getById(int appointmentId) {

        Appointment appointment = null;

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" view_appointments.id");
        querySb.append(" , view_appointments.appointment_date");
        querySb.append(" , view_appointments.doctor_surname");
        querySb.append(" , view_appointments.doctor_name");
        querySb.append(" , view_appointments.doctor_patronymic");
        querySb.append(" , view_appointments.speciality");
        querySb.append(" , view_appointments.price");
        querySb.append(" , view_appointments.percent");
        querySb.append(" , view_appointments.patient_surname");
        querySb.append(" , view_appointments.patient_name");
        querySb.append(" , view_appointments.patient_patronymic");
        querySb.append(" , view_appointments.born_date");
        querySb.append(" , view_appointments.address");
        querySb.append(" , view_appointments.passport");
        querySb.append(" from");
        querySb.append(" view_appointments");
        querySb.append(" where");
        querySb.append(" view_appointments.id = ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{String.valueOf(appointmentId)});

        //Если есть значения
        if (cursor.moveToFirst()) {


            //Получить объект
            appointment = readAppointmentFromCursor(cursor);


        }

        cursor.close();
        return appointment;
    }//appointment

    //Получение объекта из курсора
    private Appointment readAppointmentFromCursor(Cursor cursor){
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex("id");
        int appointmentDateIndex = cursor.getColumnIndex("appointment_date");
        int doctorSurnameIndex = cursor.getColumnIndex("doctor_surname");
        int doctorNameIndex = cursor.getColumnIndex("doctor_name");
        int doctorPatronymicIndex = cursor.getColumnIndex("doctor_patronymic");
        int specialityIndex = cursor.getColumnIndex("speciality");
        int priceIndex = cursor.getColumnIndex("price");
        int percentIndex = cursor.getColumnIndex("percent");
        int patientSurnameIndex = cursor.getColumnIndex("patient_surname");
        int patientNameIndex = cursor.getColumnIndex("patient_name");
        int patientPatronymicIndex = cursor.getColumnIndex("patient_patronymic");
        int bornDateIndex = cursor.getColumnIndex("born_date");
        int addressIndex = cursor.getColumnIndex("address");
        int passportIndex = cursor.getColumnIndex("passport");


        //Получение значений
        int id = cursor.getInt(idIndex);

        Date appointmentDate;
        Date bornDate;

        //Получить дату приёма
        try {
            appointmentDate = Utils.DBdateFormat.parse(cursor.getString(appointmentDateIndex));
            bornDate = Utils.DBdateFormat.parse(cursor.getString(bornDateIndex));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        String doctorSurname = cursor.getString(doctorSurnameIndex);
        String doctorName = cursor.getString(doctorNameIndex);
        String doctorPatronymic = cursor.getString(doctorPatronymicIndex);
        String speciality = cursor.getString(specialityIndex);
        int price = cursor.getInt(priceIndex);
        double percent = cursor.getDouble(percentIndex);
        String patientSurname = cursor.getString(patientSurnameIndex);
        String patientName = cursor.getString(patientNameIndex);
        String patientPatronymic = cursor.getString(patientPatronymicIndex);
        String address = cursor.getString(addressIndex);
        String passport = cursor.getString(passportIndex);

        //Создать объект
        return new Appointment(id, appointmentDate, bornDate,
                patientSurname, patientName, patientPatronymic, address,
                doctorSurname, doctorName, doctorPatronymic, speciality, passport, percent, price);
    }

    //Запрос 3
    public List<Appointment> query3(Date dateLo, Date dateHi) {


        List<Appointment> appointments = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");

        querySb.append(" view_appointments.id");
        querySb.append(" , view_appointments.appointment_date");
        querySb.append(" , view_appointments.doctor_surname");
        querySb.append(" , view_appointments.doctor_name");
        querySb.append(" , view_appointments.doctor_patronymic");
        querySb.append(" , view_appointments.speciality");
        querySb.append(" , view_appointments.price");
        querySb.append(" , view_appointments.percent");
        querySb.append(" , view_appointments.patient_surname");
        querySb.append(" , view_appointments.patient_name");
        querySb.append(" , view_appointments.patient_patronymic");
        querySb.append(" , view_appointments.born_date");
        querySb.append(" , view_appointments.address");
        querySb.append(" , view_appointments.passport");

        querySb.append(" from view_appointments");

        querySb.append(" where");
        querySb.append(" view_appointments.appointment_date between ? and ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{
                Utils.DBdateFormat.format(dateLo),Utils.DBdateFormat.format(dateHi)
        });

        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                appointments.add(readAppointmentFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        return appointments;
    }//query3

    //Запрос 6
    public List<Query6> query6() {

        List<Query6> result = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");

        querySb.append(" view_appointments.appointment_date as appointmentDate,");
        querySb.append(" count(*) as amount,");
        querySb.append(" max(view_appointments.price) as maxPrice");

        querySb.append(" from");
        querySb.append(" view_appointments");

        querySb.append(" group by");
        querySb.append(" view_appointments.appointment_date");

        Cursor cursor = db.rawQuery(querySb.toString(), null);

        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Индексы столбцов
                int appointmentDateIndex = cursor.getColumnIndex("appointmentDate");
                int amountIndex = cursor.getColumnIndex("amount");
                int maxPriceIndex = cursor.getColumnIndex("maxPrice");


                Date appointmentDate;

                //Получить дату приёма
                try {
                    appointmentDate = Utils.DBdateFormat.parse(cursor.getString(appointmentDateIndex));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                int amount = cursor.getInt(amountIndex);
                int maxPrice = cursor.getInt(maxPriceIndex);

                //Создать объект
                Query6 query6 = new Query6(appointmentDate,amount,maxPrice);

                //Добавить объект в список
                result.add(query6);

            } while (cursor.moveToNext());

        }

        return result;
    }//query6

}
