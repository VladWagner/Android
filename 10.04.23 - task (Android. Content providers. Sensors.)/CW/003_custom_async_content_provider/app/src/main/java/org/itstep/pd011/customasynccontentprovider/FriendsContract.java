package org.itstep.pd011.customasynccontentprovider;

import android.content.ContentUris;
import android.net.Uri;

/*
* Первым добавим класс FriendsContract, который будет описывать основные
* значения, столбцы, адреса uri, используемые в контент-провайдере.
*
* С помощью константы TABLE_NAME определяется имя таблицы,
* к которой будет происходить обращение.
* А вложенный статический класс Columns описывает столбцы этой таблицы.
* То есть таблица будет называться "friends", а столбцы - "_id", "Name",
* "Email", "Phone".
* То есть условно говоря в таблице будут храниться данные о друзьях - имя,
* электронный адрес и номер телефона.
* Константа CONTENT_AUTHORITY описывает название контент-провайдера.
* То есть в моем случае провайдер будет называться "org.itstep.vpu911.mycontentprovider".
* С помощью имени провайдера создается константа CONTENT_AUTHORITY_URI - универсальный
* локатор или своего рода путь, через который мы будем обращаться к провайдеру при
* выполнении с ним различных операций.
*
* Также класс определяет две константы CONTENT_TYPE и CONTENT_ITEM_TYPE,
* которые определяют тип возвращаемого содержимого. Здесь есть два варианта:
* возвращение набора данных и возвращение одного объекта.
* Значение, определяющее набор данных, строится по приницпу
* "vnd.android.cursor.dir/vnd.[name].[table]",
* где в качестве [name] обычно выступает глобально уникальный идентификатор,
* например, название провайдера или имя пакета провайдера.
* А в качестве [table], как правило, используется имя таблицы.
* По похожей схеме строится второе значение, только вместо "dir" ставится "item".
*
* Также в классе определяется вспомогательная константа CONTENT_URI, которая
* описывает путь для доступа к таблице friends. И также определяем два вспомогательных метода:
* buildFriendUri() (возвращает uri для доступа к объекту по опреленному id) и
* getFriendId (для извлечения id из переданного пути uri).
*/

public class FriendsContract {

    static final String TABLE_NAME = "friends";
    static final String CONTENT_AUTHORITY = "org.itstep.pd011.customasynccontentprovider";
    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE= "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    public static class Columns {
        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";

        private Columns(){ }
    } // Columns

    static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    // создает uri с помощью id
    static Uri buildFriendUri(long taskId){
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    } // buildFriendUri

    // получает id из uri
    static long getFriendId(Uri uri){
        return ContentUris.parseId(uri);
    } // getFriendId
} // class FriendsContract
