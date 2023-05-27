package com.step.wagner.content_providers.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.step.wagner.content_providers.models.DBHelper;
import com.step.wagner.content_providers.providers.contracts.PublicationsContract;

public class PublicationsProvider extends ContentProvider implements AutoCloseable {

    //Класс для работы с БД
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    //Составитель маршрутов
    private static final UriMatcher uriMatcher = createUriMatcher();

    public static final int PUBLICATIONS = 20;
    public static final int PUBLICATION_ID = 21;

    //Задать маршруты
    private static UriMatcher createUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Маршрут для получения всех записей
        matcher.addURI(PublicationsContract.PROVIDER_ADDRESS,PublicationsContract.VIEW_NAME, PUBLICATIONS);

        //Маршрут для получения одной записи
        matcher.addURI(PublicationsContract.PROVIDER_ADDRESS,
                PublicationsContract.VIEW_NAME + "/#", PUBLICATION_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        dbHelper = new DBHelper(getContext());
        dbHelper.createDb();
        database = dbHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int routeMatch = uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (routeMatch){

            //Если выборка коллекции (не указан id в параметре), тогда просто задаём имя представления
            case PUBLICATIONS:
                queryBuilder.setTables(PublicationsContract.VIEW_NAME);
                break;
            case PUBLICATION_ID:
                queryBuilder.setTables(PublicationsContract.VIEW_NAME);

                //Получить id из запроса, переданного в качестве параметра в URI
                long id = PublicationsContract.getPublicationId(uri);

                queryBuilder.appendWhere(PublicationsContract.Columns._ID + "=" + id);

                break;
        }


        return queryBuilder.query(database,columns,selection,selectionArgs,null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int routeMatch = uriMatcher.match(uri);

        switch (routeMatch){
            case PUBLICATIONS:
                return PublicationsContract.CONTENT_COLLECTION_TYPE;
            case PUBLICATION_ID:
                return PublicationsContract.CONTENT_ITEM_TYPE;
            default:
                throw new RuntimeException("Задан неизвестный uri. Publications provider.");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int routeMatch = uriMatcher.match(uri);

        long insertedItemId;

        //Если обращение произошло к нужной таблицу
        if (routeMatch == PUBLICATIONS){
            insertedItemId = database.insert(PublicationsContract.TABLE_NAME,null,contentValues);

            if (insertedItemId <= 0)
                throw new RuntimeException("Добавить запись издания не удалось!");
        }else
            throw new RuntimeException("Задан неизвестный uri. Publications provider.");

        return PublicationsContract.buildPublicationUri(insertedItemId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int routeMatch = uriMatcher.match(uri);

        String selectionCondition;

        if (routeMatch != PUBLICATION_ID && routeMatch != PUBLICATIONS)
            throw new RuntimeException("Задан неизвестный uri. Publications provider.");

        if(routeMatch != PUBLICATION_ID)
            return -1;

        //Получение id удаляемого элемента из переданного uri
        long itemId = PublicationsContract.getPublicationId(uri);

        //Условие выборки по id
        selectionCondition = PublicationsContract.Columns._ID + "=" + itemId;

        //Если кроме удаления по id заданы ещё условия удаления
        if (selection != null && !selection.isBlank())
            selectionCondition += String.format(" and(%s)",selection);

        return database.delete(PublicationsContract.TABLE_NAME,selectionCondition,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int routeMatch = uriMatcher.match(uri);

        String selectionCondition;

        if (routeMatch != PUBLICATION_ID && routeMatch != PUBLICATIONS)
            throw new RuntimeException("Задан неизвестный uri. Publications provider.");

        if(routeMatch != PUBLICATION_ID)
            return -1;

        //Получение id удаляемого элемента из переданного uri
        long itemId = PublicationsContract.getPublicationId(uri);

        //Условие выборки по id
        selectionCondition = PublicationsContract.Columns._ID + "=" + itemId;

        //Если кроме изменения записи по id заданы другие условия поиска редактируемого элемета
        if (selection != null && !selection.isBlank())
            selectionCondition += String.format(" and(%s)",selection);

        return database.update(PublicationsContract.TABLE_NAME,contentValues,selectionCondition,selectionArgs);
    }

    //Закрыть подключение к БД
    @Override
    public void close() throws Exception {
        if (dbHelper != null)
            dbHelper.close();
    }
}
