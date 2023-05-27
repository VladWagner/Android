package org.itstep.pd011.androiddialogpart3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

// Пример передачи из активности в диалог
public class MainActivity extends AppCompatActivity {

    // адаптер и слушатель кликов по ListView вынесены в отдельные
    // поля класса для удобства кодинга
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        // коллекция телефонов для отображения в списке
        String[] arrPhones = {
                "Samsung Galaxy S6", "iPhone 6s", "Nokia N9", "Lenovo A2109", "Sony XA1 Ultra",
                "Motorola M8", "Meizu M8", "Xiaomi Redmi 4"
        };
        ArrayList<String> phones = new ArrayList<>(Arrays.asList(arrPhones));

        // элемент для отображения коллекции - ListView
        ListView phonesList = findViewById(R.id.phonesList);

        // адаптер для отображения элементов коллекции
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phones);
        phonesList.setAdapter(adapter);

        // обработчик клика по элементу списка
        phonesList.setOnItemClickListener(onItemClickListener);
    } // onCreate


    // слушатель события клика по элементу списка
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // создать диалог, передать ему строку - элемент списка
            CustomDialogFragment dialog = new CustomDialogFragment();

            // передача параметров в диалог - через Bundle
            Bundle args = new Bundle();    // объект для передачи параметров в диалог

            // передача простого типа - String
            // выбрать телефон
            String selectedPhone = adapter.getItem(position);
            args.putString("phone", selectedPhone);

            // передача сложного типа - Customer
            Customer customer = new Customer("Елена", "Шляховая", 32_000);
            args.putParcelable("customer", customer);

            // метод базового класса DialogFragment
            dialog.setArguments(args);

            // отображение диалогового окна
            dialog.show(getSupportFragmentManager(), "dialogConfirm");
        } // onItemClick
    };

    //region Меню приложения
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // выход из приложения
        if (item.getItemId() == R.id.mniExit) {
            finish();
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity