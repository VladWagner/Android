package org.itstep.pd011.androiddbrepository;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    private EditText nameBox;
    private EditText yearBox;
    private Button delButton;
    private Button saveButton;

    private DatabaseRepository databaseRepository;
    private long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        delButton.setOnClickListener(v -> delete(v));
        saveButton.setOnClickListener(v -> save(v));

        // открыть БД
        databaseRepository = new DatabaseRepository(this);
        databaseRepository.open();

        // доступ к коллекции экстрас - параметры активити
        // равны null при первом запуске активности
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        } // if

        // если userId == 0, то добавление новой записи
        if (userId > 0) {
            // получаем элемент по id из бд
            User user = databaseRepository.getUser(userId); // чтение из БД

            // установить поля элементов управления
            nameBox.setText(user.getName());
            yearBox.setText(String.valueOf(user.getYear()));
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        } // if
    } // onCreate

    // запись в БД
    public void save(View view){
        // получить значения из элементов UI для создания объекта User
        String name = nameBox.getText().toString();
        int year = Integer.parseInt(yearBox.getText().toString());

        // создать объект из полученных значений
        User user = new User(userId, name, year);

        if (userId > 0) {
            databaseRepository.update(user);
        } else {
            databaseRepository.insert(user);
        } // if

        // при завершении операции с БД переход на главную активность
        goHome();
    } // save


    // операция удаления
    public void delete(View view){
        databaseRepository.delete(userId);

        // при завершении операции с БД переход на главную активность
        goHome();
    } // delete

    // Для корректной обработки кнопки "Назад"
    @Override protected void onDestroy() {
        super.onDestroy();

        // закрыть соединение с БД только при выходе из активности
        databaseRepository.close();
        goHome();
    } // onDestroy

    // переход к главной activity
    private void goHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    } // goHome

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
            goHome();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class UserActivity