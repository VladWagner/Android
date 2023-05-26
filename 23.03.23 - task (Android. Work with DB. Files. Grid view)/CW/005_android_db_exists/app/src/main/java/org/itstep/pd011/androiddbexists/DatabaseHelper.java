package org.itstep.pd011.androiddbexists;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// пример отдельного класса-хелпера
class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH;             // полный путь к базе данных
    private static String DB_NAME   = "enterprise.db";
    private static final int SCHEMA = 1;       // версия базы данных
    static final String TABLE_USERS = "users"; // название таблицы в бд

    // названия столбцов
    static final String COLUMN_ID   = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_YEAR = "year";
    static final String COLUMN_SALARY = "salary";

    // ссылка на активность в которой работаем
    private Context myContext;

    // в конструкторе указать путь к файлу БД
    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;

        // получить полный путь к базе данных
        DB_PATH = context.getFilesDir().getPath() + "/" + DB_NAME;
    } // DatabaseHelper

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    // собственные действия для создании БД
    void create_db(){
        // если файл БД есть, просто выходим, иначе - копируем файл
        // их папки assets
        if (new File(DB_PATH).exists()) return;

        // разрешение записи в файл базы данных - для файлового потока тоже
        this.getReadableDatabase();

        // получаем локальную бд как поток
        try (InputStream oldDatbase = myContext.getAssets().open(DB_NAME)) {
            // Путь к новой бд
            String outFileName = DB_PATH;

            // Создать файл, в котором и будет размещаться БД
            OutputStream newDatabase = new FileOutputStream(outFileName);

            // побайтово копируем данные
            byte[] buffer = new byte[1024];
            int length;
            while ((length = oldDatbase.read(buffer)) > 0) {
                newDatabase.write(buffer, 0, length);
            }

            // принудительно записать последний, не полный буфер
            // для полного буфера вызов игнорирует запись
            newDatabase.flush();

            // закрыть файловые потоки
            newDatabase.close();
        } catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        } // try-catch
    } // create_db

    // подключение к БД
    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    } // open
} // class DatabaseHelper
