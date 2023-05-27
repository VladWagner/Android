package org.itstep.pd011.customasynccontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Контент-провайдеры (content providers) позволяют обращаться одним приложениям
    // к данным других приложений. И мы таже можем сделать, чтобы другие приложения
    // могли обращаться к данным нашего приложения через некоторый API.
    //
    // Для этого нам надо создать свой контент-провайдер.

    // 1. Добавить класс FriendsContract
    // 2. Добавить класс AppDatabase
    // 3. Добавить класс AppProvider
    // 4. Внести изменения в манифест

    // В активности разместим код демонстрации работы провайдера данных
    private static final String TAG = "MainActivity_Test";

    Button btnAsyncLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAsyncLoader = findViewById(R.id.asyncLoaderButton);
        btnAsyncLoader.setOnClickListener(v -> asyncLoader(v));
    } // onCreate

    // переход на активность, демонстрирующую асинхронную загрузку данных
    // из провайдера контента
    public void asyncLoader(View view) {
        startActivity(new Intent(this, AsyncLoadActivity.class));
    } // asyncLoader


    // region синхронное получение данных - код предыдущего примера
    // получение всех записей
    public void getAll(View view){
        String[] projection = {
                FriendsContract.Columns._ID,
                FriendsContract.Columns.NAME,
                FriendsContract.Columns.EMAIL,
                FriendsContract.Columns.PHONE
        };
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(FriendsContract.CONTENT_URI,
                projection,
                null,
                null,
                FriendsContract.Columns.NAME);
        if(cursor != null){
            Log.d(TAG, "count: " + cursor.getCount());
            // перебор элементов
            while(cursor.moveToNext()){
                for(int i=0; i < cursor.getColumnCount(); i++){
                    Log.d(TAG, cursor.getColumnName(i) + " : " + cursor.getString(i));
                }
                Log.d(TAG, "=========================");
            }
            cursor.close();
        }
        else{
            Log.d(TAG, "Cursor is null");
        }
    }

    // Добавление
    public void add(View view){
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(FriendsContract.Columns.NAME, "Яков");
        values.put(FriendsContract.Columns.EMAIL, "jakob@gmail.com");
        values.put(FriendsContract.Columns.PHONE, "+1396254985");

        Uri uri = contentResolver.insert(FriendsContract.CONTENT_URI, values);
        Log.d(TAG, "Friend added");
    }

    // Обновление/изменение данных
    public void update(View view){
        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(FriendsContract.Columns.EMAIL, "jakob@gmail.com");
        values.put(FriendsContract.Columns.PHONE, "+222333333222");

        String selection = FriendsContract.Columns.NAME + " = 'Яков'";
        int count = contentResolver.update(FriendsContract.CONTENT_URI, values, selection, null);
        Log.d(TAG, "Friend updated");
    }

    // Удаление
    public void delete(View view){
        ContentResolver contentResolver = getContentResolver();

        String selection = FriendsContract.Columns.NAME + " = ?";
        String[] args = {"Яков"};
        int count = contentResolver.delete(FriendsContract.CONTENT_URI, selection, args);
        Log.d(TAG, "Friend deleted");
    } // delete
    // endregion

    //region Работа с меню приложения
    @Override  // создание меню приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    @Override // обработка выбора в меню приложения
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mniExit) {
            finish();
            return true;
        } // if
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity