package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.step.home_work.R;
import com.step.home_work.adapters.AnimalAdapter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalsListActivity extends AppCompatActivity {

    //Список кораблей
    private List<Animal> animalsList;

    //RecyclerView
    private RecyclerView animalsRcv;

    private AnimalAdapter animalAdapter;

    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);

        animalsList = getIntent().getParcelableArrayListExtra(Animal.class.getCanonicalName());

        //Получить ссылку на список
        animalsRcv = findViewById(R.id.rcvAnimalsAdapter);

        //Создать адаптер
        animalAdapter = new AnimalAdapter(this, R.layout.animal_item_full,animalsList);

        //Добавить адаптер в RecyclerView
        animalsRcv.setAdapter(animalAdapter);
        currentView = this.findViewById(android.R.id.content);

    }//onCreate


    //Получение отредактированной модели животного
    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData){
        super.onActivityResult(requestCode,resultCode,intentData);

        if(resultCode != Parameters.RESULT_OK){
            return;
        }

        try {

            Animal animal = intentData != null ? intentData.getParcelableExtra(Animal.class.getCanonicalName()) : null;
            boolean isAdding = intentData != null && intentData.getBooleanExtra("isAdding", false);

            if (animal == null){
                Utils.showSnackBar(currentView,String.format("Получить животное из активности с кодом %d не удалось!",requestCode));
                return;
            }

            //Добавление или изменение элемента списка
            if (!isAdding) {

                //Найти старую запись по id
                Animal prevAnimal = animalsList.stream()
                        .filter(a -> a.getId() == animal.getId())
                        .findFirst().get();

                //Заменить найденную запись
                animalsList.set(animalsList.indexOf(prevAnimal), animal);
            }
            else animalsList.add(animal);

            animalAdapter.notifyDataSetChanged();

            Utils.showSnackBar(currentView,String.format("Домашнее животное с id: %d %s",animal.getId(), isAdding ? "добавлено" : "изменено"));

        } catch (Exception e) {
            Utils.showSnackBar(currentView,e.getMessage());
        }//catch

    }//onActivityResult

    //Обработка клика по элементу - запуск окна редактирования
    private void startAddingForm(){
        Intent intent = new Intent(this, AnimalActivity.class);

        Animal animal = Animal.factory();

        //Передать объект животного в активность
        intent.putExtra(Animal.class.getCanonicalName(),animal);

        //Режим работы формы - добавление
        intent.putExtra("isAdding",true);

        //Запустить активность
        startActivityForResult(intent, Parameters.ANIMAL_ACTIVITY_ID);
    }


    //Возврать из активности
    private void returnFromActivity() {

        Intent intent = new Intent();

        //Задать изменённую модель
        intent.putParcelableArrayListExtra(Animal.class.getCanonicalName(), (ArrayList<? extends Parcelable>) animalsList);

        setResult(Parameters.RESULT_OK, intent);

        //Выход из активности
        finish();

    }

    //Кнопка выхода
    @Override
    public void onBackPressed(){
        returnFromActivity();
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnAdd:
                startAddingForm();
                break;
            case R.id.btnExitFromActivity:
                returnFromActivity();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}