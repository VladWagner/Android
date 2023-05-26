package org.itstep.pd011.androiddbexists;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    EditText nameBox, yearBox, salaryBox;
    Button delButton;
    Button saveButton;

    // объекты для работы с базой данных
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    long userId = 0; // значение, которое используется, если ид в активность не передается
    // т.е. создание записи

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // связь с разметкой
        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        salaryBox = findViewById(R.id.salary);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        // открыть соединение с базой данных приложения
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();

        // определить наличие экстрас в переданном интенте - т.е.
        // передавались ли параметры в вызываемую активность
        Bundle extras = getIntent().getExtras(); // можно забирать данные и напрямую из интента
        if (extras != null) {
            userId = extras.getLong("id");

            // получаем элемент по id из бд
            userCursor = db.rawQuery(
                    "select * from " + DatabaseHelper.TABLE_USERS + " where " +
                            DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});

            // заполняем элементы управления прочитанными данными
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            yearBox.setText(String.valueOf(userCursor.getInt(2)));
            salaryBox.setText(String.valueOf(userCursor.getInt(3)));
            userCursor.close();
        } else {
            // скрываем кнопку удаления, т.к. это добавление
            // новой записи
            delButton.setVisibility(View.GONE);
        } // if
    } // onCreate


    // добавление в таблицу БД новой записи или модификация существующей записи
    public void save(View view){
        // ContentValues - для записи в таблицу
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_SALARY, Integer.parseInt(salaryBox.getText().toString()));

        // cv - данные
        if (userId > 0) {
            db.update(DatabaseHelper.TABLE_USERS, cv, "_id = " + userId, null);
        } else {
            db.insert(DatabaseHelper.TABLE_USERS, null, cv);
        } // if

        // переход на вызвавшую активность
        goHome();
    } // save

    // Удаление записи по идентификатору из таблицы БД
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE_USERS, "_id = ?", new String[]{String.valueOf(userId)});

        // переход на вызвавшую активность
        goHome();
    } // delete


    // типовое действие "Домой" - переход прямо на главную активность
    // удалить все активности из стека активностей
    // запустить главную активность
    private void goHome(){
        // закрываем подключение
        db.close();

        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    } // goHome


    // перехват клавиши "Назад"
    @Override
    public void onBackPressed() { goHome(); }

    //region Меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mniBack) {
            // возврат из активности
            finish();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class UserActivity