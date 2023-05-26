package org.itstep.pd011.gridviewdemo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import org.itstep.pd011.gridviewdemo.R;
import org.itstep.pd011.gridviewdemo.adapters.AnimalGridAdapter;
import org.itstep.pd011.gridviewdemo.models.Animal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimalsActivity extends AppCompatActivity {

    // коллекция данных
    private List<Animal> animalList;

    // элемент интерфейса для отображения коллекции данных
    private GridView grvAnimals;

    // адаптер для ListView
    AnimalGridAdapter animalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        // инициализация списка животных для отображения в ListView
        initializeList();

        // создание адаптера
        animalAdapter = new AnimalGridAdapter(this, R.layout.animal_item_grid, animalList);

        // связать разметку и ссылку на GridView, установить адаптер
        grvAnimals = findViewById(R.id.grvAnimals);
        grvAnimals.setAdapter(animalAdapter);
    } // onCreate

    // инициализация списка животных для отображения в ListView
    private void initializeList() {
        // при создании коллекции оператором animalList = Arrays.asList(...)
        // получаем по сути массив, к которому не применимы операции добавления
        // и удаления элементов
        animalList = new ArrayList<>(Arrays.asList(
                new Animal("кот", 3.1, R.drawable.cat),
                new Animal("божья коровка", 0.01, R.drawable.ladybug),
                new Animal("бык", 890.0, R.drawable.bull),
                new Animal("омар", 12.4, R.drawable.lobster),
                new Animal("леопард", 56.6, R.drawable.leopard),
                new Animal("корова", 110.5, R.drawable.cow),
                new Animal("утка", 4.5, R.drawable.duck),
                new Animal("лев", 210.5, R.drawable.lion),
                new Animal("птица", 50.5, R.drawable.bird),
                new Animal("кот", 3.1, R.drawable.cat),
                new Animal("божья коровка", 0.01, R.drawable.ladybug),
                new Animal("бык", 890.0, R.drawable.bull),
                new Animal("омар", 12.4, R.drawable.lobster),
                new Animal("леопард", 56.6, R.drawable.leopard),
                new Animal("корова", 410.5, R.drawable.cow),
                new Animal("утка", 410.5, R.drawable.duck),
                new Animal("лев", 410.5, R.drawable.lion),
                new Animal("птица", 410.5, R.drawable.bird)
        ));
    } // initializeList

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
            finish();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}