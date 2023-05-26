package org.itstep.pd011.androiddbrepository;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

// создание БД, заполнение таблиц
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userstore.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных

    // название таблицы в БД
    static final String TABLE = "users";

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";

    // создание БД
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    // при подключении - создаем таблицу/таблицы если ее/их нет
    // заполненение таблиц/таблицы
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_YEAR + " INTEGER);");

        // добавление начальных данных
        // TODO: bulk insert (один insert для всех записей)
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ") VALUES ('Том Смит', 1981);");
		db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ") VALUES ('Саша Васин', 1998);");
		db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ") VALUES ('Даша Иванова', 1991);");
		db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ") VALUES ('Игорь Киров', 1987);");
		db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ") VALUES ('Валя Никитина', 1985);");
    } // onCreate

    // при передаче новой версии БД - пересоздать таблицы
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    } // onUpgrade
} // class DatabaseHelper