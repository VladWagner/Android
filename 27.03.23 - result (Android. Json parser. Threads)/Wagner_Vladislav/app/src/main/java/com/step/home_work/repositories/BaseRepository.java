package com.step.home_work.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.step.home_work.models.DBHelper;
import com.step.home_work.models.entities.Appointment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T> {

    // Рабта с БД
    private DBHelper dbHelper;
    protected SQLiteDatabase db;

    protected String tableName;

    //Сырые запросы
    protected StringBuilder getAllQuery;
    protected StringBuilder getByIdQuery;

    public BaseRepository(Context context, String tableName) {
        this.dbHelper = new DBHelper(context);
        dbHelper.createDb();

        this.tableName = tableName;
    }//ctor

    //Открыть подключение к локальной БД
    public BaseRepository<T> open() {

        db = dbHelper.open();
        return this;
    }

    //Количество элементов
    public long getCount() {
        return DatabaseUtils.queryNumEntries(db, tableName);
    }

    //Закрыть подключение к БД
    public void close(){
        dbHelper.close();
    }

    //Получение всей коллекции
    public List<T> getAll() {

        List<T> collection = new ArrayList<>();

        Cursor cursor = db.rawQuery(getAllQuery.toString(), null);

        if (cursor.moveToFirst()) {

            //Получение значений
            do {

                //Добавить объект в список
                collection.add(readFromCursor(cursor));

            } while (cursor.moveToNext());

        }

        cursor.close();
        return collection;
    }//getAll

    public T getById(int appointmentId) {

        T entity = null;

        Cursor cursor = db.rawQuery(getByIdQuery.toString(), new String[]{String.valueOf(appointmentId)});

        //Если есть значения
        if (cursor.moveToFirst()) {
            //Получить объект
            entity = readFromCursor(cursor);

        }

        cursor.close();
        return entity;
    }//getById

    //Получить объект из курсора
    abstract T readFromCursor(Cursor cursor);

}
