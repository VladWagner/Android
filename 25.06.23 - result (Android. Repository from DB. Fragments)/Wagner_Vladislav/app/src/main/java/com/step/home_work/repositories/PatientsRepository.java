package com.step.home_work.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.DBHelper;
import com.step.home_work.models.entities.Patient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientsRepository {

    // Рабта с БД
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public PatientsRepository(Context context) {

        this.dbHelper = new DBHelper(context);
        dbHelper.createDb();
    }

    //Открыть подключение к БД
    public PatientsRepository open(){

        //db = dbHelper.getReadableDatabase();
        db = dbHelper.open();
        return this;
    }


    //Закрыть подключение к БД
    public void close(){
        dbHelper.close();
    }

    public List<Patient> getAll() {

        List<Patient> patients = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" *");
        querySb.append(" from");
        querySb.append(" view_patients");

        Cursor cursor = db.rawQuery(querySb.toString(), null);


        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                patients.add(readPatientFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return patients;
    }//getAll


    //Получить запись по id
    public Patient getById(int patientId) {

        Patient patient = null;

        StringBuilder querySb = new StringBuilder("select");
        querySb.append(" *");
        querySb.append(" from");
        querySb.append(" view_patients");

        querySb.append(" where");
        querySb.append(" view_patients.id = ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{String.valueOf(patientId)});

        //Если есть значения
        if (cursor.moveToFirst()) {

            //Получить объект
            patient = readPatientFromCursor(cursor);

        }

        cursor.close();
        return patient;
    }//getById


    //Запрос 1
    public List<Patient> query1(String surname) {


        List<Patient> patients = new ArrayList<>();

        StringBuilder querySb = new StringBuilder("select ");
        querySb.append("*");

        querySb.append(" from view_patients");

        querySb.append(" where view_patients.patient_surname like ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{
                String.valueOf(surname)
        });

        //Если данные были получены
        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                patients.add(readPatientFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return patients;
    }//query1

    //Получение объекта из курсора
    private Patient readPatientFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex("id");
        int patientSurnameIndex = cursor.getColumnIndex("patient_surname");
        int patientNameIndex = cursor.getColumnIndex("patient_name");
        int patientPatronymicIndex = cursor.getColumnIndex("patient_patronymic");
        int bornDateIndex = cursor.getColumnIndex("born_date");
        int addressIndex = cursor.getColumnIndex("address");
        int passportIndex = cursor.getColumnIndex("passport");

        //Получение значений
        int id = cursor.getInt(idIndex);
        String patientSurname = cursor.getString(patientSurnameIndex);
        String patientName = cursor.getString(patientNameIndex);
        String patientPatronymic = cursor.getString(patientPatronymicIndex);
        String address = cursor.getString(addressIndex);
        String passport = cursor.getString(passportIndex);

        Date bornDate;

        //Получить дату приёма
        try {
            bornDate = Utils.DBdateFormat.parse(cursor.getString(bornDateIndex));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Создать объект
        return new Patient(id, patientSurname, patientName, patientPatronymic, bornDate, address, passport);
    }


}
