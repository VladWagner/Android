package com.step.home_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.home_work.activities.TelevisionsListActivity;

public class MainActivity extends AppCompatActivity {


    Button task2ActivityBtn;
    Button task2AcitvityBtn;
    Button exitBtn;

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentView = this.findViewById(android.R.id.content);


        task2AcitvityBtn = findViewById(R.id.btnTelevisions);
        task2ActivityBtn =   findViewById(R.id.btnBooks);
        exitBtn =           findViewById(R.id.btnExit);

        //Назначить обработчики на кнопки
        task2AcitvityBtn.setOnClickListener((v) -> startTask1Activity());
        task2ActivityBtn.setOnClickListener((v) -> startTask2Activity());
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
                startTask1Activity();
                break;
            case R.id.menuBtnShip:
                startTask2Activity();
                break;
            case R.id.menuBtnExit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //Запуск активности коллекций
    private void startTask1Activity(){
        Intent intent = new Intent(this, TelevisionsListActivity.class);

        startActivity(intent);

    }

    //Запуск активности судов
    private void startTask2Activity(){
        /*Intent intent = new Intent(this, ShipsListActivity.class);

        startActivity(intent);*/

    }


}