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
import com.step.home_work.activities.AnimalsListActivity;
import com.step.home_work.activities.ShipActivity;
import com.step.home_work.activities.ShipsListActivity;
import com.step.home_work.models.Animal;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.infrastructure.Utils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button shipActivityBtn;
    Button animalActivityBtn;
    Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    //Запуск активности 1
    private void startAnimalActivity(){
        Intent intent = new Intent(this, AnimalsListActivity.class);

        startActivity(intent);

    }

    //Запуск активности 2
    private void startShipActivity(){
       Intent intent = new Intent(this, ShipsListActivity.class);

        startActivity(intent);

    }

}