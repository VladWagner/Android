package com.step.home_work.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.DBHelper;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.queries.Query5;
import com.step.home_work.models.queries.Query7;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorsRepository {

    // Рабта с БД
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DoctorsRepository(Context context) {
        this.dbHelper = new DBHelper(context);
        dbHelper.createDb();
    }

    //Открыть подключение к БД
    public DoctorsRepository open() {

        //db = dbHelper.getWritableDatabase();
        db = dbHelper.open();
        return this;
    }

    //Закрыть подключение к БД
    public void close(){
        dbHelper.close();
    }

    public List<Doctor> getAll() {

        List<Doctor> doctors = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" *");
        querySb.append(" from");
        querySb.append(" view_doctors");

        Cursor cursor = db.rawQuery(querySb.toString(), null);


        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                doctors.add(readDoctorFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        return doctors;
    }//getAll

    public Doctor getById(int doctorId) {

        Doctor doctor = null;

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" *");
        querySb.append(" from");
        querySb.append(" view_doctors");

        querySb.append(" where");
        querySb.append(" view_doctors.id = ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{String.valueOf(doctorId)});

        //Если есть значения
        if (cursor.moveToFirst()) {


            //Получить объект
            doctor = readDoctorFromCursor(cursor);


        }

        cursor.close();
        return doctor;
    }//getById

    //Запрос 2
    public List<Doctor> query2(double percent) {

        List<Doctor> doctors = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" *");

        querySb.append(" from");
        querySb.append(" view_doctors");

        querySb.append(" where");
        querySb.append(" view_doctors.percent > ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{
                String.valueOf(percent)
        });

        //Если данные были полечены
        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                doctors.add(readDoctorFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return doctors;
    }//query2

    //Запрос 4
    public List<Doctor> query4(String speciality) {

        List<Doctor> doctors = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" *");

        querySb.append(" from");
        querySb.append(" view_doctors");

        querySb.append(" where view_doctors.speciality like ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{
                String.valueOf(speciality)
        });

        //Если данные были полечены
        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                doctors.add(readDoctorFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return doctors;
    }//query4

    //Запрос 5
    public List<Query5> query5() {

        List<Query5> result = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");

        querySb.append(" view_appointments.id");
        querySb.append(" , view_appointments.appointment_date");
        querySb.append(" , view_appointments.doctor_surname");
        querySb.append(" , view_appointments.doctor_name");
        querySb.append(" , view_appointments.doctor_patronymic");
        querySb.append(" , view_appointments.speciality");
        querySb.append(" , view_appointments.price");
        querySb.append(" , view_appointments.percent");
        querySb.append(" , view_appointments.price * percent / 100 as salary");

        querySb.append(" from");
        querySb.append(" view_appointments");

        querySb.append(" group by");
        querySb.append(" view_appointments.speciality");

        Cursor cursor = db.rawQuery(querySb.toString(), null);

        //Получение значений из курсора
        if (cursor.moveToFirst()) {

            int idIndex = 0;
            int appointmentDateIndex = 0;
            int doctorSurnameIndex = 0;
            int doctorNameIndex = 0;
            int doctorPatronymicIndex = 0;
            int specialityIndex = 0;
            int priceIndex = 0;
            int percentIndex = 0;
            int salaryIndex = 0;

            //Получение значений
            do {

                //Индексы столбцов
                idIndex = cursor.getColumnIndex("id");
                appointmentDateIndex = cursor.getColumnIndex("appointment_date");
                doctorSurnameIndex = cursor.getColumnIndex("doctor_surname");
                doctorNameIndex = cursor.getColumnIndex("doctor_name");
                doctorPatronymicIndex = cursor.getColumnIndex("doctor_patronymic");
                specialityIndex = cursor.getColumnIndex("speciality");
                priceIndex = cursor.getColumnIndex("price");
                percentIndex = cursor.getColumnIndex("percent");
                salaryIndex = cursor.getColumnIndex("salary");


                int id = cursor.getInt(idIndex);
                Date appointmentDate;

                //Получить дату приёма
                try {
                    appointmentDate = Utils.DBdateFormat.parse(cursor.getString(appointmentDateIndex));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                String doctorSurname = cursor.getString(doctorSurnameIndex);
                String doctorName = cursor.getString(doctorNameIndex);
                String doctorPatronymic = cursor.getString(doctorPatronymicIndex);
                String speciality = cursor.getString(specialityIndex);
                int price = cursor.getInt(priceIndex);
                double percent = cursor.getDouble(percentIndex);
                double salary = cursor.getDouble(salaryIndex);

                //Создать объект
                Query5 query5 = new Query5(id, appointmentDate, doctorSurname, doctorName, doctorPatronymic, speciality, price, percent, salary);

                //Добавить объект в список
                result.add(query5);

            } while (cursor.moveToNext());

        }//if

        cursor.close();
        return result;
    }//query5


    //Запрос 7
    public List<Query7> query7() {

        List<Query7> result = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");

        querySb.append(" view_appointments.speciality,");
        querySb.append(" count(*) as amount,");
        querySb.append(" avg(view_appointments.percent) as avgPercent");

        querySb.append(" from view_appointments");

        querySb.append(" group by view_appointments.speciality");

        Cursor cursor = db.rawQuery(querySb.toString(), null);

        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Индексы столбцов
                int specialityIndex = cursor.getColumnIndex("speciality");
                int amountIndex = cursor.getColumnIndex("amount");
                int avgPercentIndex = cursor.getColumnIndex("avgPercent");


                String speciality = cursor.getString(specialityIndex);
                int amount = cursor.getInt(amountIndex);
                double avgPrice = cursor.getDouble(avgPercentIndex);

                //Создать объект
                Query7 query7 = new Query7(speciality,amount,avgPrice);

                //Добавить объект в список
                result.add(query7);

            } while (cursor.moveToNext());

        }

        cursor.close();
        return result;
    }//query7

    //Получение объекта из курсора
    private Doctor readDoctorFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex("id");
        int doctorSurnameIndex = cursor.getColumnIndex("doctor_surname");
        int doctorNameIndex = cursor.getColumnIndex("doctor_name");
        int doctorPatronymicIndex = cursor.getColumnIndex("doctor_patronymic");
        int specialityIndex = cursor.getColumnIndex("speciality");
        int priceIndex = cursor.getColumnIndex("price");
        int percentIndex = cursor.getColumnIndex("percent");

        //Получение значений
        int id = cursor.getInt(idIndex);

        String doctorSurname = cursor.getString(doctorSurnameIndex);
        String doctorName = cursor.getString(doctorNameIndex);
        String doctorPatronymic = cursor.getString(doctorPatronymicIndex);
        String speciality = cursor.getString(specialityIndex);
        int price = cursor.getInt(priceIndex);
        double percent = cursor.getDouble(percentIndex);

        //Создать объект
        return new Doctor(id, doctorSurname, doctorName, doctorPatronymic, speciality, percent, price);
    }
}
