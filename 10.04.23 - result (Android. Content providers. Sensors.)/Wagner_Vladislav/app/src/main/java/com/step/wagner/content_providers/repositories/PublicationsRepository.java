package com.step.wagner.content_providers.repositories;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.models.entities.Publication;
import com.step.wagner.content_providers.models.entities.Subscription;
import com.step.wagner.content_providers.providers.contracts.PublicationsContract;
import com.step.wagner.content_providers.providers.contracts.SubscriptionsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicationsRepository extends BaseRepository<Publication> {

    public PublicationsRepository(ContentResolver contentResolver) {

        super(contentResolver);

    }


    //Оболоченый метод получения всех записей
    public List<Publication> getAll(){
        return super.getAll(PublicationsContract.CONTENT_URI);
    }

    //Получение подписки по id
    public Publication getById(int id){
        return super.getById(id,PublicationsContract.CONTENT_URI);
    }

    //Получаемые столбцы
    @Override
    String[] columns() {
        return new String[]{
               PublicationsContract.Columns._ID,
               PublicationsContract.Columns.PUBLICATION_TYPE,
               PublicationsContract.Columns.PUBLICATION_TYPE_ID,
               PublicationsContract.Columns.PUBLICATION_NAME,
               PublicationsContract.Columns.PUBLICATION_INDEX,
               PublicationsContract.Columns.PRICE
        };
    }

    //Получение объекта из курсора
    @Override
    Publication readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex =        cursor.getColumnIndex(PublicationsContract.Columns._ID);
        int pubTypeIndex =   cursor.getColumnIndex(PublicationsContract.Columns.PUBLICATION_TYPE);
        int pubTypeIdIndex = cursor.getColumnIndex(PublicationsContract.Columns.PUBLICATION_TYPE_ID);
        int pubNameIndex =   cursor.getColumnIndex(PublicationsContract.Columns.PUBLICATION_NAME);
        int pubIndIndex =    cursor.getColumnIndex(PublicationsContract.Columns.PUBLICATION_INDEX);
        int priceIndex =     cursor.getColumnIndex(PublicationsContract.Columns.PRICE);


        //Получение значений
        int id = cursor.getInt(idIndex);

        String publicationType =  cursor.getString(pubTypeIndex);
        int publicationTypeId =  cursor.getInt(pubTypeIdIndex);
        String publicationName =  cursor.getString(pubNameIndex);
        String publicationIndex = cursor.getString(pubIndIndex);
        int publicationUnitPrice = cursor.getInt(priceIndex);

        //Создать объект
        return new Publication(id, publicationType, publicationTypeId, publicationIndex,publicationName,publicationUnitPrice);
    }


    //Добавление
    public void insert(Publication publication){

        ContentValues cv = new ContentValues();

        cv.put("pub_index", publication.getPublicationIndex());
        cv.put("type_id", publication.getPublicationTypeId());
        cv.put("pub_name",  publication.getPublicationName());
        cv.put("unit_price",  publication.getUnitPrice());

        contentResolver.insert(PublicationsContract.CONTENT_URI,cv);
    } // insert

    //Изменение
    @SuppressLint("DefaultLocale")
    public void update(Publication publication){

        String selection = PublicationsContract.Columns._ID + " = " + publication.getId();

        ContentValues cv = new ContentValues();

        cv.put("pub_index",   publication.getPublicationIndex());
        cv.put("type_id",     publication.getPublicationTypeId());
        cv.put("pub_name",    publication.getPublicationName());
        cv.put("unit_price",  publication.getUnitPrice());


        //Маршрут с параметром - id изменяемого элемента
        Uri uriWithId = Uri.withAppendedPath(SubscriptionsContract.CONTENT_URI,String.valueOf(publication.getId()));

        contentResolver.update(uriWithId,cv,null,null);
    } // update

    //Удаление
    @SuppressLint("DefaultLocale")
    public void delete(long id){

        String where = String.format("%s = %d",PublicationsContract.Columns._ID,id);


        //Маршрут с параметром - id удаляемого элемента
        Uri uriWithId = Uri.withAppendedPath(SubscriptionsContract.CONTENT_URI,String.valueOf(id));

        contentResolver.delete(uriWithId,null,null);
    } // update
}
