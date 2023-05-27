package org.itstep.pd011.customasynccontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Третьим добавим класс AppProvider, который собственно и будет представлять
// провайдер контента:
public class AppProvider extends ContentProvider {

    private AppDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int FRIENDS = 100;
    public static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // content:// org.itstep.pd011.customasynccontentprovider/FRIENDS
        matcher.addURI(FriendsContract.CONTENT_AUTHORITY, FriendsContract.TABLE_NAME, FRIENDS);

        // content:// org.itstep.pd011.customasynccontentprovider/FRIENDS/8
        matcher.addURI(FriendsContract.CONTENT_AUTHORITY, FriendsContract.TABLE_NAME + "/#", FRIENDS_ID);

        return matcher;
    } // buildUriMatcher

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    } // onCreate

    // Данный метод должен принимать пять параметров:
    // uri: путь запроса
    // projection: набор столбцов, данные для которых надо получить
    // selection: выражение для выборки типа "WHERE Name = ? ...."
    // selectionArgs: набор значений для параметров из selection (вставляются вместо знаков вопроса)
    // sortOrder: критерий сортировки, в качестве которого выступает имя столбца
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch(match){
            case FRIENDS:
                queryBuilder.setTables(FriendsContract.TABLE_NAME);
                break;
            case FRIENDS_ID:
                queryBuilder.setTables(FriendsContract.TABLE_NAME);
                long taskId = FriendsContract.getFriendId(uri);
                queryBuilder.appendWhere(FriendsContract.Columns._ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        } // switch

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    } // query

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch(match){
            case FRIENDS:
                return FriendsContract.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        } // switch
    } // getType

    // Добавление данных
    // Метод принимает два параметра:
    //uri: путь запроса
    //values: объект ContentValues, через который передаются добавляемые данные
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        if (match == FRIENDS) {
            db = mOpenHelper.getWritableDatabase();
            recordId = db.insert(FriendsContract.TABLE_NAME, null, values);
            if (recordId > 0) {
                returnUri = FriendsContract.buildFriendUri(recordId);
            } else {
                throw new SQLException("Failed to insert: " + uri.toString());
            }
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return returnUri;
    } // insert

    // Удаление данных
    // Данный метод должен принимать три параметра:
    //uri: путь запроса
    //selection: выражение для выборки типа "WHERE Name = ? ...."
    //selectionArgs: набор значений для параметров из selection (вставляются вместо знаков вопроса)
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String selectionCriteria = selection;

        if(match != FRIENDS && match != FRIENDS_ID)
            throw new IllegalArgumentException("Unknown URI: "+ uri);

        // При удалении мы можем реализовать один из двух сценариев: либо удалить
        // из таблицы набор данных (например, друзей, у которых имя Том), либо удалить
        // один объект по определенному идентифкатору.
        // В случае если идет удаление по идентификатору, то к выражению выборки
        // удаляемых данных в selection добавляется условие удаления по id
        if(match==FRIENDS_ID) {
            long taskId = FriendsContract.getFriendId(uri);
            selectionCriteria = FriendsContract.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.delete(FriendsContract.TABLE_NAME, selectionCriteria, selectionArgs);
    } // delete

    // Метод изменения данных, должен принимать четыре параметра:
    // uri: путь запроса
    // values: объект ContentValues, который определяет новые значения
    // selection: выражение для выборки типа "WHERE Name = ? ...."
    // selectionArgs: набор значений для параметров из selection (вставляются вместо знаков вопроса)
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if(match != FRIENDS && match != FRIENDS_ID)
            throw new IllegalArgumentException("Unknown URI: "+ uri);

        if(match==FRIENDS_ID) {
            long taskId = FriendsContract.getFriendId(uri);
            selectionCriteria = FriendsContract.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.update(FriendsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
    } // update
} // class AppProvider
