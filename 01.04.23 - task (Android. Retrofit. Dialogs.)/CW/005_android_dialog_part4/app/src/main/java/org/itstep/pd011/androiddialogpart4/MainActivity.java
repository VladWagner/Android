    package org.itstep.pd011.androiddialogpart4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

// интерфейс Datable - самопальный интерфайс для передачи данных из
// диалога в активность
public class MainActivity extends AppCompatActivity implements Datable {

    // private ArrayAdapter<String> adapter;
    public ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        // коллекция телефонов для отображения в списке
        String[] arrPhones = {
            "Samsung Galaxy S6", "iPhone 6s", "Nokia N9", "Lenovo A2109", "Sony XA1 Ultra",
            "Motorola M8", "Meizu M8", "Xiaomi Redmi 4"
        };
        ListView phonesList = findViewById(R.id.phonesList);
        ArrayList<String> phones = new ArrayList<>(Arrays.asList(arrPhones));

        // адаптер для отображения коллекции
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phones);
        phonesList.setAdapter(adapter);

        // обработчик клика по элементу списка
        phonesList.setOnItemClickListener(onItemClickListener);
    } // onCreate

    // реализация обработчика клика по элементу списка
    private final AdapterView.OnItemClickListener onItemClickListener =
            (parent, view, position, id) -> {

                // получить выбранный объект из адаптера
                String selectedPhone = adapter.getItem(position);

                // создать диалог, передать ему строку - элемент списка
                DialogRemove dialog = new DialogRemove();
                Bundle args = new Bundle();
                args.putString("phone", selectedPhone);
                dialog.setArguments(args);

                // показать диалог
                dialog.show(getSupportFragmentManager(), "deletePhone");
            }; // onItemClick


    @Override // реализация интерфейса - удалить элемент из коллекции
    // !! работаем с объектом типа String !!
    // !!! из диалога !!!
    public void remove(String phone) {
        adapter.remove(phone);

        // не обязателен - т.к. удаление выполняется средствами адаптерпа
        adapter.notifyDataSetChanged();
    } // remove

    @Override
    // !!! из диалога !!!
    // !!! добавление объекта String в коллекцию !!! и вывод user ы Toast
    public void add(String phoneName, Customer customer) {
        adapter.add(phoneName);
        adapter.notifyDataSetChanged();  // не обязательно...

        // индикация получена информации о Закзачике
        Toast toast = Toast.makeText(this, "Заказчик " + customer, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    } // add


    //region Работа с меню приложения
    @Override  // создание меню приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    @Override // обработка выбора в меню приложения
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mniAdd:
                DialogAdd dialogAdd = new DialogAdd();
                dialogAdd.show(getSupportFragmentManager(), "dialogAdd");
                return true;
            case R.id.mniExit:
                finish();
                return true;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}