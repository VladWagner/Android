package org.itstep.pd011.listviewdiscover.activities;

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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.listviewdiscover.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // ListView, заполняемый в разметке
    ListView lsvMarkup;

    // ListView, заполняемый в коде
    ListView lsvCode;

    // Массив строк для вывода в  ListView
    String[] fruits = {"бананы", "киви", "манго", "папайя", "маракуйя", "апельсины"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // для вывода данных в ListView
        // 1. связать разметку и ссылку на объект
        lsvCode = findViewById(R.id.lsvCode);

        // 2. Создать адаптер для отображения массив строк в ListView
        //    стандартный адаптер для каждого элемента вызывет toString()
        ArrayAdapter<String> adapterFruits = new ArrayAdapter<>(
            this,  // контекст размещения - активность или фрагмент
            android.R.layout.simple_list_item_1, // идентификатор разметки
            fruits  // коллекция для отображения
        );

        // 3. Назначить адаптер элементу интерфейса
        lsvCode.setAdapter(adapterFruits);

        // список, заполненный в разметке - для демонстранции кликов по нему
        lsvMarkup = findViewById(R.id.lsvMarkup);

        // назначение обработчиков клика по спискам
        lsvMarkup.setOnItemClickListener(this::onItemClickListner1);
        lsvCode.setOnItemClickListener(this::onItemClickListner2);
    } // onCreate

    // обработчик клика по списку lsvMarkup
    private void onItemClickListner1(AdapterView<?> listView, View listItem, int position, long idItem) {
        String fruit = getResources().getStringArray(R.array.fruits)[position];
        Snackbar sb = Snackbar.make(listItem, String.format(Locale.UK, "Выбран: %s",
                fruit), Snackbar.LENGTH_INDEFINITE);
        sb.setAction("OK", v -> {});
        sb.show();
    } // onItemClicListner2

    // обработчик клика по списку lsvCode
    private void onItemClickListner2(AdapterView<?> listView, View listItem, int position, long idItem) {
        Snackbar sb = Snackbar.make(listItem, String.format(Locale.UK, "Выбран: %s",
                fruits[position]), Snackbar.LENGTH_INDEFINITE);
        sb.setAction("OK", v -> {});
        sb.show();
    } // onItemClicListner2

    // region Работа с главным меню активности
    // обработчик события создани меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // связать разметку с ссылкой на меню
        // getMenuInflater() - загрузчик меню
        // inflate()         - загрузка меню
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    // обработчик события выбора в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // обработка выбора в меню по ид пункта
        switch (item.getItemId()) {
            case R.id.mniCustomAdapter:
                startActivity(new Intent(this, CustomAdapterActivity.class));
                break;
            case R.id.mniCustomAdapterActions:
                startCustomAdapterActionActivity();
                break;
            case R.id.mniCustomOptimizedAdapter:
                startActivity(new Intent(this, CustomAdapterOptimizedActivity.class));
                break;
            case R.id.mniMultiChoiceAdd:
                startActivity(new Intent(this, MultiChoiceAddActivity.class));
                break;
            case R.id.mniSpinnersDemo:
                startActivity(new Intent(this, SpinnersDemoActivity.class));
                break;
            case R.id.mniExit:
                finish();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected


    private void startCustomAdapterActionActivity() {
        startActivity(new Intent(this, CustomAdapterActionActivity.class));
    }
    // endregion

}