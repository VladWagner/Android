package com.step.wagner.content_providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.step.wagner.content_providers.activities.AboutActivity;
import com.step.wagner.content_providers.activities.QueryResultActivity;
import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.interfaces.Sender;
import com.step.wagner.content_providers.interfaces.Receiver;
import com.step.wagner.content_providers.repositories.PublicationsRepository;
import com.step.wagner.content_providers.repositories.SubscriptionsRepository;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Sender {

    //Текущее представление
    View currentView;

    private Receiver fragmentReceiver;

    //Номер выбранного пункта меню
    private int menuItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Utils.subscriptionsRepository == null || Utils.publicationsRepository == null) {
            Utils.subscriptionsRepository = new SubscriptionsRepository(getContentResolver());
            Utils.publicationsRepository = new PublicationsRepository(getContentResolver());
        }

        currentView = this.findViewById(android.R.id.content);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragmentReceiver = (Receiver) getSupportFragmentManager().findFragmentById(R.id.fragmentReceiver);

    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnSubscriptions:

                sendQueryNumber(Parameters.SUBSCRIPTIONS_TABLE_INDEX);
                break;

            case R.id.btnPublications:

                sendQueryNumber(Parameters.PUBLICATIONS_TABLE_INDEX);

                break;

            case R.id.aboutBtn:

                startActivity(new Intent(this, AboutActivity.class));

                break;
            case R.id.menuBtnExit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //Выполнение запроса и вызов фрагмента/активности
    @SuppressLint("DefaultLocale")
    @Override
    public void sendQueryNumber(int queryNumber) {

        menuItem = queryNumber;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, QueryResultActivity.class);
            intent.putExtra("query_number", queryNumber);

            //startActivity(intent);
            startActivityForResult(intent, Parameters.QUERIES_ACTIVITY_ID);
        } else if (fragmentReceiver != null) {

            //Если требуется запрос
            if (queryNumber <= Parameters.QUERIES_AMOUNT)
                fragmentReceiver.queries(queryNumber);
                //Если требуется вывод таблицы
            else
                fragmentReceiver.tables(queryNumber);
        }
        ;

    }//sendQueryNumber

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        //Сохранить выбранный пункт меню
        outState.putInt("menuItem", menuItem);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        menuItem = savedInstanceState.getInt("menuItem", 0);

        if (menuItem > 0)
            sendQueryNumber(menuItem);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);

        if (resultCode != Parameters.RESULT_OK) {
            return;
        }

        //Если вернулись из активности, содержащей фрагмент запросов - сбросить выбранный пункт меню
        if (requestCode == Parameters.QUERIES_ACTIVITY_ID)
            menuItem = 0;

    }

}