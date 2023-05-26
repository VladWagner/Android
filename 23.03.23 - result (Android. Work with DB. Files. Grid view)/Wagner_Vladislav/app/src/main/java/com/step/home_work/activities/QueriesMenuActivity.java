package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.home_work.R;

public class QueriesMenuActivity extends AppCompatActivity {

    Button query1Btn;
    Button query2Btn;
    Button query3Btn;
    Button exitBtn;

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries_menu);
        currentView = this.findViewById(android.R.id.content);


        query1Btn = findViewById(R.id.btnQuery1);
        query2Btn =   findViewById(R.id.btnQuery2);
        query3Btn =   findViewById(R.id.btnQuery3);
        exitBtn =           findViewById(R.id.btnExit);

        //Назначить обработчики на кнопки
        query2Btn.setOnClickListener((v) -> startQuery1Activity());
        query1Btn.setOnClickListener((v) -> startQuery2Activity());
        exitBtn.setOnClickListener((v) -> finish());
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.queries_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }



    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuBtnQuery1:
                startQuery1Activity();
                break;
            case R.id.menuBtnQuery2:
                startQuery2Activity();
                break;
            case R.id.menuBtnQuery3:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //Запуск активности коллекций
    private void startQuery1Activity(){
        /*Intent intent = new Intent(this, TelevisionsListActivity.class);

        startActivity(intent);*/

    }

    //Запуск активности судов
    private void startQuery2Activity(){
        /*Intent intent = new Intent(this, ShipsListActivity.class);

        startActivity(intent);*/

    }
}