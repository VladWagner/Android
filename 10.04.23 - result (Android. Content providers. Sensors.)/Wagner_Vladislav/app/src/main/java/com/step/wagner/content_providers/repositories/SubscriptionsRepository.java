package com.step.wagner.content_providers.repositories;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.models.entities.Subscription;
import com.step.wagner.content_providers.providers.contracts.SubscriptionsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubscriptionsRepository extends BaseRepository<Subscription> {

    public SubscriptionsRepository(ContentResolver contentResolver) {

        super(contentResolver);
    }

    //Получение всех записей
    public List<Subscription> getAll(){
        return super.getAll(SubscriptionsContract.CONTENT_URI);
    }

    //Получение подписки по id
    public Subscription getById(int id){
        return super.getById(id,SubscriptionsContract.CONTENT_URI);
    }

    @Override
    String[] columns() {
        return new String[]{
                SubscriptionsContract.Columns._ID,
                SubscriptionsContract.Columns.PUBLICATION_ID,
                SubscriptionsContract.Columns.PUBLICATION_TYPE,
                SubscriptionsContract.Columns.PUBLICATION_NAME,
                SubscriptionsContract.Columns.PUBLICATION_INDEX,
                SubscriptionsContract.Columns.PRICE,
                SubscriptionsContract.Columns.DATE_START,
                SubscriptionsContract.Columns.DURATION
        };
    }


    //Получение объекта из курсора
    @Override
    Subscription readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex =        cursor.getColumnIndex(Parameters.ID_FIELD);
        int pubIdIndex =     cursor.getColumnIndex(SubscriptionsContract.Columns.PUBLICATION_ID);
        int pubTypeIndex =   cursor.getColumnIndex(SubscriptionsContract.Columns.PUBLICATION_TYPE);
        int pubNameIndex =   cursor.getColumnIndex(SubscriptionsContract.Columns.PUBLICATION_NAME);
        int pubIndIndex =    cursor.getColumnIndex(SubscriptionsContract.Columns.PUBLICATION_INDEX);
        int priceIndex =     cursor.getColumnIndex(SubscriptionsContract.Columns.PRICE);
        int dateStartIndex = cursor.getColumnIndex(SubscriptionsContract.Columns.DATE_START);
        int durationIndex =  cursor.getColumnIndex(SubscriptionsContract.Columns.DURATION);


        //Получение значений
        int id = cursor.getInt(idIndex);

        //Дата начала подписки
        Date startDate = Utils.tryParseDBDate(cursor.getString(dateStartIndex));

        //Издание
        int publicationId = cursor.getInt(pubIdIndex);
        String publicationType = cursor.getString(pubTypeIndex);
        String publicationName = cursor.getString(pubNameIndex);
        String publicationIndex = cursor.getString(pubIndIndex);
        int publicationUnitPrice = cursor.getInt(priceIndex);

        //Длительность подписки
        int duration = cursor.getInt(durationIndex);


        //Создать объект
        return new Subscription(id,publicationId, publicationType, publicationIndex,
                publicationName, publicationUnitPrice,
                startDate,duration);
    }


    //Добавление
    public void insert(Subscription subscription){

        ContentValues cv = new ContentValues();

        cv.put("publication_id", subscription.getPublicationId());
        cv.put("date_start", Utils.DBdateFormat.format(subscription.getDateStart()));
        cv.put("duration",  subscription.getDuration());

       contentResolver.insert(SubscriptionsContract.CONTENT_URI,cv);
    } // insert

    //Изменение
    @SuppressLint("DefaultLocale")
    public void update(Subscription subscription){
        String selection = SubscriptionsContract.Columns._ID + " = ?";

        ContentValues cv = new ContentValues();

        cv.put("publication_id", subscription.getPublicationId());
        cv.put("date_start", Utils.DBdateFormat.format(subscription.getDateStart()));
        cv.put("duration",  subscription.getDuration());

        //Маршрут с параметром - id изменяемого элемента
        Uri uriWithId = Uri.withAppendedPath(SubscriptionsContract.CONTENT_URI,String.valueOf(subscription.getId()));

        //contentResolver.update(SubscriptionsContract.CONTENT_URI,cv,selection,new String[]{String.valueOf(subscription.getId())});
        contentResolver.update(uriWithId,cv,null,null);
    } // update

    //Удаление
    @SuppressLint("DefaultLocale")
    public void delete(long id){

        String where = String.format("%s = %d",SubscriptionsContract.Columns._ID,id);

        Uri uriWithId = Uri.withAppendedPath(SubscriptionsContract.CONTENT_URI,String.valueOf(id));

        contentResolver.delete(uriWithId,null,null);
    } // delete

    public void delete(Subscription subscription){

        delete(subscription.getId());
    } // delete
}
