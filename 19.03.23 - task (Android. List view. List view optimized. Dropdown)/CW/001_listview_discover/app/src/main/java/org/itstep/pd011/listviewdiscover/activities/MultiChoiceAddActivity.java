package org.itstep.pd011.listviewdiscover.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.itstep.pd011.listviewdiscover.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiChoiceAddActivity extends AppCompatActivity {

    // для заполнения в коде
    private ListView lsvFruits;
    private ArrayList<String> fruits = new ArrayList<>(
            Arrays.asList("яблоки", "груши", "мандарины", "апельсины", "бананы")
    );

    // выбранные элементы списка - для удаления, отобржаения
    List<String> selected = new ArrayList<>();

    // поле вывода
    private TextView txvSelectedItems;
    private EditText edtAddItem;

    // должен быть полем класса для доступности в методах добавления
    // и удаления элемента
    private ArrayAdapter<String> fruitsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_choice_add);

        // в этот TextView выводим информацию о выбранном элементе ListView
        txvSelectedItems = findViewById(R.id.txvItems);
        edtAddItem = findViewById(R.id.edtAddItem);

        // как отобразить коллекцию в ListView
        // 1. найти ссылку на разметку ListView
        lsvFruits = findViewById(R.id.lsvAddRemoveItem);

        // 2. Настроить адаптер отображения коллекци в ListView
        fruitsAdapter = new ArrayAdapter<>(
                this,                         // для текущей активности
                // разметка вывода элемента с возможностью выбора
                android.R.layout.simple_list_item_multiple_choice,
                fruits                               // обрабатываемая коллекция
        );

        // 3. Назначить адаптер элементу ListView
        lsvFruits.setAdapter(fruitsAdapter);

        // назначить обработчик клика по ListView
        // parent   - ListView
        // view     - элемент, виджет, по которому кликнули
        // position - индекс в коллекции
        // id       - идентификатор нажатого элемента
        lsvFruits.setOnItemClickListener((parent, view, position, id) -> {
            // получаем нажатый элемент - для удаления
            String item = fruits.get(position);
            if(lsvFruits.isItemChecked(position))
                selected.add(item);
            else
                selected.remove(item);

            // для вывода в TextView выбранных элементов
            StringBuilder result = new StringBuilder();
            for (String str : selected)
                result.append(str).append("\n");

            txvSelectedItems.setText(result);
        });
    } // onCreate

    // добавление элемента
    public void addItem(View view) {
        String temp = edtAddItem.getText().toString();
        if (temp.isEmpty()) return;

        // собственно добавление
        fruitsAdapter.add(temp);
        fruitsAdapter.notifyDataSetChanged();

        // очистка поля ввода
        edtAddItem.getText().clear();
    } // addItem

    // удаление выбранного элемента/выбранных элементов
    public void remove(View view) {
        // получаем и удаляем выделенные элементы
        for(int i=0; i< selected.size(); i++)
            fruitsAdapter.remove(selected.get(i));

        // собственно удаление элементов из коллекции
        fruitsAdapter.notifyDataSetChanged();

        // снимаем все ранее установленные отметки в listView
        lsvFruits.clearChoices();

        // очищаем массив выбраных объектов
        selected.clear();

        // очищаем поле вывода выбранных элементов
        txvSelectedItems.setText("");
    } // remove

    //region Меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);
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
}