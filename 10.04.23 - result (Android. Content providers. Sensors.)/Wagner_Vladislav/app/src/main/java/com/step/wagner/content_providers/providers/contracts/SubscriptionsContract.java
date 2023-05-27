package com.step.wagner.content_providers.providers.contracts;

import android.content.ContentUris;
import android.net.Uri;

public class SubscriptionsContract {
    //Названия табилицы и представления
    public static final String VIEW_NAME = "view_subscriptions";
    public static final String TABLE_NAME = "subscriptions";

    //Название и маршрут к провайдеру
    public static final String PROVIDER_ADDRESS = "com.step.wagner.content_providers.subscriptions";
    public static final Uri PROVIDER_ADDRESS_URI = Uri.parse("content://" + PROVIDER_ADDRESS);

    //Определение типа возвращаемых данных
    public static final String CONTENT_COLLECTION_TYPE =
              String.format("vnd.android.cursor.dir/vnd.%s.%s",PROVIDER_ADDRESS,VIEW_NAME);
    public static final String CONTENT_ITEM_TYPE =
            String.format("vnd.android.cursor.item/vnd.%s.%s",PROVIDER_ADDRESS,VIEW_NAME);

    //Формирование адреса провадера для конкретной таблицы/представления

    //content://com.step.wagner.content_providers/view_subscriptions
    public static final Uri CONTENT_URI = Uri.withAppendedPath(PROVIDER_ADDRESS_URI,VIEW_NAME);

    //Адрес провайдера для конкретной таблицы с добавлением id приложения
    public static Uri buildSubscriptionUri(long appId){
        return ContentUris.withAppendedId(CONTENT_URI,appId);
    }

    //Получить id приложения из URI
    public static long getSubscriptionId(Uri uri){
        return ContentUris.parseId(uri);
    }//getSubscriptionId


    public static class Columns{
        public static final String _ID = "_id";
        public static final String PUBLICATION_ID = "publication_id";
        public static final String PUBLICATION_TYPE = "pub_type";
        public static final String PUBLICATION_INDEX = "pub_index";
        public static final String PUBLICATION_NAME = "pub_name";
        public static final String PRICE = "unit_price";
        public static final String DATE_START = "date_start";
        public static final String DURATION = "duration";

        private Columns(){ }
    } //  class Columns

}
