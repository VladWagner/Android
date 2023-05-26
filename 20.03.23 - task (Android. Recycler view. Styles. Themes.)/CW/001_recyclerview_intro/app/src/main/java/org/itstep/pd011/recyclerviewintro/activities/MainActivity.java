package org.itstep.pd011.recyclerviewintro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import org.itstep.pd011.recyclerviewintro.R;
import org.itstep.pd011.recyclerviewintro.adapters.AnimalAdapter;
import org.itstep.pd011.recyclerviewintro.models.Animal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// Демонстраця RecyclerView - адаптер собственной разработки
public class MainActivity extends AppCompatActivity {

    // коллекция данных
    private List<Animal> animalList;
    private RecyclerView rcvAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получение коллекции для отображения в RecyclerView
        initializeList();

        // работа с RecyclerView
        rcvAnimals = findViewById(R.id.rcvAnimals);
        AnimalAdapter adapter = new AnimalAdapter(this, animalList);
        rcvAnimals.setAdapter(adapter);
    } // onCreate

    // инициализация списка животных для отображения в ListView
    private void initializeList() {
        // при создании коллекции оператором animalList = Arrays.asList(...)
        // получаем по сути массив, к которому не применимы операции добавления
        // и удаления элементов
        animalList = new ArrayList<>(Arrays.asList(
            new Animal("кот", 3.1, R.drawable.cat),
            new Animal("божья коровка", 0.01, R.drawable.ladybug),
            new Animal("бык", 580.0, R.drawable.bull),
            new Animal("омар", 12.4, R.drawable.lobster),
            new Animal("леопард", 56.6, R.drawable.leopard),
            new Animal("корова", 110.5, R.drawable.cow),
            new Animal("утка", 4.5, R.drawable.duck),
            new Animal("лев", 210.5, R.drawable.lion),
            new Animal("птица", 50.5, R.drawable.bird),
            new Animal("кот", 3.1, R.drawable.cat),
            new Animal("божья коровка", 0.01, R.drawable.ladybug),
            new Animal("бык", 480.0, R.drawable.bull),
            new Animal("омар", 12.4, R.drawable.lobster),
            new Animal("леопард", 56.6, R.drawable.leopard),
            new Animal("корова", 120.5, R.drawable.cow),
            new Animal("утка", 4.5, R.drawable.duck),
            new Animal("лев", 210.5, R.drawable.lion),
            new Animal("птица", 30.5, R.drawable.bird)
        ));
    } // initializeList

    //region Стандартная работа с меню активности
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniAnimalsAction:
                // старт активности
                Intent intent = new Intent(this, AnimalsActionActivity.class);
                intent.putParcelableArrayListExtra(Animal.class.getCanonicalName(), (ArrayList<? extends Parcelable>) animalList);
                startActivity(intent);
                break;

            case R.id.mniExit:
                finish();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}