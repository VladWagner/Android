package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.step.home_work.R;
import com.step.home_work.adapters.AnimalsAdapter;
import com.step.home_work.adapters.ShipsAdapter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;
import com.step.home_work.models.ship.Ship;

import java.util.List;

public class AnimalsListActivity extends AppCompatActivity {

    //Список кораблей
    private List<Animal> animalsList;

    //Listview
    private ListView animalsLsv;

    private AnimalsAdapter animalsAdapter;

    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);

        Parameters.lastAnimalId = 0;
        animalsList = Utils.generateAnimalsList();

        //Получить ссылку на список
        animalsLsv = findViewById(R.id.lsvAnimalsAdapter);

        //Создать адаптер
        animalsAdapter = new AnimalsAdapter(this, R.layout.animal_item,animalsList);

        //Добавить адаптер в ListView
        animalsLsv.setAdapter(animalsAdapter);
        currentView = this.findViewById(android.R.id.content);

    }//onCreate


    //Получение отредактированной модели животного
    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData){
        super.onActivityResult(requestCode,resultCode,intentData);

        if(resultCode != Parameters.RESULT_OK){
            Utils.showSnackBar(currentView,String.format("Активность с кодом %d вернула код ошибки: %d",requestCode,resultCode));
            return;
        }

        try {

            Animal animal = intentData != null ? intentData.getParcelableExtra(Animal.class.getCanonicalName()) : null;

            if (animal == null){
                Utils.showSnackBar(currentView,String.format("Получить животное из активности с кодом %d не удалось!",requestCode));
                return;
            }

            //Найти старую запись по id
            Animal prevAnimal = animalsList.stream()
                    .filter(a -> a.getId() == animal.getId())
                    .findFirst().get();

            //Заменить найденную запись
            animalsList.set(animalsList.indexOf(prevAnimal),animal);

            animalsAdapter.notifyDataSetChanged();

            Utils.showSnackBar(currentView,String.format("Домашнее животное с id: %d изменено",animal.getId()));

        } catch (Exception e) {
            Utils.showSnackBar(currentView,e.getMessage());
        }//catch

    }//onActivityResult

    //Обработка клика по элементу - запуск окна редактирования
    /*private void onClickListener(AdapterView<?> adapter, View view, int index, long id){
        Intent intent = new Intent(this, ShipActivity.class);

        Animal animal = animalsList.get(index);

        //Передать объект животного в активность
        intent.putExtra(Animal.class.getCanonicalName(),animal);

        //Запустить активность
        startActivityForResult(intent, Parameters.ANIMAL_ACTIVITY_ID);
    }*/

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion
}