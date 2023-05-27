package org.itstep.pd011.customasynccontentprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// для асинхронного загрузчика контента
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.InvalidParameterException;

// для асинхронной загрузки контента активность должна реализовывать интерфейс
// LoaderManager.LoaderCallbacks<Cursor>
public class AsyncLoadActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    Button btnBack;
    TextView txvContent;

    // тег для LogCat
    final String TAG = "AsyncLoadActivity_Test";

    // числовой ид для запроса к загрузчику
    final int LOADER_ID = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_load);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> back(v));

        txvContent = findViewById(R.id.txvContent);

        // при создании активности запускаем асинхронного загрузчика контента
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    } // onCreate


    //region Реализация асинхронной загрузки контента

    // Создание загрузчика курсора
    // id - числовой код запроса к загрузчику
    // bundle - параметры пользоавтеля
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == LOADER_ID) {
            Log.d(TAG, "onCreateLoader...");
            // столбцы, которые будет выбирать загрузчик
            String[] projections = {
                FriendsContract.Columns._ID,
                FriendsContract.Columns.NAME,
                FriendsContract.Columns.EMAIL,
                FriendsContract.Columns.PHONE
            };

            // создание загрузчика
            return new CursorLoader(
                this,
                FriendsContract.CONTENT_URI,  // адрес БД
                projections,                  // столбцы для выбора в запросе
                null,
                null,
                FriendsContract.Columns.NAME  // сортировать по этому полю
            );
        } else {
            throw new InvalidParameterException("Invalid loader id");
        } // if
    } // onCreateLoader

    // вызывается по окончании загрузки
    // cursor - выбранные данные, поместим их в UI
    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished...");

        // вывод в элементы интерфейса
        // пустой вывод
        if (cursor == null) {
            txvContent.setText("Нет данных по Вашему запросу");
            return;
        } // if

        // вывод при наличии данных
        String text = txvContent.getText().toString();
        while (cursor.moveToNext()) {
            String line = "";

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                line += cursor.getString(i) + " | ";
            } // for i

            text += (line + "\n");
        } // while

        txvContent.setText(text);
    } // onLoadFinished

    // сброс загрузчика перед повторным использованием
    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset...");
    } // onLoaderReset
    //endregion

    // для возврата на главную активность
    public void back(View view) {
        // LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
        finish();
    } // back
} // class AsyncLoadActivity