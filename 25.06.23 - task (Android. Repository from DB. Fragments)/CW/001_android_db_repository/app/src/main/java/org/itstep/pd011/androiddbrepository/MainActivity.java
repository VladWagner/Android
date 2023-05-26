package org.itstep.pd011.androiddbrepository;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // элемент отображения списка и его адаптер
    private ListView userList;
    ArrayAdapter<User> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = findViewById(R.id.list);

        // вызов активности редактирования пользователя по клику на элементе списка
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = arrayAdapter.getItem(position);
                if(user != null) {
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    intent.putExtra("id", user.getId()); // id пользоватеоя
                    intent.putExtra("click", 42);  // можно передать еще что-то, естественно
                    startActivity(intent);
                } // if
            } // onItemClick
        });
    } // onCreate

    // открытие БД
    @Override
    public void onResume() {
        super.onResume();

        // открыть базу данных
        DatabaseRepository repository = new DatabaseRepository(this);
        repository.open();

        // получить коллекцию пользователей из БД
        List<User> users = repository.getUsers();

        // вывести в ListView при помощи адаптера
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(arrayAdapter);
        repository.close();
    } // onResume


    //region Стандартная работа с меню активности
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniAddUser:
                // запускаем UserActivity для добавления данных
                // признак добавления - ничего не передается в активность  UserActivity
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