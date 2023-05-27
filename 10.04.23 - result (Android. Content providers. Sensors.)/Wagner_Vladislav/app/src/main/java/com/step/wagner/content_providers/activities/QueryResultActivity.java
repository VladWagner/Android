package com.step.wagner.content_providers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.wagner.content_providers.R;

import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.interfaces.Receiver;

import java.util.ArrayList;

public class QueryResultActivity extends AppCompatActivity {

    Intent intent;

    Button setParamsBtn;
    Receiver fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries);

        //Если ориентация горизонтальная, тогда выходим из активность
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return;
        }

        setParamsBtn = findViewById(R.id.btnSetParams);
        setParamsBtn.setOnClickListener(this::onSetParamsClickListener);
        //Задать номер запроса
        setParamsBtn.setTag(1);
        findViewById(R.id.btnExit).setOnClickListener(v -> returnFromActivity());

        intent = getIntent();
    }//OnCreate

    //Получить номер и задать во фрагмент
    @Override
    public void onResume() {
        super.onResume();

        fragment = (Receiver) getSupportFragmentManager().findFragmentById(R.id.fragmentReceiver);

        if (fragment == null)
            return;

        //Получить номер запрсоа
        int queryNum = intent.getIntExtra("query_number",1);

        //Если кнопка отключена, тогда включить
        if(setParamsBtn.getVisibility() == View.GONE)
            setParamsBtn.setVisibility(View.VISIBLE);

        //Проверка - требуется ли запрос или вывод всей таблицы
        if (queryNum <= Parameters.QUERIES_AMOUNT) {
            fragment.queries(queryNum);
            setParamsBtn.setTag(queryNum);
        }
        else{
            setParamsBtn.setVisibility(View.GONE);
            fragment.tables(queryNum);
        }

    } // onResume

    //Кнопка выхода
    @Override
    public void onBackPressed() {
        returnFromActivity();
    }

    private void onSetParamsClickListener(View view){
        int queryNumber = (int)((Button) view).getTag();

        if (fragment == null)
            return;
        fragment.queries(queryNumber);
    }

    private void returnFromActivity(){
        setResult(Parameters.RESULT_OK, new Intent());

        //Выход из активности
        finish();

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
            returnFromActivity();

        return super.onOptionsItemSelected(item);
    }
    //endregion
}