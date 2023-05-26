package org.itstep.pd011.androiddbexists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    // объекты для базы данных
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;

    // отображение таблицы БД
    private ListView userList;
    private SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получить ссылку на элемент разметки, выводящий таблицу БД
        userList = findViewById(R.id.list);

        // по клику на элемент списка запускать активность для
        // редактирования или удаления записи из файла
        // onItemClick
        userList.setOnItemClickListener(clickListner);

        // создаем вспомогательный класс и базу данных
        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.create_db();
    } // onCreate

    // обработчк клика по списку пользоввтелей - запуск активности UserActivity
    // в режиме редактирования данных пользователя - т.к. передается id
    private AdapterView.OnItemClickListener clickListner = (parent, view, position, id) -> {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    };


    @Override  // !!! рекомендованное событие для открытия БД !!!
    public void onResume() {
        super.onResume();

        // открываем подключение
        db = databaseHelper.open();

        // получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from " + DatabaseHelper.TABLE_USERS, null);

        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_SALARY};

        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(
                this, // в какой активности
                android.R.layout.two_line_list_item,  // шаблон вывода элемента
                userCursor,  // данные для вывода
                headers,     // заголовки столбцов и соответствие им элементов разметки в адаптере
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    } // onResume

    // по нажатию на кнопку запускаем UserActivity для добавления данных
    // это таже активность, что и для редактирования или удаления
    // режим добавления - т.к. не передается id
    public void add(View view){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    } // add

    // рекомендуемое событие для закрытия БД
    @Override
    public void onDestroy(){
        super.onDestroy();

        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    } // onDestroy

    //region Стандартная работа с меню активности
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniGrid1:
                startActivity(new Intent(this, UserActivity.class));
                break;

            case R.id.mniExit:
                finish();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity