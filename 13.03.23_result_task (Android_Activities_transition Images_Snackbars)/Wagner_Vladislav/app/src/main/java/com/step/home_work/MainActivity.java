package com.step.home_work;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.activities.AnimalActivity;
import com.step.home_work.activities.ShipActivity;
import com.step.home_work.models.Animal;
import com.step.home_work.models.Parameters;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
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
        animalFileNameView = findViewById(R.id.animalFileName);

        animalImageView =    findViewById(R.id.catImage);

        shipTypeView =     findViewById(R.id.shipType);
        loadCapacityView = findViewById(R.id.loadCapacity);
        destinationView =  findViewById(R.id.destination);
        cargoTypeView =    findViewById(R.id.cargoType);
        cargoWeightView =  findViewById(R.id.cargoWeight);
        tonPriceView =     findViewById(R.id.tonPrice);
        shipFileNameView = findViewById(R.id.shipFileName);

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

        setImageView(animal.getFileName(),animalImageView);
        setImageView(ship.getFileName(),shipImageView);

    }


    //Записать изображение в imageView
    public void setImageView(String fileName, ImageView imgView){

        //Окрыть поток чтения из assets
        try(InputStream is = getApplicationContext().getAssets().open(fileName)){
            Drawable drawable = Drawable.createFromStream(is,null);
            imgView.setImageDrawable(drawable);

        }catch (IOException e){
            showSnackBar(e.getMessage());
        }
    }

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


        //Получение значений
        switch(requestCode){
            //Активность домашнего животного
            case Parameters.ANIMAL_ACTIVITY_ID: {
                animal = intentData != null ? intentData.getParcelableExtra(Animal.class.getCanonicalName()) : animal;
                setAnimalTextViews();
                break;
            }
            //Активность изменения корабля корабля
            case Parameters.SHIP_ACTIVITY_ID:{
                ship = intentData != null ? intentData.getParcelableExtra(Ship.class.getCanonicalName()) : ship;
                setShipTextViews();
                //setImageView(ship.getFileNameFromType(),shipImageView);
                break;
            }
        }

    }


    private void setShipTextViews(){

        shipTypeView.setText(String.format("Тип судна: %s",ship.getShipType()));
        loadCapacityView.setText(String.format("Грузоподъемность: %s т.", Utils.numbersFormatter.format(ship.getLoadCapacity()) ));

        destinationView.setText(String.format("Пункт назначения: %s",ship.getDestination()));
        cargoTypeView.setText(String.format("Тип груза: %s",ship.getCargoType()));

        cargoWeightView.setText((String.format("Вес груза: %s т.",Utils.numbersFormatter.format(ship.getCargoWeight())) ) );
        tonPriceView.setText((String.format(Locale.UK,"Цена 1 тонны: %d руб.",ship.getTonPrice()) ) );
        shipFileNameView.setText((String.format("Имя файла: %s",ship.getFileName()) ) );
    }

    private void setAnimalTextViews(){

        breedView.setText(String.format("Порода: %s",animal.getBreed()));
        nameView.setText(String.format("Кличка: %s",animal.getName()));

        ageView.setText(String.format(Locale.UK,"Возраст: %d лет",animal.getAge()));
        weightView.setText(String.format(Locale.UK,"Вес: %d кг.",animal.getWeight()));

        ownerSnpView.setText((String.format("Владелец:\n%s",animal.getOwnerSnp()) ) );
        animalFileNameView.setText((String.format("Имя файла: %s",animal.getFileName()) ) );
    }

    //Вывести snackBar
    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content),message,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Закрыть",(v) -> {});
        snackbar.setActionTextColor(Color.CYAN);

        snackbar.show();
    }

}