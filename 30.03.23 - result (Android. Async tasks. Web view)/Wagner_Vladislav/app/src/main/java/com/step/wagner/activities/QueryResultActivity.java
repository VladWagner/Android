package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.step.wagner.R;
import com.step.wagner.intefaces.FragmentReceiver;

public class QueryResultActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        //Если ориентация горизонтальная, тогда выходим из активность
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return;
        }

        findViewById(R.id.btnExit).setOnClickListener(v -> finish());

        intent = getIntent();
    }//OnCreate

    //Получить результат запроса и задать результат во фрагмент
    @Override
    public void onResume() {
        super.onResume();

        FragmentReceiver fragment = (FragmentReceiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        if (fragment == null)
            return;

        //Получить номер запрсоа
        int queryNum = intent.getIntExtra("query_number",1);

        fragment.queries(queryNum);

    } // onResume

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