package org.itstep.pd011.listviewdiscover.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.itstep.pd011.listviewdiscover.R;

import java.util.ArrayList;
import java.util.List;

// Демонстрация выпадающих списков - Spinner
public class SpinnersDemoActivity extends AppCompatActivity {

    private TextView txvSpinner1SelectedItem;
    private TextView txvSpinner2SelectedItem;
    private TextView txvSpinner3SelectedItem;

    private Spinner spinner2;
    private String[] colors = {"красный", "оранжевый", "желтый", "зеленый", "голубой", "синий", "фиолетовый"};

    private Spinner spinner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinners_demo);

        // простейший вариант, данные спиннера задаются в разметке
        txvSpinner1SelectedItem = findViewById(R.id.txvSpinner1SelectedItem);

        // спиннеры с заданием списка выбора в коде
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        // поля отображения выбранного элемента
        txvSpinner2SelectedItem = findViewById(R.id.txvSpinner2SelectedItem);
        txvSpinner3SelectedItem = findViewById(R.id.txvSpinner3SelectedItem);

        // адаптер спиннера 2
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                colors);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        // программно формируем коллекцию числовых значений
        // для отображения в выпадающем списке спиннер
        List<String> years = new ArrayList<>();
        for (int year = 2000; year <= 2023; ++year)
            years.add(String.valueOf(year));
        // years.add(String.format("%d", year));

        // адаптер спиннера 3
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                years);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        // настройка слушателя выбора из спиннера
        ((Spinner) findViewById(R.id.spinner1)).setOnItemSelectedListener(listnerSpinner1);
        spinner3.setOnItemSelectedListener(listnerSpinner3);
        spinner2.setOnItemSelectedListener(listnerSpinner2);
    } // onCreate

    // обработка выбора в выпадающем списке 1
    AdapterView.OnItemSelectedListener listnerSpinner1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
            txvSpinner1SelectedItem.setText(listView.getItemAtPosition(position).toString());
        } // onItemSelected

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // обработка выбора в выпадающем списке 2
    AdapterView.OnItemSelectedListener listnerSpinner2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            txvSpinner2SelectedItem.setText(parent.getItemAtPosition(position).toString());
        } // onItemSelected

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // обработка выбора в выпадающем списке 3
    AdapterView.OnItemSelectedListener listnerSpinner3 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            txvSpinner3SelectedItem.setText(parent.getItemAtPosition(position).toString());
        } // onItemSelected

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // region Работа с главным меню активности
    // обработчик события создани меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // связать разметку с ссылкой на меню
        // getMenuInflater() - загрузчик меню
        // inflate()         - загрузка меню
        getMenuInflater().inflate(R.menu.back_menu, menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    // обработчик события выбора в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // обработка выбора в меню по ид пункта
        switch (item.getItemId()) {
            case R.id.mniBack:
                finish();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    // endregion
}
