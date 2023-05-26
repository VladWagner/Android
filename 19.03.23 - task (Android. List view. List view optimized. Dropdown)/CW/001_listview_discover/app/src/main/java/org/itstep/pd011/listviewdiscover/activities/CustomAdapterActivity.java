package org.itstep.pd011.listviewdiscover.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.listviewdiscover.R;
import org.itstep.pd011.listviewdiscover.adapters.SchoolAdapter;
import org.itstep.pd011.listviewdiscover.models.School;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomAdapterActivity extends AppCompatActivity {
    // коллекция данных для обработки
    private List<School> schools;

    private ListView lsvCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_adapter);

        initializer();

        // список школьных принадлежностей
        // 1. Получение ссылки на ListView
        lsvCustomAdapter = findViewById(R.id.lsvCustomAdapter);

        // 2. Создание адаптера
        // адаптер попроще
        SchoolAdapter schoolAdapter = new SchoolAdapter(this, R.layout.school_item, schools);

        // 3. Назаначение адаптера
        lsvCustomAdapter.setAdapter(schoolAdapter);

        // назанчение слушателя события клик по элементу
        lsvCustomAdapter.setOnItemClickListener(this::onClickListner);
    } // onCreate

    // обработка клика по списку
    private void onClickListner(AdapterView<?> adapter, View view, int position, long id) {
        School school = schools.get(position);
        String str  = String.format(Locale.UK, "Вы выбрали '%s'", school.getName());

        Snackbar snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", v -> {});
        snackbar.show();
    } // onClickListner

    // инициализатор
    void initializer() {
        schools =  new ArrayList<>(Arrays.asList(
            new School("учебник",      230.0, 2, R.drawable.book),
            new School("звонок",      1230.0, 5, R.drawable.bell),
            new School("портфель",    1810.0, 1, R.drawable.briefcase),
            new School("калькулятор", 3800.0, 1, R.drawable.calculator),
            new School("циркуль",       300.0, 2, R.drawable.compass),
            new School("кофе",       50.0, 2, R.drawable.coffee),
            new School("шахматы",       570.0, 2, R.drawable.chess)
        ));
    } // initializer

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