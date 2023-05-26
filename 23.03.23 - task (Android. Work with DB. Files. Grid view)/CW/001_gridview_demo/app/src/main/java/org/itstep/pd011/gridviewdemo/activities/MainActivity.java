package org.itstep.pd011.gridviewdemo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.gridviewdemo.R;

public class MainActivity extends AppCompatActivity {

    // коллекция данных для отображения
    private String[] cities = {
        "Москва", "Моспино", "Макеевка", "Мариуполь",
        "Самара", "Славянск", "Снежное", "Вологда", "Волгоград",
        "Моспино", "Енакиево", "Чистяково", "Воронеж",
        "Адлер"
    };

    // сетка
    private GridView grvCities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // связать ссылку с разметкой сетки, назначить адаптер
        grvCities = findViewById(R.id.grvCities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            cities
        );
        grvCities.setAdapter(adapter);

        // обработчка клика по элементу сетки
        grvCities.setOnItemClickListener((parent, view, position, id) -> {
            Snackbar sb = Snackbar.make(view, "Клик по: \"" + cities[position] + "\"",
                    Snackbar.LENGTH_INDEFINITE);
            sb.setAction("OK" ,v -> {});
            sb.show();
        });
    } // onCreate

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
                // старт активности с GridView для отображения с использованием
                // собственного адаптера
                startActivity(new Intent(this, AnimalsActivity.class));
                break;

            case R.id.mniExit:
                finish();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity