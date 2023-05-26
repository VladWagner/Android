package org.itstep.pd011.androiddbrepository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

// Репозиторий для БД - для конкретной таблицы
public class DatabaseRepository {

    // поля для работы с БД
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseRepository(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    } // DatabaseRepository

    public DatabaseRepository open(){
        // getWritableDatabase() ВСЕГДА открывает БД в памяти устройства
        // getReadableDatabase() открывает БД по заданному пути (либо в памяти устройства либо
        //                       во внешней памяти)
        database = dbHelper.getWritableDatabase();
        return this;
    } // open

    public void close(){ dbHelper.close(); }

    // !!! оболочка к запросу select * from users
    private Cursor getAllEntries(){
        String[] columns = new String[] {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_YEAR};
        // 1-й параметр - таблица
        // 2-й параметр - массив строк - имен столбцов
        return  database.query(
                DatabaseHelper.TABLE, columns, null, null, null,
                null, null);
    } // getAllEntries

    // метод паттерна Репозиторий
    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    // возвращает коллекцию пользователей из таблицы БД
    public List<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = getAllEntries();  // запрос к БД
        if(cursor.moveToFirst()){
            do{
                int idIndex   = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                int yearIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR);

                int id      = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int year    = cursor.getInt(yearIndex);

                // добавление в коллекцию объекта User
                users.add(new User(id, name, year));
            } while (cursor.moveToNext());
        } // if
        cursor.close();
        return  users;
    } // getUsers

    // получить одного пользователя по id
    public User getUser(long id){
        User user = null;
        String query = String.format(
            "SELECT * FROM %s WHERE %s=?",
            DatabaseHelper.TABLE,
            DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});

        // чтение данных, если они получены
        if(cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int yearIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR);

            String name = cursor.getString(nameIndex);
            int year = cursor.getInt(yearIndex);

            user = new User(id, name, year);
        } // if

        cursor.close();
        return user;
    } // getUser

    // метод - оболочка для запроса insert
    public long insert(User user){
        // ContentValues - для добавдения данных
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, user.getName());
        cv.put(DatabaseHelper.COLUMN_YEAR, user.getYear());

        return database.insert(DatabaseHelper.TABLE, null, cv);
    } // insert

    // метод - оболочка для запроса delete
    public long delete(long userId){

        // String whereClause = DatabaseHelper.COLUMN_ID + " = ?"; // условие удаления
        String whereClause = "_id = ?"; // условие удаления
        String[] whereArgs = new String[]{String.valueOf(userId)}; // параметр для условия
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    } // delete

    // метод - оболочка для запроса update
    public long update(User user){

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + user.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, user.getName());
        cv.put(DatabaseHelper.COLUMN_YEAR, user.getYear());

        return database.update(DatabaseHelper.TABLE, cv, whereClause, null);
    } // update
} // class DatabaseRepository