package com.step.home_work;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.activities.AnimalActivity;
import com.step.home_work.activities.ShipActivity;
import com.step.home_work.models.Animal;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.infrastructure.Utils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //region Домашнее животное

    //Порода
    private TextView breedView;

    //Кличка
    private TextView nameView;

    //Возраст
    private TextView ageView;

    //Вес животного
    private TextView weightView;

    //ФИО владельца
    private TextView ownerSnpView;

    //Имя файла
    private TextView animalFileNameView;

    //Признаки животного
    private TextView animalSigns;

    //Вывод изображения
    private ImageView animalImageView;

    //endregion

    //region Судно


    //Тип корабля
    private TextView shipTypeView;

    //Грузоподъемность
    private TextView loadCapacityView;

    //Пункт назанчения
    private TextView destinationView;

    //Тип груза
    private TextView cargoTypeView;

    //Вес груза
    private TextView cargoWeightView;

    //Стоимость 1 тонны
    private TextView tonPriceView;

    //Имя файла
    private TextView shipFileNameView;

    //Признаки судна
    private TextView shipSigns;

    //Вывод изображения
    private ImageView shipImageView;
    //endregion

    Button shipActivityBtn;
    Button animalActivityBtn;
    Button exitBtn;

    //Животное
    private Animal animal;

    //Судно
    private Ship ship;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animal = Animal.factory();
        ship = Ship.factory();

        breedView =          findViewById(R.id.breed);
        nameView =           findViewById(R.id.name);
        ageView =            findViewById(R.id.age);
        weightView =         findViewById(R.id.weight);
        ownerSnpView =       findViewById(R.id.ownerSnp);
        animalSigns =       findViewById(R.id.animalSigns);
        animalFileNameView = findViewById(R.id.animalFileName);

        animalImageView =    findViewById(R.id.catImage);

        shipTypeView =     findViewById(R.id.shipType);
        loadCapacityView = findViewById(R.id.loadCapacity);
        destinationView =  findViewById(R.id.destination);
        cargoTypeView =    findViewById(R.id.cargoType);
        cargoWeightView =  findViewById(R.id.cargoWeight);
        tonPriceView =     findViewById(R.id.tonPrice);
        shipFileNameView = findViewById(R.id.shipFileName);
        shipSigns =         findViewById(R.id.shipSigns);

        shipImageView =    findViewById(R.id.shipImage);

        animalActivityBtn = findViewById(R.id.btnAnimal);
        shipActivityBtn =   findViewById(R.id.btnShip);
        exitBtn =           findViewById(R.id.btnExit);

        //Назначить обработчики на кнопки
        animalActivityBtn.setOnClickListener((v) -> startAnimalActivity());
        shipActivityBtn.setOnClickListener((v) -> startShipActivity());
        exitBtn.setOnClickListener((v) -> finish());


        setAnimalTextViews();
        setShipTextViews();

        try {
            Utils.setImageView(animal.getFileName(),animalImageView,getApplicationContext());
            Utils.setImageView(ship.getFileNameFromType(),shipImageView,getApplicationContext());
        } catch (Exception e) {
            finish();
        }

    }

    //region Сохранение состояний
    @Override
    // сохранение состояния при повороте устройства
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Animal.class.getCanonicalName(), animal);
        outState.putParcelable(Ship.class.getCanonicalName(), ship);


        super.onSaveInstanceState(outState);
    } // onSaveInstanceState

    @Override
    // Восстановление значений
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        animal = savedInstanceState.getParcelable(Animal.class.getCanonicalName());
        ship = savedInstanceState.getParcelable(Ship.class.getCanonicalName());

        setAnimalTextViews();
        setShipTextViews();

        //Установить изображения
        try {
            Utils.setImageView(animal.getFileName(),animalImageView,getApplicationContext());
            Utils.setImageView(ship.getFileNameFromType(),shipImageView,getApplicationContext());
        } catch (Exception e) {
            finish();
        }

    } // onRestoreInstanceState
    //endregion

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

    //Запуск активности 1
    private void startAnimalActivity(){
        Intent intent = new Intent(this, AnimalActivity.class);

        //Передать объект животного в активность
        intent.putExtra(Animal.class.getCanonicalName(),animal);

        //Запустить активность
        startActivityForResult(intent, Parameters.ANIMAL_ACTIVITY_ID/*.getValue()*/);

    }

    //Запуск активности 2
    private void startShipActivity(){
        Intent intent = new Intent(this, ShipActivity.class);

        //Передать объект корабля в активность
        intent.putExtra(Ship.class.getCanonicalName(),ship);

        //Запустить активность
        startActivityForResult(intent, Parameters.SHIP_ACTIVITY_ID);

    }

    //Получение объектов из активностей
    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData){
        super.onActivityResult(requestCode,resultCode,intentData);

        if(resultCode != Parameters.RESULT_OK){
            showSnackBar(String.format("Активность с кодом %d вернула код ошибки: %d",requestCode,resultCode));
            return;
        }


        try {

            //Получение значений
            switch(requestCode){
                //Активность домашнего животного
                case Parameters.ANIMAL_ACTIVITY_ID: {
                    animal = intentData != null ? intentData.getParcelableExtra(Animal.class.getCanonicalName()) : animal;
                    setAnimalTextViews();
                    Utils.setImageView(animal.getFileName(),animalImageView,getApplicationContext());
                    break;
                }
                //Активность изменения корабля корабля
                case Parameters.SHIP_ACTIVITY_ID:{
                    ship = intentData != null ? intentData.getParcelableExtra(Ship.class.getCanonicalName()) : ship;
                    setShipTextViews();
                    Utils.setImageView(ship.getFileNameFromType(),shipImageView,getApplicationContext());
                    break;
                }
            }//switch

        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }//catch

    }


    private void setShipTextViews(){

        shipTypeView.setText(String.format("Тип судна: %s",ship.getShipType()));
        loadCapacityView.setText(String.format("Грузоподъемность: %s т.", Utils.numbersFormatter.format(ship.getLoadCapacity()) ));

        destinationView.setText(String.format("Пункт назначения: %s",ship.getDestination()));
        cargoTypeView.setText(String.format("Тип груза: %s",ship.getCargoType()));

        cargoWeightView.setText((String.format("Вес груза: %s т.",Utils.numbersFormatter.format(ship.getCargoWeight())) ) );
        tonPriceView.setText((String.format(Locale.UK,"Цена 1 тонны: %d руб.",ship.getTonPrice()) ) );
        shipFileNameView.setText((String.format("Имя файла: %s",ship.getFileNameFromType()) ) );

        //Вывести признаки
        shipSigns.setText(String.format("Требуется лоцман: %s\nТребуется якорная стоянка: %s\nТребуется дозаправка : %s",
                ship.isPilotNeeds() ? "да" : "нет",
                ship.isAnchorage() ? "да" : "нет",
                ship.isRefuelingNeeds() ? "да" : "нет"
                ));
    }

    private void setAnimalTextViews(){

        breedView.setText(String.format("Порода: %s",animal.getBreed()));
        nameView.setText(String.format("Кличка: %s",animal.getName()));

        ageView.setText(String.format(Locale.UK,"Возраст: %d лет",animal.getAge()));
        weightView.setText(String.format(Locale.UK,"Вес: %.2f кг.",animal.getWeight()));

        ownerSnpView.setText((String.format("Владелец:%s",animal.getOwnerSnp()) ) );
        animalFileNameView.setText((String.format("Имя файла: %s",animal.getFileName()) ) );

        //Вывести признаки
        animalSigns.setText(String.format("Специальная диета: %s\nПолу-вольное содержание: %s",
                animal.isDiet() ? "да" : "нет",
                animal.isFreeKeeping() ? "да" : "нет"
        ));
    }

    //Вывести snackBar
    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content),message,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Закрыть",(v) -> {});
        snackbar.setActionTextColor(Color.CYAN);

        snackbar.show();
    }

}