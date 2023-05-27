package org.itstep.pd011.customcontentprovider;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

// Вторым добавим класс AppDatabase по принципу синглтона организует доступ к
// базе данных и, кроме того, создает саму базу данных и добавляет в нее
// начальные данные.
public class AppDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "friends.db";
    public static final int DATABASE_VERSION = 1;

    private static AppDatabase instance = null;

    private AppDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = new AppDatabase(context);
        }
        return instance;
    } // getInstance

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + FriendsContract.TABLE_NAME + "(" +
            FriendsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            FriendsContract.Columns.NAME + " TEXT NOT NULL, " +
            FriendsContract.Columns.EMAIL + " TEXT, " +
            FriendsContract.Columns.PHONE + " TEXT NOT NULL)";
        db.execSQL(sql);

        // добавление начальных данных
        db.execSQL("INSERT INTO "+ FriendsContract.TABLE_NAME +" (" + FriendsContract.Columns.NAME +
            ", " + FriendsContract.Columns.PHONE  + ") VALUES ('Игорь', '+12345678990');");
        db.execSQL("INSERT INTO "+ FriendsContract.TABLE_NAME +" (" + FriendsContract.Columns.NAME +
            ", " + FriendsContract.Columns.EMAIL  + ", " + FriendsContract.Columns.PHONE +
            " ) VALUES ('Ирина', 'irina@mail.com', '+13456789102');");
        db.execSQL("INSERT INTO "+ FriendsContract.TABLE_NAME +" (" + FriendsContract.Columns.NAME +
            ", " + FriendsContract.Columns.EMAIL  + ", " + FriendsContract.Columns.PHONE +
            " ) VALUES ('Сергей', 'serg@mail.com', '+13456712332');");        
        db.execSQL("INSERT INTO "+ FriendsContract.TABLE_NAME +" (" + FriendsContract.Columns.NAME +
            ", " + FriendsContract.Columns.EMAIL  + ", " + FriendsContract.Columns.PHONE +
            " ) VALUES ('Юлия', 'yulia@mail.com', '+13458762332');");
    } // onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } // onUpgrade
} // class AppDatabase
