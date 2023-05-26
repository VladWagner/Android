package com.step.home_work;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.activities.AnimalActivity;
import com.step.home_work.activities.AnimalsListActivity;
import com.step.home_work.activities.ShipActivity;
import com.step.home_work.activities.ShipsListActivity;
import com.step.home_work.adapters.AnimalAdapter;
import com.step.home_work.adapters.AnimalAdapterSimple;
import com.step.home_work.adapters.ShipAdapter;
import com.step.home_work.adapters.ShipAdapterSimple;
import com.step.home_work.models.Animal;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.infrastructure.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<Ship> ships;
    List<Animal> animals;

    //RecyclerView
    private RecyclerView animalsRcv;
    private AnimalAdapterSimple animalAdapter;

    private RecyclerView shipsRcv;

    private ShipAdapterSimple shipsAdapter;

    Button shipActivityBtn;
    Button animalActivityBtn;
    Button exitBtn;

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentView = this.findViewById(android.R.id.content);

        //Генерация коллекций
        Parameters.lastShipId = 0;

        ships = Utils.generateShipsList(8);

        shipsRcv = findViewById(R.id.rcvShipsAdapter);

        //Создать адаптер
        shipsAdapter = new ShipAdapterSimple(this, R.layout.ship_item_simple,ships);

        //Добавить адаптер в ListView
        shipsRcv.setAdapter(shipsAdapter);

        Parameters.lastAnimalId = 0;
        animals = Utils.generateAnimalsList(8);

        //Получить ссылку на список
        animalsRcv = findViewById(R.id.rcvAnimalsAdapter);

        //Создать адаптер
        animalAdapter = new AnimalAdapterSimple(this, R.layout.animal_item_simple,animals);

        //Добавить адаптер в RecyclerView
        animalsRcv.setAdapter(animalAdapter);

        animalActivityBtn = findViewById(R.id.btnAnimal);
        shipActivityBtn =   findViewById(R.id.btnShip);
        exitBtn =           findViewById(R.id.btnExit);

        //Назначить обработчики на кнопки
        animalActivityBtn.setOnClickListener((v) -> startAnimalActivity());
        shipActivityBtn.setOnClickListener((v) -> startShipActivity());
        exitBtn.setOnClickListener((v) -> finish());


    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }



    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuBtnAnimal:
                startAnimalActivity();
                break;
            case R.id.menuBtnShip:
                startShipActivity();
                break;
            case R.id.menuBtnExit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //Запуск активности животных
    private void startAnimalActivity(){
        Intent intent = new Intent(this, AnimalsListActivity.class);

        //Передать коллекцию
        intent.putParcelableArrayListExtra(Animal.class.getCanonicalName(), (ArrayList<? extends Parcelable>) animals);

        startActivityForResult(intent,Parameters.ANIMAL_ACTIVITY_ID);

    }

    //Запуск активности судов
    private void startShipActivity(){
        Intent intent = new Intent(this, ShipsListActivity.class);

        //Передать коллекцию
        intent.putParcelableArrayListExtra(Ship.class.getCanonicalName(), (ArrayList<? extends Parcelable>) ships);

        startActivityForResult(intent,Parameters.SHIP_ACTIVITY_ID);

    }


    //Получение коллекций из активностей
    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData){
        super.onActivityResult(requestCode,resultCode,intentData);

        if(resultCode != Parameters.RESULT_OK){
            return;
        }


        try {

            //Получение значений
            switch(requestCode){
                //Коллекция домашних животных
                case Parameters.ANIMAL_ACTIVITY_ID: {
                    animals = intentData != null ? intentData.getParcelableArrayListExtra(Animal.class.getCanonicalName()) : animals;

                    //Изменить вывод
                    animalAdapter.notifyItemRangeChanged(0,animals.size());
                    //animalAdapter.notifyDataSetChanged();
                    break;
                }
                //Активность изменения корабля корабля
                case Parameters.SHIP_ACTIVITY_ID:{
                    ships = intentData != null ? intentData.getParcelableArrayListExtra(Ship.class.getCanonicalName()) : ships;

                    //Изменить вывод
                    shipsAdapter.notifyItemRangeChanged(0,ships.size());

                    //shipsAdapter.notifyDataSetChanged();
                    break;
                }
            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(currentView,e.getMessage());
        }//catch

    }

}