package com.step.wagner.models;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.step.wagner.infrastructure.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "sensors.db";
    public static String DB_PATH;

    private static final int VERSION = 1;

    private final Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);

        this.context = context;

        //DB_PATH = Utils.getExternalPath(DB_NAME, this.context).getPath();
        DB_PATH = Utils.getInternalPath(DB_NAME, this.context).getPath();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createDb() {
        //File dbFile = Utils.getExternalPath(DB_NAME, this.context);
        File dbFile = Utils.getInternalPath(DB_NAME, this.context);


        //Если файл БД есть во внешнем хранилище
        if (dbFile.exists()) return;

        // Задать разрешения
        this.getReadableDatabase();

        //Открыть файл БД из папки assets
        try (InputStream assetsDB = context.getAssets().open(DB_NAME)) {

            //Путь к БД во внешнем хранилище
            String outFileName = DB_PATH;

            // Создать файл во внешнем хранилище
            try (OutputStream outerStorageDB = new FileOutputStream(outFileName)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;

                while ((length = assetsDB.read(buffer)) > 0) {

                    //Записать буферный массив в файл
                    outerStorageDB.write(buffer, 0, length);
                }

                outerStorageDB.flush();
            }
        } catch (IOException ex) {
            Log.d("DatabaseHelper", ex.getMessage());
        } // try-catch
    } // createDb

    //Открыть подключение к БД
    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    } // open
}
