package com.step.home_work.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.DBHelper;
import com.step.home_work.models.entities.Patient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientsRepository extends BaseRepository<Patient> {
    public PatientsRepository(Context context) {

        super(context,"patients");

        getAllQuery = new StringBuilder("select");
        getAllQuery.append(" *");
        getAllQuery.append(" from");
        getAllQuery.append(" view_patients");

        getByIdQuery =  new StringBuilder(getAllQuery);
        getByIdQuery.append(" where");
        getByIdQuery.append(String.format(" view_patients.%s = ?", Parameters.ID_FIELD));
    }


    //Запрос 1
    public List<Patient> query1(String surname) {


        List<Patient> patients = new ArrayList<>();

        StringBuilder querySb = new StringBuilder(getAllQuery);

        querySb.append(" where view_patients.patient_surname like ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{
                String.valueOf(surname)
        });

        //Если данные были получены
        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                patients.add(readFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return patients;
    }//query1

    //Получение объекта из курсора
    @Override
    Patient readFromCursor(Cursor cursor) {
        //Индексы столбцов
        //int idIndex = cursor.getColumnIndex("id");
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
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
