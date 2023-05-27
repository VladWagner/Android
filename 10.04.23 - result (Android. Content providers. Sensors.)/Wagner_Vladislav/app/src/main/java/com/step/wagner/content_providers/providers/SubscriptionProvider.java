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

import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.models.DBHelper;
import com.step.wagner.content_providers.providers.contracts.SubscriptionsContract;

public class SubscriptionProvider extends ContentProvider implements AutoCloseable {

    //Класс для работы с БД
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    //Составитель маршрутов
    private static final UriMatcher uriMatcher = createUriMatcher();

    public static final int SUBSCRIPTIONS = 10;
    public static final int SUBSCRIPTION_ID = 11;

    //Задать маршруты
    private static UriMatcher createUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Маршрут для получения всех записей
        matcher.addURI(SubscriptionsContract.PROVIDER_ADDRESS,SubscriptionsContract.VIEW_NAME,SUBSCRIPTIONS);

        //Маршрут для получения одной записи
        matcher.addURI(SubscriptionsContract.PROVIDER_ADDRESS,
                SubscriptionsContract.VIEW_NAME + "/#",SUBSCRIPTION_ID);

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
            case SUBSCRIPTIONS:
                queryBuilder.setTables(SubscriptionsContract.VIEW_NAME);
                break;
            case SUBSCRIPTION_ID:
                queryBuilder.setTables(SubscriptionsContract.VIEW_NAME);

                long id = SubscriptionsContract.getSubscriptionId(uri);

                queryBuilder.appendWhere(SubscriptionsContract.Columns._ID + "=" + id);

                break;
        }



        return queryBuilder.query(database,columns,selection,selectionArgs,null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int routeMatch = uriMatcher.match(uri);

        switch (routeMatch){
            case SUBSCRIPTIONS:
                return SubscriptionsContract.CONTENT_COLLECTION_TYPE;
            case SUBSCRIPTION_ID:
                return SubscriptionsContract.CONTENT_ITEM_TYPE;
            default:
                throw new RuntimeException("Задан неизвестный uri. Subscriptions provider.");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int routeMatch = uriMatcher.match(uri);

        long insertedItemId;

        if (routeMatch == SUBSCRIPTIONS){
            insertedItemId = database.insert(SubscriptionsContract.TABLE_NAME,null,contentValues);

            if (insertedItemId <= 0)
                throw new RuntimeException("Добавить запись подписки не удалось!");
        }else
            throw new RuntimeException("Задан неизвестный uri. Subscriptions provider.");

        return SubscriptionsContract.buildSubscriptionUri(insertedItemId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int routeMatch = uriMatcher.match(uri);

        String selectionCondition;

        if (routeMatch != SUBSCRIPTION_ID && routeMatch != SUBSCRIPTIONS)
            throw new RuntimeException("Задан неизвестный uri. Subscriptions provider.");

        if(routeMatch != SUBSCRIPTION_ID)
            return 0;

        //Получение id удаляемого элемента из переданного uri
        long itemId = SubscriptionsContract.getSubscriptionId(uri);

        //Условие выборки по id
        selectionCondition = SubscriptionsContract.Columns._ID + "=" + itemId;

        //Если кроме удаления по id заданы ещё условия удаления
        if (selection != null && !selection.isBlank())
            selectionCondition += String.format(" and(%s)",selection);

        return database.delete(SubscriptionsContract.TABLE_NAME,selectionCondition,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int routeMatch = uriMatcher.match(uri);

        String selectionCondition;

        if (routeMatch != SUBSCRIPTION_ID && routeMatch != SUBSCRIPTIONS)
            throw new RuntimeException("Задан неизвестный uri. Subscriptions provider.");

        if(routeMatch != SUBSCRIPTION_ID)
            return 0;

        //Получение id удаляемого элемента из переданного uri
        long itemId = SubscriptionsContract.getSubscriptionId(uri);

        //Условие выборки по id
        selectionCondition = SubscriptionsContract.Columns._ID + "=" + itemId;

        //Если кроме изменения записи по id заданы другие условия поиска редактируемого элемета
        if (selection != null && !selection.isBlank())
            selectionCondition += String.format(" and(%s)",selection);

        return database.update(SubscriptionsContract.TABLE_NAME,contentValues,selectionCondition,selectionArgs);
    }

    //Закрыть подключение к БД
    @Override
    public void close() throws Exception {
        if (dbHelper != null)
            dbHelper.close();
    }
}
