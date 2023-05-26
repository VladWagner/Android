package org.itstep.pd011.recyclerviewintro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import org.itstep.pd011.recyclerviewintro.R;
import org.itstep.pd011.recyclerviewintro.adapters.AnimalButtonAdapter;
import org.itstep.pd011.recyclerviewintro.models.Animal;

import java.util.List;

public class AnimalsActionActivity extends AppCompatActivity {

    // коллекция данных
    private List<Animal> animalList;

    // элемент отображения списка
    private RecyclerView rcvModAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_action);

        // получение коллекции для отображения в RecyclerView
        animalList = getIntent().getParcelableArrayListExtra(Animal.class.getCanonicalName());

        // работа с RecyclerView
        rcvModAnimals = findViewById(R.id.rcvModAnimals);
        AnimalButtonAdapter adapter = new AnimalButtonAdapter(this, animalList);
        rcvModAnimals.setAdapter(adapter);
    }

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
}