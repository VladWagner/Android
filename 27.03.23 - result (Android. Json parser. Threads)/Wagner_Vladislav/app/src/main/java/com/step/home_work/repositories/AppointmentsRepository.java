package com.step.home_work.repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.DBHelper;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.queries.Query6;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentsRepository extends BaseRepository<Appointment> {

    public AppointmentsRepository(Context context) {

        super(context, Parameters.APPOINTMENTS);

        //Задать строки для сырых запросов
        getAllQuery = new StringBuilder("select ");

        getAllQuery.append(Parameters.ID_FIELD);
        getAllQuery.append(" , view_appointments.appointment_date");
        getAllQuery.append(" , view_appointments.doctor_id");
        getAllQuery.append(" , view_appointments.doctor_surname");
        getAllQuery.append(" , view_appointments.doctor_name");
        getAllQuery.append(" , view_appointments.doctor_patronymic");
        getAllQuery.append(" , view_appointments.speciality");
        getAllQuery.append(" , view_appointments.price");
        getAllQuery.append(" , view_appointments.percent");
        getAllQuery.append(" , view_appointments.patient_id");
        getAllQuery.append(" , view_appointments.patient_surname");
        getAllQuery.append(" , view_appointments.patient_name");
        getAllQuery.append(" , view_appointments.patient_patronymic");
        getAllQuery.append(" , view_appointments.born_date");
        getAllQuery.append(" , view_appointments.address");
        getAllQuery.append(" , view_appointments.passport");

        getAllQuery.append(" from");
        getAllQuery.append(" view_appointments");

        //Запрос для получения элемента по id
        getByIdQuery = new StringBuilder(getAllQuery);
        getByIdQuery.append(" where");
        getByIdQuery.append(String.format(" view_appointments.%s = ?",Parameters.ID_FIELD));
    }


    //Получение объекта из курсора
    @Override
    Appointment readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
        int appointmentDateIndex = cursor.getColumnIndex("appointment_date");
        int doctorIdIndex = cursor.getColumnIndex("doctor_id");
        int doctorSurnameIndex = cursor.getColumnIndex("doctor_surname");
        int doctorNameIndex = cursor.getColumnIndex("doctor_name");
        int doctorPatronymicIndex = cursor.getColumnIndex("doctor_patronymic");
        int specialityIndex = cursor.getColumnIndex("speciality");
        int priceIndex = cursor.getColumnIndex("price");
        int percentIndex = cursor.getColumnIndex("percent");
        int patientIdIndex = cursor.getColumnIndex("patient_id");
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


        int doctorId = cursor.getInt(doctorIdIndex);
        String doctorSurname = cursor.getString(doctorSurnameIndex);
        String doctorName = cursor.getString(doctorNameIndex);
        String doctorPatronymic = cursor.getString(doctorPatronymicIndex);
        String speciality = cursor.getString(specialityIndex);
        int price = cursor.getInt(priceIndex);
        double percent = cursor.getDouble(percentIndex);

        int patientId = cursor.getInt(patientIdIndex);
        String patientSurname = cursor.getString(patientSurnameIndex);
        String patientName = cursor.getString(patientNameIndex);
        String patientPatronymic = cursor.getString(patientPatronymicIndex);
        String address = cursor.getString(addressIndex);
        String passport = cursor.getString(passportIndex);

        //Создать объект
        return new Appointment(id, appointmentDate, patientId,
                patientSurname, patientName, patientPatronymic, address, bornDate,doctorId,
                doctorSurname, doctorName, doctorPatronymic, speciality, passport, percent, price);
    }

    //Запрос 3
    public List<Appointment> query3(Date dateLo, Date dateHi) {

        List<Appointment> appointments = new ArrayList<>();

        StringBuilder querySb = new StringBuilder(getAllQuery);

        querySb.append(" where");
        querySb.append(" view_appointments.appointment_date between ? and ?");

        Cursor cursor = db.rawQuery(querySb.toString(), new String[]{
                Utils.DBdateFormat.format(dateLo),Utils.DBdateFormat.format(dateHi)
        });

        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                appointments.add(readFromCursor(cursor));

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

    //Добавление
    public long insert(Appointment appointment){

        ContentValues cv = new ContentValues();

        cv.put("appointment_date", Utils.DBdateFormat.format(appointment.getAppointmentDate()));
        cv.put("id_doctor", appointment.getDoctor().getId());
        cv.put("id_patient", appointment.getPatient().getId());

        return db.insert(tableName, null, cv);
    } // insert

    //Изменение
    @SuppressLint("DefaultLocale")
    public long update(Appointment appointment){

        String where = String.format("%s = %d",Parameters.ID_FIELD,appointment.getId());
        ContentValues cv = new ContentValues();

        cv.put("appointment_date", Utils.DBdateFormat.format(appointment.getAppointmentDate()));
        cv.put("id_doctor", appointment.getDoctor().getId());
        cv.put("id_patient", appointment.getPatient().getId());

        return db.update(tableName, cv,where,null);
    } // update

    //Удаление
    @SuppressLint("DefaultLocale")
    public long delete(long id){

        String where = String.format("%s = %d",Parameters.ID_FIELD,id);

        return db.delete(tableName,where,null);
    } // update
}
