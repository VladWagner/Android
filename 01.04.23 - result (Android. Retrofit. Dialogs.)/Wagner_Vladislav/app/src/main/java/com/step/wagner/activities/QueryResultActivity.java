package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.wagner.R;
import com.step.wagner.intefaces.Receiver;

public class QueryResultActivity extends AppCompatActivity {

    Intent intent;

    Button setParamsBtn;
    Receiver fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        //Если ориентация горизонтальная, тогда выходим из активность
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return;
        }

        setParamsBtn = findViewById(R.id.btnSetParams);
        setParamsBtn.setOnClickListener(this::onSetParamsClickListener);
        //Задать номер запроса
        setParamsBtn.setTag(1);
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());

        intent = getIntent();
    }//OnCreate

    //Получить результат запроса и задать результат во фрагмент
    @Override
    public void onResume() {
        super.onResume();

        fragment = (Receiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        if (fragment == null)
            return;

        //Получить номер запрсоа
        int queryNum = intent.getIntExtra("query_number",1);

        //Если кнопка отключена, тогда включить
        if(setParamsBtn.getVisibility() == View.GONE)
            setParamsBtn.setVisibility(View.VISIBLE);

        fragment.queries(queryNum);
        setParamsBtn.setTag(queryNum);

    } // onResume

    private void onSetParamsClickListener(View view){
        int queryNumber = (int)((Button) view).getTag();

        if (fragment == null)
            return;
        fragment.queries(queryNumber);
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion
}