package com.step.wagner.sensors.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.step.wagner.sensors.infrastructure.Parameters;
import com.step.wagner.sensors.models.DBHelper;

import java.util.ArrayList;
import java.util.List;

//Базовый класс репозитория: организует подключение к БД, закрытие подключения,
// выборку всех записей и записей по ID
public abstract class BaseRepository<T> {

    // Рабта с БД
    private DBHelper dbHelper;
    protected SQLiteDatabase db;

    protected String tableName;

    //Строки для сырых запросов
    protected String getAllQuery;
    protected String getByIdQuery;

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

        open();

        long count = DatabaseUtils.queryNumEntries(db, tableName);

        close();

        return count;
    }

    //Закрыть подключение к БД
    public void close(){
        dbHelper.close();
    }

    //Чтение всех записей таблицы
    public List<T> getAll() {

        List<T> collection = new ArrayList<>();

        Cursor cursor = db.rawQuery(getAllQuery, null);

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

    //Чтение записи по ID
    public T getById(int id) {

        T entity = null;

        Cursor cursor = db.rawQuery(getByIdQuery, new String[]{String.valueOf(id)});

        //Если есть значения
        if (cursor.moveToFirst()) {
            //Получить объект
            entity = readFromCursor(cursor);

        }

        cursor.close();
        return entity;
    }//getById

    //Получить последнюю добавленную запись
    public T getLastAddedItem(){
        String query = String.format(
                "select * from %1$s " +
                "where %1$s._id = (select max(_id) from %1$s);",tableName);

        Cursor cursor = db.rawQuery(query,null);

        T entity = null;

        if (cursor.moveToFirst())
            entity = readFromCursor(cursor);

        cursor.close();
        return entity;
    }

    //Получить список id записей
    public List<Integer> getIdsList() {

        List<Integer> ids = new ArrayList<>();

        String getIdsQuery = String.format("select %s from %s", Parameters.ID_FIELD,tableName);

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

    //Получить объект из курсора
    abstract T readFromCursor(Cursor cursor);

}
