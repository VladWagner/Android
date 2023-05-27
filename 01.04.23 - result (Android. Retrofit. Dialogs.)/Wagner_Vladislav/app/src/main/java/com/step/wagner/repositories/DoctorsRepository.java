package com.step.wagner.repositories;

import android.content.Context;
import android.database.Cursor;

import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.queries.Query5;
import com.step.wagner.models.queries.Query7;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorsRepository extends BaseRepository<Doctor> {

    public DoctorsRepository(Context context) {
        super(context,"doctors");

        getAllQuery = "select * from view_doctors";

        getByIdQuery = String.format("%s where view_doctors.%s = ?", getAllQuery, Parameters.ID_FIELD);
    }

    //Получить список id
    public List<Integer> getIdsList() {

        List<Integer> ids = new ArrayList<>();

        String getIdsQuery = String.format("select %s from view_doctors", Parameters.ID_FIELD);

        Cursor cursor = db.rawQuery(getIdsQuery, null);

        //Если данные были получены
        if (cursor.moveToFirst()) {

            int idIndex = 0;

            //Получение значений
            do {

                idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);

                //Добавить объект в список
                ids.add(cursor.getInt(idIndex));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return ids;
    }//getIdsList

    //Получить список специальностей
    public List<String> getSpecialities() {

        List<String> specialities = new ArrayList<>();

        String getIdsQuery = "select specialities.name as speciality" +
                            " from specialities;";

        Cursor cursor = db.rawQuery(getIdsQuery, null);

        //Если данные были получены
        if (cursor.moveToFirst()) {

            int columnIndex = 0;

            //Получение значений
            do {

                columnIndex = cursor.getColumnIndex("speciality");

                //Добавить объект в список
                specialities.add(cursor.getString(columnIndex));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return specialities;
    }//getIdsList

    //Запрос 2 - Выбирает информацию о врачах, для которых значение в поле
    // Процент отчисления на зарплату, больше 2.3% (задавать параметром)
    public List<Doctor> query2(double percent) {

        List<Doctor> doctors = new ArrayList<>();

        String query = String.format("%s where view_doctors.percent > ?",getAllQuery);

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(percent)
        });

        //Если данные были полечены
        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                doctors.add(readFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return doctors;
    }//query2

    //Запрос 4 - Выбирает информацию о докторах, специальность которых задана параметром
    public List<Doctor> query4(String speciality) {

        List<Doctor> doctors = new ArrayList<>();

        String query = String.format("%s where view_doctors.speciality like ?",getAllQuery);

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(speciality)
        });

        //Если данные были полечены
        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                doctors.add(readFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return doctors;
    }//query4

    //Запрос 5 - Вычисляет размер заработной платы врача за каждый прием. Включает поля Фамилия врача, Имя врача, Отчество врача,
    // Специальность врача, Стоимость приема, Зарплата.
    // Сортировка по полю Специальность врача
    public List<Query5> query5() {

        List<Query5> result = new ArrayList<>();

        String query = "select " + Parameters.ID_FIELD
                + " , appointment_date"
                + " , doctor_surname"
                + " , doctor_name"
                + " , doctor_patronymic"
                + " , speciality"
                + " , price"
                + " , percent"
                + " , price * percent / 100 as salary"
                + " from view_appointments order by view_appointments.speciality";

        Cursor cursor = db.rawQuery(query, null);

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
                idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
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


    //Запрос 7 - Выполняет группировку по полю Специальность. Для каждой специальности вычисляет
    // средний Процент отчисления на зарплату от стоимости приема
    public List<Query7> query7() {

        List<Query7> result = new ArrayList<>();

        String query = "select speciality,"
                +" count(*) as amount,"
                +" avg(percent) as avgPercent"
                +" from view_appointments group by view_appointments.speciality";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            //Индексы столбцов
            int specialityIndex = 0;
            int amountIndex = 0;
            int avgPercentIndex = 0;

            //Получение значений
            do {

                //Индексы столбцов
                specialityIndex = cursor.getColumnIndex("speciality");
                amountIndex = cursor.getColumnIndex("amount");
                avgPercentIndex = cursor.getColumnIndex("avgPercent");


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
    @Override
    Doctor readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
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
