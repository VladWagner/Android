package com.step.wagner.content_providers.repositories;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.providers.contracts.SubscriptionsContract;

import java.util.ArrayList;
import java.util.List;

//Базовый класс репозитория: организует подключение к БД, закрытие подключения,
// выборку всех записей и записей по ID
public abstract class BaseRepository<T> {

    protected ContentResolver contentResolver;
    public BaseRepository(ContentResolver resolver) {

        contentResolver = resolver;

    }//ctor

    //Открыть подключение к локальной БД
    public BaseRepository<T> open() {

        return this;
    }

    //Задать массив названий столбцов
    abstract String[] columns();

    //Чтение всех записей таблицы
    public List<T> getAll(Uri uri) {

        List<T> collection = new ArrayList<>();

        Cursor cursor = contentResolver.query(uri,
                columns(),
                null,
                null,
                Parameters.ID_FIELD);

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
    public T getById(int id, Uri uri) {

        T entity = null;

        String selection = Parameters.ID_FIELD + " = " + id;

        //Создать маршрут с ID
        Uri uriWithId = Uri.withAppendedPath(uri,String.valueOf(id));

        Cursor cursor = contentResolver.query(uriWithId/*uri*/,
                columns(),
                /*selection*/null,
                /*new String[]{String.valueOf(id)}*/null,
                SubscriptionsContract.Columns._ID);

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
